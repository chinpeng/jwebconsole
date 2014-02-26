


package org.jwebconsole.client.application.left;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.jwebconsole.client.application.left.event.HostSelectedEvent;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.application.popup.connection.event.HostChangedEvent;
import org.jwebconsole.client.application.popup.connection.event.HostChangedEventHandler;
import org.jwebconsole.client.application.popup.connection.event.HostCreatedEvent;
import org.jwebconsole.client.application.popup.connection.event.HostCreatedEventHandler;
import org.jwebconsole.client.application.toolbar.event.*;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.jwebconsole.client.event.RevealOnStartEventHandler;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionListResponse;
import org.jwebconsole.client.service.AppCallback;

import java.util.List;


public class AvailableHostsPresenter
        extends Presenter<AvailableHostsView, AvailableHostsPresenter.AvailableHostsProxy>
        implements AvailableHostsUiHandlers,
        RevealOnStartEventHandler,
        HostDeletionFailedEventHandler,
        HostDeletionSuccessEventHandler,
        HostDeletionStartedEventHandler,
        HostChangedEventHandler,
        HostCreatedEventHandler {

    private static final int SCHEDULE_TIME = 10000;

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
        getEventBus().addHandler(HostChangedEvent.TYPE, this);
        getEventBus().addHandler(HostCreatedEvent.TYPE, this);
    }

    public void onBind() {
        super.onBind();
    }

    @Override
    public void onReset() {
        facade.stopTimer();
        super.onReset();
        init();
    }

    private void init() {
        getView().showLoadingMask();
        makeHostsListRequest();
        scheduleHostsListRequest();
    }

    private void scheduleHostsListRequest() {
        facade.scheduleReceiveHosts(SCHEDULE_TIME, new AppCallback<HostConnectionListResponse>() {
            @Override
            public void onSuccess(HostConnectionListResponse response) {
                processConnectionListResponse(response.getBody());
            }
        });
    }

    private void makeHostsListRequest() {
        facade.getHosts(new AppCallback<HostConnectionListResponse>() {

            @Override
            public void beforeResponse() {
                getView().hideLoadingMask();
            }

            @Override
            public void onSuccess(HostConnectionListResponse response) {
                processConnectionListResponse(response.getBody());
                fireHostChangedEvent(response.getBody());
            }
        });
    }

    private void fireHostChangedEvent(List<HostConnection> body) {
        HostConnection selection = getSelectionFromRequest(body);
        if (selection != null) {
            getEventBus().fireEvent(new HostSelectedEvent(selection));
        } else {
            facade.revealHomePlace();
        }
    }

    private void processConnectionListResponse(List<HostConnection> connections) {
        getView().clearStore();
        for (HostConnection connection : connections) {
            getView().addConnection(connection);
        }
        applySelection(connections);
    }

    private void applySelection(List<HostConnection> connections) {
        getView().disableSelectionHandler();
        HostConnection selection = getSelectionFromRequest(connections);
        if (selection != null) {
            getView().setSelection(selection);
        }
        getView().enableSelectionHandler();
    }

    private HostConnection getSelectionFromRequest(List<HostConnection> connections) {
        String hostId = facade.getHostIdFromPlaceRequest();
        for (HostConnection connection : connections) {
            if (connection.getId().equals(hostId)) {
                return connection;
            }
        }
        return null;
    }

    @ProxyEvent
    @Override
    public void onApplicationStart(RevealOnStartEvent event) {
        forceReveal();
    }

    @Override
    public void onTreeItemSelected(HostConnection connection) {
        facade.revealThreadContentPlace(connection.getId());
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

    @Override
    public void onHostChanged(HostChangedEvent hostChangedEvent) {
        facade.rescheduleTimer();
        getView().disableSelectionHandler();
        getView().changeHost(hostChangedEvent.getConnection());
        getView().setSelection(hostChangedEvent.getConnection());
        getView().enableSelectionHandler();
    }

    @Override
    public void onHostCreated(HostCreatedEvent event) {
        facade.rescheduleTimer();
        getView().addHost(event.getConnection());
    }

    @ProxyCodeSplit
    public interface AvailableHostsProxy extends Proxy<AvailableHostsPresenter> {
    }


}
