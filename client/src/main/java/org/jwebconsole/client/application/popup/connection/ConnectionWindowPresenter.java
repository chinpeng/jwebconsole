package org.jwebconsole.client.application.popup.connection;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.sencha.gxt.widget.core.client.form.Field;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.bundle.ValidationMessages;
import org.jwebconsole.client.event.popup.RevealConnectionPopupEvent;
import org.jwebconsole.client.event.popup.RevealConnectionPopupEventHandler;
import org.jwebconsole.client.model.base.ValidationMessage;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.jwebconsole.client.place.NameTokens;

public class ConnectionWindowPresenter extends Presenter<ConnectionWindowView, ConnectionWindowPresenter.ConnectionWindowProxy> implements
        ConnectionWindowUiHandlers,
        RevealConnectionPopupEventHandler {

    private final ConnectionWindowPresenterFacade facade;

    @Inject
    public ConnectionWindowPresenter(EventBus eventBus, ConnectionWindowView view, ConnectionWindowProxy proxy, ConnectionWindowPresenterFacade facade) {
        super(eventBus, view, proxy, RevealType.RootPopup);
        this.facade = facade;
        view.setUiHandlers(this);
    }

    @Override
    protected void onBind() {
        super.onBind();
        init();
    }

    private void init() {
        getView().getHostName().clear();
        getView().getPort().clear();
        getView().getLogin().clear();
        getView().getPassword().clear();
        getView().showDialog();
    }

    @ProxyEvent
    @Override
    public void onRevealEvent(RevealConnectionPopupEvent event) {
        if (!isBound()) {
            forceReveal();
        } else {
            init();
        }
    }

    @Override
    public void hideDialog() {
        getView().hideDialog();
    }

    @Override
    public void connectHost() {
        boolean valid = getView().getHostName().validate() && getView().getPort().validate();
        if (valid) {
            getView().getWindow().mask(facade.getMaskMessage());
            HostConnection connection = createConnection();
            makeRequestToServer(connection);
        }
    }

    private void makeRequestToServer(HostConnection connection) {
        facade.connect(connection, new MethodCallback<HostConnectionResponse>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                getView().getWindow().unmask();
                Window.alert("" + throwable);
            }

            @Override
            public void onSuccess(Method method, HostConnectionResponse response) {
                getView().getWindow().unmask();
                processResponse(response);
            }
        });
    }

    private void processResponse(HostConnectionResponse response) {
        if (response.isSuccess()) {
            getView().getWindow().hide();
        } else if (!response.isValid()) {
            printValidationMessages(response);
        } else if (response.isError()) {
            facade.displayError(response.getError());
        }
    }

    private void printValidationMessages(HostConnectionResponse response) {
        for (ValidationMessage validationMessage : response.getMessages()) {
            markInvalid(ValidationMessages.HOST_CREATED_ID, validationMessage, getView().getHostName());
            markInvalid(ValidationMessages.HOST_DELETED_ID, validationMessage, getView().getHostName());
            markInvalid(ValidationMessages.PORT_EMPTY_ID, validationMessage, getView().getPort());
            markInvalid(ValidationMessages.BIG_PORT_NUMBER_ID, validationMessage, getView().getPort());
            markInvalid(ValidationMessages.HOST_NAME_EMPTY_ID, validationMessage, getView().getHostName());
            markInvalid(ValidationMessages.PORT_MUST_BE_POSITIVE_ID, validationMessage, getView().getPort());
        }
    }

    private void markInvalid(Integer id, ValidationMessage message, Field field) {
        if (message.getId().equals(id)) {
            field.markInvalid(facade.getValidationMessage(id));
        }
    }

    private HostConnection createConnection() {
        HostConnection result = new HostConnection();
        result.setName(getView().getHostName().getValue());
        result.setPort(getView().getPort().getValue());
        result.setUser(getView().getLogin().getText());
        result.setPassword(getView().getPassword().getText());
        return result;
    }


    @ProxyCodeSplit
    @NameToken(NameTokens.newConnection)
    public interface ConnectionWindowProxy extends Proxy<ConnectionWindowPresenter> {

    }

}
