package org.jwebconsole.client.application.popup.connection;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.application.popup.connection.event.HostCreatedEvent;
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
        getView().clearFields();
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
        if (getView().isFieldsValid()) {
            getView().showLoadingMask();
            HostConnection connection = createConnection();
            makeRequestToServer(connection);
        }
    }

    private void makeRequestToServer(HostConnection connection) {
        facade.connect(connection, new MethodCallback<HostConnectionResponse>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                getView().hideMask();
                facade.displayError(throwable.getMessage());
            }

            @Override
            public void onSuccess(Method method, HostConnectionResponse response) {
                getView().hideMask();
                processResponse(response);
            }
        });
    }

    private void processResponse(HostConnectionResponse response) {
        if (response.isSuccess()) {
            processSuccessResult(response.getBody());
        } else if (!response.isValid()) {
            printValidationMessages(response);
        } else if (response.isError()) {
            facade.displayError(response.getError());
        }
    }

    private void processSuccessResult(HostConnection connection) {
        getView().hideDialog();
        getEventBus().fireEvent(new HostCreatedEvent(connection));
    }

    private void printValidationMessages(HostConnectionResponse response) {
        for (ValidationMessage validationMessage : response.getMessages()) {
            Integer id = validationMessage.getId();
            if (id.equals(AppValidationId.BIG_PORT_NUMBER.getId())) {
                getView().markPortInvalid(facade.getMessage(AppValidationId.BIG_PORT_NUMBER));
            }
            if (id.equals(AppValidationId.HOST_ALREADY_CREATED.getId())) {
                getView().markHostInvalid(facade.getMessage(AppValidationId.HOST_ALREADY_CREATED));
            }
            if (id.equals(AppValidationId.HOST_ALREADY_DELETED.getId())) {
                getView().markHostInvalid(facade.getMessage(AppValidationId.HOST_ALREADY_DELETED));
            }
            if (id.equals(AppValidationId.PORT_EMPTY_MESSAGE.getId())) {
                getView().markPortInvalid(facade.getMessage(AppValidationId.PORT_EMPTY_MESSAGE));
            }
            if (id.equals(AppValidationId.HOST_NAME_EMPTY.getId())) {
                getView().markHostInvalid(facade.getMessage(AppValidationId.HOST_NAME_EMPTY));
            }
            if (id.equals(AppValidationId.PORT_NEGATIVE.getId())) {
                getView().markPortInvalid(facade.getMessage(AppValidationId.PORT_NEGATIVE));
            }
        }
    }


    private HostConnection createConnection() {
        HostConnection result = new HostConnection();
        result.setName(getView().getHostName());
        result.setPort(getView().getPort());
        result.setUser(getView().getLogin());
        result.setPassword(getView().getPassword());
        return result;
    }


    @ProxyCodeSplit
    @NameToken(NameTokens.newConnection)
    public interface ConnectionWindowProxy extends Proxy<ConnectionWindowPresenter> {

    }

}
