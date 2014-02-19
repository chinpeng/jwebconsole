package org.jwebconsole.client.application.toolbar;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.fusesource.restygwt.client.Method;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.application.left.event.HostSelectedEvent;
import org.jwebconsole.client.application.left.event.HostSelectedEventHandler;
import org.jwebconsole.client.application.toolbar.event.HostDeletionFailedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionStartedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionSuccessEvent;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.jwebconsole.client.event.RevealOnStartEventHandler;
import org.jwebconsole.client.event.popup.RevealAddConnectionPopupEvent;
import org.jwebconsole.client.event.popup.RevealEditConnectionPopupEvent;
import org.jwebconsole.client.model.base.SimpleResponse;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.service.SuccessCallback;


public class ToolbarPresenter extends Presenter<ToolbarView, ToolbarPresenter.ToolbarProxy> implements ToolbarUiHandlers,
        RevealOnStartEventHandler,
        HostSelectedEventHandler {

    private final ToolbarPresenterFacade facade;
    private HostConnection selectedConnection;

    @ProxyCodeSplit
    public interface ToolbarProxy extends Proxy<ToolbarPresenter> {
    }

    @Inject
    public ToolbarPresenter(EventBus eventBus, ToolbarView view, ToolbarProxy proxy, ToolbarPresenterFacade facade) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_TOOLBAR);
        this.facade = facade;
        init();

    }

    private void init() {
        getView().setUiHandlers(this);
        getEventBus().addHandler(HostSelectedEvent.TYPE, this);
    }

    @Override
    protected void onBind() {
        super.onBind();
        getView().disableEditButtons();
    }

    @ProxyEvent
    @Override
    public void onApplicationStart(RevealOnStartEvent event) {
        forceReveal();
    }

    @Override
    public void openConnectionWindow() {
        getEventBus().fireEvent(new RevealAddConnectionPopupEvent());
    }

    @Override
    public void deleteConnection() {
        getEventBus().fireEvent(new HostDeletionStartedEvent());
        facade.deleteHost(selectedConnection.getId(), new SuccessCallback<SimpleResponse>() {

            @Override
            public void onFailure(Method method, Throwable throwable) {
                super.onFailure(method, throwable);
                getEventBus().fireEvent(new HostDeletionFailedEvent());
            }

            @Override
            public void onSuccess(SimpleResponse response) {
                if (response.isValid()) {
                    processSuccessfulDeletion();
                } else {
                    facade.printValidationMessages(response.getMessages());
                    getEventBus().fireEvent(new HostDeletionFailedEvent());
                }
            }
        });
    }

    @Override
    public void editConnection() {
        getEventBus().fireEvent(new RevealEditConnectionPopupEvent(selectedConnection));
    }

    private void processSuccessfulDeletion() {
        getEventBus().fireEvent(new HostDeletionSuccessEvent(selectedConnection));
        getView().disableEditButtons();
    }

    @Override
    public void onHostSelected(HostSelectedEvent event) {
        this.selectedConnection = event.getConnection();
        getView().enableEditButtons();
    }


}
