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
import org.jwebconsole.client.application.popup.connection.state.ConnectionControllerState;
import org.jwebconsole.client.bundle.AppValidationId;
import org.jwebconsole.client.event.popup.RevealAddConnectionPopupEvent;
import org.jwebconsole.client.event.popup.RevealAddConnectionPopupEventHandler;
import org.jwebconsole.client.event.popup.RevealEditConnectionPopupEvent;
import org.jwebconsole.client.event.popup.RevealEditConnectionPopupEventHandler;
import org.jwebconsole.client.model.base.ValidationMessage;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.jwebconsole.client.place.NameTokens;

public class ConnectionWindowPresenter extends Presenter<ConnectionWindowView, ConnectionWindowPresenter.ConnectionWindowProxy> implements
        ConnectionWindowUiHandlers,
        RevealAddConnectionPopupEventHandler,
        RevealEditConnectionPopupEventHandler{

    private final ConnectionWindowPresenterFacade facade;
    private final ConnectionControllerState state;

    @Inject
    public ConnectionWindowPresenter(EventBus eventBus, ConnectionWindowView view, ConnectionWindowProxy proxy, ConnectionWindowPresenterFacade facade,
                                     ConnectionControllerState state) {
        super(eventBus, view, proxy, RevealType.RootPopup);
        this.facade = facade;
        this.state = state;
        view.setUiHandlers(this);
    }

    @Override
    protected void onBind() {
        super.onBind();
    }

    private void init() {
        state.getController().initViewOnAppear();
        getView().showDialog();
    }

    @ProxyEvent
    @Override
    public void onRevealAddEvent(RevealAddConnectionPopupEvent event) {
        if (!isBound()) {
            forceReveal();
            initWithCreationState();
        } else {
           initWithCreationState();
        }

    }

    private void initWithCreationState() {
        state.becomeCreateConnectionController();
        init();
    }

    @ProxyEvent
    @Override
    public void onRevealEditEvent(RevealEditConnectionPopupEvent event) {
        if (!isBound()) {
            forceReveal();
            initWithEditState(event.getConnection());
        } else {
            initWithEditState(event.getConnection());
        }
    }

    private void initWithEditState(HostConnection connection) {
        state.becomeEditConnectionController(connection);
        init();
    }

    @Override
    public void hideDialog() {
        getView().hideDialog();
    }

    @Override
    public void connectHost() {
        if (getView().isFieldsValid()) {
            getView().showLoadingMask();
            state.getController().makeRequest(getResponseCallback());
        }
    }


    private MethodCallback<HostConnectionResponse> getResponseCallback() {
        return new MethodCallback<HostConnectionResponse>() {
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
        };
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
        state.getController().fireChangeEvent(connection);
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

    @ProxyCodeSplit
    @NameToken(NameTokens.newConnection)
    public interface ConnectionWindowProxy extends Proxy<ConnectionWindowPresenter> {

    }

}
