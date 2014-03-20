package org.jwebconsole.client.application.left;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.application.popup.connection.event.HostChangedEvent;
import org.jwebconsole.client.application.popup.connection.event.HostCreatedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionFailedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionStartedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionSuccessEvent;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionListResponse;
import org.jwebconsole.client.service.AppCallback;
import org.jwebconsole.client.util.TestUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class AvailableHostsPresenterTests extends Mockito {

    private AvailableHostsView view;
    private AvailableHostsPresenterFacade facade;
    private EventBus eventBus;
    private AvailableHostsPresenter.AvailableHostsProxy proxy;
    private HostConnection connection;
    private Method method;
    private SimpleEventBus realEventBus;
    private String connectionId;
    private AvailableHostsMockView mockView;

    @Before
    public void init() {
        this.view = mock(AvailableHostsView.class);
        this.facade = mock(AvailableHostsPresenterFacade.class);
        this.eventBus = mock(EventBus.class);
        this.proxy = mock(AvailableHostsPresenter.AvailableHostsProxy.class);
        this.connection = mock(HostConnection.class);
        this.method = mock(Method.class);
        this.realEventBus = new SimpleEventBus();
        this.connectionId = "test-id";
        this.mockView = new AvailableHostsMockView();
    }


    @Test
    public void shouldSetUiHandlers() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        verify(view).setUiHandlers(presenter);
    }

    @Test
    public void shouldBeSetInSlot() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        TestUtils.verifyRevealedInSlot(presenter, eventBus, ApplicationPresenter.SLOT_LEFT_PANEL);
    }

    @Test
    public void shouldSetSelectionOnReset() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        when(facade.getHostIdFromPlaceRequest()).thenReturn("test-id");
        presenter.onReset();
        ArgumentCaptor<HostConnection> captor = ArgumentCaptor.forClass(HostConnection.class);
        verify(view).setSelection(captor.capture());
        assertEquals(captor.getValue().getId(), "test-id");
    }

    @Test
    public void shouldSelectHostSilently() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, mockView, proxy, facade);
        when(facade.getHostIdFromPlaceRequest()).thenReturn("test-id");
        presenter.onReset();
        assertFalse(mockView.isSelectionFired());
    }

    @Test
    public void shouldRevealThreadPlaceWhenHostWasSelected() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        presenter.onTreeItemSelected(new HostConnection());
        when(facade.isTabNameToken()).thenReturn(false);
        verify(facade).revealThreadContentPlace(anyString());
    }

    @Test
    public void shouldRegisterSelfOnDeletionStartedEvent() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        verify(eventBus).addHandler(HostDeletionStartedEvent.TYPE, presenter);
    }


    @Test
    public void shouldRegisterSelfOnDeletionFailedEvent() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        verify(eventBus).addHandler(HostDeletionFailedEvent.TYPE, presenter);
    }

    @Test
    public void shouldRegisterSelfOnDeletionSuccessEvent() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        verify(eventBus).addHandler(HostDeletionSuccessEvent.TYPE, presenter);
    }

    @Test
    public void shouldRegisterSelfOnHostChangedEvent() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        verify(eventBus).addHandler(HostChangedEvent.TYPE, presenter);
    }

    @Test
    public void shouldRegisterSelfOnHostCreatedEvent() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        verify(eventBus).addHandler(HostCreatedEvent.TYPE, presenter);
    }

    @Test
    public void shouldShowLoadingMaskOnDeletionStart() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        presenter.onHostDeletionStarted(new HostDeletionStartedEvent());
        verify(view).showLoadingMask();
    }

    @Test
    public void shouldChangeViewOnHostCreatedEvent() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        presenter.onHostCreated(new HostCreatedEvent(connection));
        verify(view).addHost(connection);
    }

    @Test
    public void shouldChangeViewOnHostChangedEvent() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        presenter.onHostChanged(new HostChangedEvent(connection));
        verify(view).changeHost(connection);
    }


    @Test
    public void shouldHideLoadingMaskOnDeletionFailed() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        presenter.onDeletionFailed(new HostDeletionFailedEvent());
        verify(view).hideLoadingMask();
    }

    @Test
    public void shouldHideLoadingMaskOnDeletionSuccess() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        presenter.onSuccessDeletion(new HostDeletionSuccessEvent(connectionId));
        verify(view).hideLoadingMask();
    }

    @Test
    public void shouldDeleteHostConnectionOnDeletingSuccess() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        presenter.onSuccessDeletion(new HostDeletionSuccessEvent(connectionId));
        verify(view).deleteHostConnection(any(HostConnection.class));
    }

    @Test
    public void shouldDisableHandlerWhileChangingHost() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, mockView, proxy, facade);
        presenter.onHostChanged(new HostChangedEvent(new HostConnection()));
        assertFalse(mockView.isSelectionFired());
    }

    @Test
    public void shouldEnableHandlerAfterChangingHost() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, mockView, proxy, facade);
        presenter.onHostChanged(new HostChangedEvent(new HostConnection()));
        assertTrue(mockView.isEnabled());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldDisableHandlerWhileInsertingHosts() {
        when(connection.getId()).thenReturn("test-id");
        when(facade.getHostIdFromPlaceRequest()).thenReturn("test-id");
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, mockView, proxy, facade);
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.onReveal();
        verify(facade).scheduleReceiveHosts(anyInt(), argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess(method, createConnectionsResponse());
        assertFalse(mockView.isSelectionFired());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldApplyPreviousSelectionOnAddingHosts() {
        when(connection.getId()).thenReturn("test-id");
        when(facade.getHostIdFromPlaceRequest()).thenReturn("test-id");
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, mockView, proxy, facade);
        presenter.onTreeItemSelected(connection);
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.onReveal();
        verify(facade).scheduleReceiveHosts(anyInt(), argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess(method, createConnectionsResponse());
        assertEquals(connection, mockView.getSelectedItem());
    }

    @Test
    @SuppressWarnings("unused")
    public void shouldResetTimerOnHostCreationEvent() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(realEventBus, view, proxy, facade);
        realEventBus.fireEvent(new HostCreatedEvent(connection));
        verify(facade).rescheduleTimer();
    }

    @Test
    @SuppressWarnings("unused")
    public void shouldResetTimerOnHostChangeEvent() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(realEventBus, view, proxy, facade);
        realEventBus.fireEvent(new HostChangedEvent(connection));
        verify(facade).rescheduleTimer();
    }

    @Test
    public void shouldRevealCurrentPlaceIfNameTokenIsTabToken() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(realEventBus, view, proxy, facade);
        when(facade.isTabNameToken()).thenReturn(true);
        presenter.onTreeItemSelected(new HostConnection());
        verify(facade).revealCurrentPlaceWithHostId(anyString());
    }

    @Test
    public void shouldShowLoadingMaskOnReveal() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(realEventBus, view, proxy, facade);
        presenter.onReveal();
        verify(view).showLoadingMask();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldHideLoadingMaskOnResponse() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(realEventBus, view, proxy, facade);
        presenter.onReveal();
        ArgumentCaptor<AppCallback> captor = ArgumentCaptor.forClass(AppCallback.class);
        verify(facade).getHosts(captor.capture());
        captor.getValue().beforeResponse();
        verify(view).hideLoadingMask();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldClearStoreOnResponse() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(realEventBus, view, proxy, facade);
        when(connection.getId()).thenReturn("test-id");
        presenter.onReveal();
        ArgumentCaptor<AppCallback> captor = ArgumentCaptor.forClass(AppCallback.class);
        verify(facade).getHosts(captor.capture());
        HostConnectionListResponse response = createConnectionsResponse();
        captor.getValue().onSuccess(response);
        verify(view).clearStore();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFillViewWithConnections() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(realEventBus, view, proxy, facade);
        when(connection.getId()).thenReturn("test-id");
        presenter.onReveal();
        ArgumentCaptor<AppCallback> captor = ArgumentCaptor.forClass(AppCallback.class);
        verify(facade).getHosts(captor.capture());
        HostConnectionListResponse response = createConnectionsResponse();
        captor.getValue().onSuccess(response);
        verify(view).addConnection(response.getBody().get(0));
    }

    private HostConnectionListResponse createConnectionsResponse() {
        HostConnectionListResponse response = new HostConnectionListResponse();
        List<HostConnection> result = new ArrayList<HostConnection>();
        result.add(connection);
        response.setBody(result);
        return response;
    }

}
