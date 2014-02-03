package org.jwebconsole.client.application.toolbar;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.jwebconsole.client.application.ApplicationPresenter;
import org.jwebconsole.client.application.left.event.HostSelectedEvent;
import org.jwebconsole.client.application.left.event.HostSelectedEventHandler;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.jwebconsole.client.event.RevealOnStartEventHandler;
import org.jwebconsole.client.event.popup.RevealConnectionPopupEvent;
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
        getEventBus().fireEvent(new RevealConnectionPopupEvent());
    }

    @Override
    public void deleteConnection() {
        facade.deleteHost(selectedConnection.getId(), new SuccessCallback<SimpleResponse>() {

            @Override
            public void beforeResponse() {

            }

            @Override
            public void onSuccess(SimpleResponse response) {
                if (response.isValid()) {
                    processSuccessfulDeletion();
                } else {

                }
            }
        });
    }

    private void processSuccessfulDeletion() {

    }

    @Override
    public void onHostSelected(HostSelectedEvent event) {
        this.selectedConnection = event.getConnection();
        getView().enableEditButtons();
    }



}
