package org.jwebconsole.client.application.popup.connection;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.bundle.AppValidationId;
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
                facade.displayError(throwable.getMessage());
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
            markInvalid(validationMessage.getId(), AppValidationId.PORT_NEGATIVE, getView().getPort());
            markInvalid(validationMessage.getId(), AppValidationId.BIG_PORT_NUMBER, getView().getPort());
            markInvalid(validationMessage.getId(), AppValidationId.HOST_ALREADY_CREATED, getView().getHostName());
            markInvalid(validationMessage.getId(), AppValidationId.HOST_ALREADY_DELETED, getView().getHostName());
            markInvalid(validationMessage.getId(), AppValidationId.PORT_EMPTY_MESSAGE, getView().getPort());
            markInvalid(validationMessage.getId(), AppValidationId.HOST_NAME_EMPTY, getView().getHostName());
        }
    }

    private void markInvalid(Integer id, AppValidationId validation, Field field) {
        if (validation.getId().equals(id)) {
            field.markInvalid(facade.getMessage(validation));
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
