


package org.jwebconsole.client.application.left;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.jwebconsole.client.application.ApplicationPresenter;
import org.jwebconsole.client.application.left.event.HostSelectedEvent;
import org.jwebconsole.client.application.popup.connection.event.HostCreatedEvent;
import org.jwebconsole.client.application.popup.connection.event.HostCreatedEventHandler;
import org.jwebconsole.client.application.toolbar.event.*;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.jwebconsole.client.event.RevealOnStartEventHandler;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionListResponse;
import org.jwebconsole.client.service.SuccessCallback;

import java.util.List;


public class AvailableHostsPresenter
        extends Presenter<AvailableHostsView, AvailableHostsPresenter.AvailableHostsProxy>
        implements AvailableHostsUiHandlers,
        RevealOnStartEventHandler,
        HostDeletionFailedEventHandler,
        HostDeletionSuccessEventHandler,
        HostDeletionStartedEventHandler {


    private AvailableHostsPresenterFacade facade;


    @Inject
    public AvailableHostsPresenter(EventBus eventBus, AvailableHostsView view, AvailableHostsProxy proxy, AvailableHostsPresenterFacade facade) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_LEFT_PANEL);
        this.facade = facade;
        getView().setUiHandlers(this);
        initHandlers();
    }

    private void initHandlers() {
        getEventBus().addHandler(HostDeletionStartedEvent.TYPE, this);
        getEventBus().addHandler(HostDeletionSuccessEvent.TYPE, this);
        getEventBus().addHandler(HostDeletionFailedEvent.TYPE, this);
    }

    public void onBind() {
        super.onBind();
        init();
    }

    private void init() {
        facade.getHosts(new SuccessCallback<HostConnectionListResponse>() {
            @Override
            public void onSuccess(HostConnectionListResponse response) {
                processResponse(response.getBody());
            }
        });
        getEventBus().addHandler(HostCreatedEvent.TYPE, new HostCreatedEventHandler() {
            @Override
            public void onHostCreated(HostCreatedEvent event) {
                getView().addHost(event.getConnection());
            }
        });

    }

    private void processResponse(List<HostConnection> connections) {
        getView().fillTree(connections);
    }

    @ProxyEvent
    @Override
    public void onApplicationStart(RevealOnStartEvent event) {
        forceReveal();
    }

    @Override
    public void onTreeItemSelected(HostConnection connection) {
        getEventBus().fireEvent(new HostSelectedEvent(connection));
    }

    @Override
    public void onDeletionFailed(HostDeletionFailedEvent event) {
        getView().hideLoadingMask();
    }

    @Override
    public void onSuccessDeletion(HostDeletionSuccessEvent event) {
        getView().hideLoadingMask();
        getView().deleteHostConnection(event.getDeletedHost());
    }

    @Override
    public void onHostDeletionStarted(HostDeletionStartedEvent event) {
        getView().showLoadingMask();
    }

    @ProxyCodeSplit
    public interface AvailableHostsProxy extends Proxy<AvailableHostsPresenter> {
    }


}
