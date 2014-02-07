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
import org.jwebconsole.client.event.popup.RevealAddConnectionPopupEvent;
import org.jwebconsole.client.event.popup.RevealAddConnectionPopupEventHandler;
import org.jwebconsole.client.event.popup.RevealEditConnectionPopupEvent;
import org.jwebconsole.client.event.popup.RevealEditConnectionPopupEventHandler;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.jwebconsole.client.place.NameTokens;

public class ConnectionWindowPresenter extends Presenter<ConnectionWindowView, ConnectionWindowPresenter.ConnectionWindowProxy> implements
        ConnectionWindowUiHandlers,
        RevealAddConnectionPopupEventHandler,
        RevealEditConnectionPopupEventHandler{

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
    }

    private void init() {
        facade.getController().initViewOnAppear();
        getView().clearValidations();
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
        facade.becomeCreateConnectionController();
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
        facade.becomeEditConnectionController(connection);
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
            facade.getController().makeRequest(getResponseCallback());
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
        facade.getController().fireChangeEvent(connection);
    }

    private void printValidationMessages(HostConnectionResponse response) {
        facade.validate(response.getMessages());
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.newConnection)
    public interface ConnectionWindowProxy extends Proxy<ConnectionWindowPresenter> {

    }

}
