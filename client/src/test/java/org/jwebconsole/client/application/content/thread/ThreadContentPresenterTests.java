package org.jwebconsole.client.application.content.thread;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.model.host.HostConnection;
import org.mockito.Mockito;

public class ThreadContentPresenterTests extends Mockito {

    private ThreadContentView view;
    private ThreadContentPresenter.ThreadContentProxy proxy;
    private EventBus eventBus;
    private ThreadContentPresenterFacade facade;
    private HostConnection connection;

    @Before
    public void init() {
        this.view = mock(ThreadContentView.class);
        this.proxy = mock(ThreadContentPresenter.ThreadContentProxy.class);
        this.eventBus = new SimpleEventBus();
        this.facade = mock(ThreadContentPresenterFacade.class);
        this.connection = mock(HostConnection.class);
        when(connection.getId()).thenReturn("test-id");
    }

    @Test
    public void shouldSetUiHandlers() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        verify(view).setUiHandlers(presenter);
    }

    @Test
    @SuppressWarnings("unused")
    public void shouldDisableChartWhenConnectionIsNull() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn(null);
        presenter.onReset();
        verify(facade).disableChart();
    }

    @Test
    public void shouldInitChartWidgetWhenConnectionIsFull() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn("test-id");
        presenter.onReset();
        verify(facade).revealThreadCountChartPresenter(presenter, connection.getId());
    }

}
