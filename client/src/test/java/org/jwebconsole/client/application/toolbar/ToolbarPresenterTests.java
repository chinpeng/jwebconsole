package org.jwebconsole.client.application.toolbar;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.popup.connection.event.HostChangedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionFailedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionStartedEvent;
import org.jwebconsole.client.application.toolbar.event.HostDeletionSuccessEvent;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.jwebconsole.client.model.base.SimpleResponse;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.service.AppCallback;
import org.jwebconsole.client.util.monad.future.Future;
import org.jwebconsole.client.util.monad.option.Option;
import org.jwebconsole.client.util.monad.trymonad.Failure;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class ToolbarPresenterTests extends Mockito {

    private ToolbarView view;
    private EventBus eventBus;
    private ToolbarPresenter.ToolbarProxy proxy;
    private ToolbarPresenterFacade facade;
    private SimpleResponse response;
    private Future<SimpleResponse> future;

    @Before
    public void init() {
        this.view = mock(ToolbarView.class);
        this.eventBus = mock(EventBus.class);
        this.proxy = mock(ToolbarPresenter.ToolbarProxy.class);
        this.facade = mock(ToolbarPresenterFacade.class);
        this.response = mock(SimpleResponse.class);
        this.future = new Future<>();
        when(facade.deleteHost(anyString())).thenReturn(future);
        when(facade.getCurrentConnectionId()).thenReturn(Option.getEmpty());
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
    public void shouldEnableEditButtonsOnHostIdRequest() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn(Option.create("test-id"));
        presenter.onReset();
        verify(view).enableEditButtons();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFireFailedEventWhenHostDeletionFailed() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn(Option.create("test-id"));
        presenter.deleteConnection();
        future.completeWithResult(new Failure <>(new RuntimeException()));
        ArgumentCaptor<HostDeletionFailedEvent> eventCaptor = ArgumentCaptor.forClass(HostDeletionFailedEvent.class);
        verify(eventBus, times(2)).fireEvent(eventCaptor.capture());
        assertEquals(eventCaptor.getValue().getClass(), HostDeletionFailedEvent.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFireSuccessEventWhenHostDeletionSucceed() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn(Option.create("test-id"));
        presenter.deleteConnection();
        future.completeWithSuccess(new SimpleResponse());
        ArgumentCaptor<HostDeletionSuccessEvent> eventCaptor = ArgumentCaptor.forClass(HostDeletionSuccessEvent.class);
        verify(eventBus, times(2)).fireEvent(eventCaptor.capture());
        assertEquals(eventCaptor.getValue().getClass(), HostDeletionSuccessEvent.class);

    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFireFailedEventWhenHostDeletionInvalid() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn(Option.create("test-id"));
        when(response.isValid()).thenReturn(false);
        presenter.deleteConnection();
        future.completeWithSuccess(response);
        ArgumentCaptor<HostDeletionFailedEvent> eventCaptor = ArgumentCaptor.forClass(HostDeletionFailedEvent.class);
        verify(eventBus, times(2)).fireEvent(eventCaptor.capture());
        assertEquals(eventCaptor.getValue().getClass(), HostDeletionFailedEvent.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldDisableButtonsAfterSuccessDeletion() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn(Option.create("test-id"));
        when(response.isValid()).thenReturn(true);
        presenter.deleteConnection();
        future.completeWithSuccess(response);
        verify(view).disableEditButtons();
    }

    @Test
    public void shouldFireDeletionStartedEventBeforeDeleting() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        presenter.deleteConnection();
        ArgumentCaptor<HostDeletionStartedEvent> eventCaptor = ArgumentCaptor.forClass(HostDeletionStartedEvent.class);
        verify(eventBus).fireEvent(eventCaptor.capture());
        assertEquals(eventCaptor.getValue().getClass(), HostDeletionStartedEvent.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRedirectToHomeAfterDeletionEvent() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn(Option.create("test-id"));
        when(response.isValid()).thenReturn(true);
        future.completeWithSuccess(new SimpleResponse());
        presenter.deleteConnection();
        verify(facade).redirectToHome();
    }

}
