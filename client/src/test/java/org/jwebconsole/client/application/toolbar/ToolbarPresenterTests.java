package org.jwebconsole.client.application.toolbar;

import com.google.web.bindery.event.shared.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.left.event.HostSelectedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionFailedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionStartedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionSuccessEvent;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.jwebconsole.client.model.base.SimpleResponse;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.service.AppCallback;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class ToolbarPresenterTests extends Mockito {

    private ToolbarView view;
    private EventBus eventBus;
    private ToolbarPresenter.ToolbarProxy proxy;
    private ToolbarPresenterFacade facade;
    private HostConnection connection;
    private Throwable failure;
    private SimpleResponse response;

    @Before
    public void init() {
        this.view = mock(ToolbarView.class);
        this.eventBus = mock(EventBus.class);
        this.proxy = mock(ToolbarPresenter.ToolbarProxy.class);
        this.facade = mock(ToolbarPresenterFacade.class);
        this.connection = mock(HostConnection.class);
        this.failure = mock(Throwable.class);
        this.response = mock(SimpleResponse.class);
    }

    @Test
    public void shouldRevealSelfOnStart() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        ToolbarPresenter presenterSpy = spy(presenter);
        presenterSpy.onApplicationStart(new RevealOnStartEvent());
        verify(presenterSpy).forceReveal();
    }

    @Test
    public void shouldSetUiHandlers() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        verify(view).setUiHandlers(presenter);
    }

    @Test
    public void shouldOpenConnectionWindow() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        presenter.openConnectionWindow();
        verify(eventBus).fireEvent(any(RevealOnStartEvent.class));
    }

    @Test
    public void shouldDisableEditButtonOnStart() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        presenter.onBind();
        verify(view).disableEditButtons();
    }

    @Test
    public void shouldSubscribeSelfOnHostSelectedEvent() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        verify(eventBus).addHandler(HostSelectedEvent.TYPE, presenter);
    }

    @Test
    public void shouldEnableEditButtonsOnHostSelectedEvent() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        presenter.onHostSelected(new HostSelectedEvent(new HostConnection()));
        verify(view).enableEditButtons();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFireFailedEventWhenHostDeletionFailed() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        ArgumentCaptor<AppCallback> argumentCaptor = ArgumentCaptor.forClass(AppCallback.class);
        when(connection.getId()).thenReturn("test-id");
        presenter.onHostSelected(new HostSelectedEvent(connection));
        presenter.deleteConnection();
        verify(facade).deleteHost(anyString(), argumentCaptor.capture());
        AppCallback<SimpleResponse> callback = argumentCaptor.getValue();
        callback.onFailure(null, failure);
        ArgumentCaptor<HostDeletionFailedEvent> eventCaptor = ArgumentCaptor.forClass(HostDeletionFailedEvent.class);
        verify(eventBus, times(2)).fireEvent(eventCaptor.capture());
        assertEquals(eventCaptor.getValue().getClass(), HostDeletionFailedEvent.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFireSuccessEventWhenHostDeletionSucceed() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        ArgumentCaptor<AppCallback> argumentCaptor = ArgumentCaptor.forClass(AppCallback.class);
        when(connection.getId()).thenReturn("test-id");
        presenter.onHostSelected(new HostSelectedEvent(connection));
        presenter.deleteConnection();
        verify(facade).deleteHost(anyString(), argumentCaptor.capture());
        AppCallback<SimpleResponse> callback = argumentCaptor.getValue();
        callback.onSuccess(null, new SimpleResponse());
        ArgumentCaptor<HostDeletionSuccessEvent> eventCaptor = ArgumentCaptor.forClass(HostDeletionSuccessEvent.class);
        verify(eventBus, times(2)).fireEvent(eventCaptor.capture());
        assertEquals(eventCaptor.getValue().getClass(), HostDeletionSuccessEvent.class);

    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFireFailedEventWhenHostDeletionInvalid() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        ArgumentCaptor<AppCallback> argumentCaptor = ArgumentCaptor.forClass(AppCallback.class);
        when(connection.getId()).thenReturn("test-id");
        when(response.isValid()).thenReturn(false);
        presenter.onHostSelected(new HostSelectedEvent(connection));
        presenter.deleteConnection();
        verify(facade).deleteHost(anyString(), argumentCaptor.capture());
        AppCallback<SimpleResponse> callback = argumentCaptor.getValue();
        callback.onSuccess(null, response);
        ArgumentCaptor<HostDeletionFailedEvent> eventCaptor = ArgumentCaptor.forClass(HostDeletionFailedEvent.class);
        verify(eventBus, times(2)).fireEvent(eventCaptor.capture());
        assertEquals(eventCaptor.getValue().getClass(), HostDeletionFailedEvent.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldDisableButtonsAfterSuccessDeletion() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        ArgumentCaptor<AppCallback> argumentCaptor = ArgumentCaptor.forClass(AppCallback.class);
        when(connection.getId()).thenReturn("test-id");
        when(response.isValid()).thenReturn(true);
        presenter.onHostSelected(new HostSelectedEvent(connection));
        presenter.deleteConnection();
        verify(facade).deleteHost(anyString(), argumentCaptor.capture());
        AppCallback<SimpleResponse> callback = argumentCaptor.getValue();
        callback.onSuccess(null, response);
        verify(view).disableEditButtons();
    }

    @Test
    public void shouldFireDeletionStartedEventBeforeDeleting() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        presenter.onHostSelected(new HostSelectedEvent(connection));
        presenter.deleteConnection();
        ArgumentCaptor<HostDeletionStartedEvent> eventCaptor = ArgumentCaptor.forClass(HostDeletionStartedEvent.class);
        verify(eventBus).fireEvent(eventCaptor.capture());
        assertEquals(eventCaptor.getValue().getClass(), HostDeletionStartedEvent.class);
    }

}
