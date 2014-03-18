package org.jwebconsole.client.application.toolbar;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.fusesource.restygwt.client.Method;
import org.jwebconsole.client.application.left.AvailableHostsPresenter;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.application.left.event.HostSelectedEvent;
import org.jwebconsole.client.application.left.event.HostSelectedEventHandler;
import org.jwebconsole.client.application.popup.connection.event.HostChangedEvent;
import org.jwebconsole.client.application.popup.connection.event.HostChangedEventHandler;
import org.jwebconsole.client.application.toolbar.event.HostDeletionFailedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionStartedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionSuccessEvent;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.jwebconsole.client.event.RevealOnStartEventHandler;
import org.jwebconsole.client.event.popup.RevealAddConnectionPopupEvent;
import org.jwebconsole.client.event.popup.RevealEditConnectionPopupEvent;
import org.jwebconsole.client.model.base.SimpleResponse;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.service.AppCallback;


public class ToolbarPresenter extends Presenter<ToolbarView, ToolbarPresenter.ToolbarProxy> implements ToolbarUiHandlers,
        RevealOnStartEventHandler,
        HostChangedEventHandler {

    private final ToolbarPresenterFacade facade;



    @ProxyCodeSplit
    public interface ToolbarProxy extends Proxy<ToolbarPresenter> {
    }

    @Inject
    public ToolbarPresenter(EventBus eventBus, ToolbarView view, ToolbarProxy proxy, ToolbarPresenterFacade facade) {
        super(eventBus, view, proxy, AvailableHostsPresenter.SLOT_TOOLBAR);
        this.facade = facade;
        init();
    }

    private void init() {
        getView().setUiHandlers(this);
        getEventBus().addHandler(HostChangedEvent.TYPE, this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        if (facade.getCurrentConnectionId() != null) {
            getView().enableEditButtons();
        }
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
    public void onHostChanged(HostChangedEvent hostChangedEvent) {

    }

    @Override
    public void deleteConnection() {
        getEventBus().fireEvent(new HostDeletionStartedEvent());
        facade.deleteHost(facade.getCurrentConnectionId(), new AppCallback<SimpleResponse>() {

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
        getEventBus().fireEvent(new RevealEditConnectionPopupEvent());
    }

    private void processSuccessfulDeletion() {
        getEventBus().fireEvent(new HostDeletionSuccessEvent(facade.getCurrentConnectionId()));
        getView().disableEditButtons();
        facade.redirectToHome();
    }

}
