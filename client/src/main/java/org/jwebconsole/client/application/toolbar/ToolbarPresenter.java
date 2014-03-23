package org.jwebconsole.client.application.toolbar;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.jwebconsole.client.application.left.AvailableHostsPresenter;
import org.jwebconsole.client.application.toolbar.event.HostDeletionFailedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionStartedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionSuccessEvent;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.jwebconsole.client.event.RevealOnStartEventHandler;
import org.jwebconsole.client.event.popup.RevealAddConnectionPopupEvent;
import org.jwebconsole.client.event.popup.RevealEditConnectionPopupEvent;
import org.jwebconsole.client.model.base.SimpleResponse;
import org.jwebconsole.client.util.monad.future.Future;


public class ToolbarPresenter extends Presenter<ToolbarView, ToolbarPresenter.ToolbarProxy> implements ToolbarUiHandlers,
        RevealOnStartEventHandler {

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
    public void deleteConnection() {
        getEventBus().fireEvent(new HostDeletionStartedEvent());
        facade.getCurrentConnectionId().forEach((connectionId) -> {
            Future<SimpleResponse> future = facade.deleteHost(connectionId);
            future.handle(this::processFailure, this::processSuccess);
        });
    }

    public void processFailure(Throwable throwable) {
        facade.printUnknownErrorMessage();
        getEventBus().fireEvent(new HostDeletionFailedEvent());
    }

    public void processSuccess(SimpleResponse response) {
        if (response.isValid()) {
            processSuccessfulDeletion();
        } else {
            facade.printValidationMessages(response.getMessages());
            getEventBus().fireEvent(new HostDeletionFailedEvent());
        }
    }

    @Override
    public void editConnection() {
        getEventBus().fireEvent(new RevealEditConnectionPopupEvent());
    }

    private void processSuccessfulDeletion() {
        facade.getCurrentConnectionId().forEach((connectionId) -> {
            getEventBus().fireEvent(new HostDeletionSuccessEvent(connectionId));
            getView().disableEditButtons();
            facade.redirectToHome();
        });

    }

}
