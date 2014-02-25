package org.jwebconsole.client.application.content.thread;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.left.event.HostSelectedEvent;
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
        eventBus.fireEvent(new HostSelectedEvent(null));
        verify(facade).disableChart();
    }

    @Test
    public void shouldInitChartWidgetWhenConnectionIsFull() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        eventBus.fireEvent(new HostSelectedEvent(connection));
        verify(facade).revealThreadCountChartPresenter(presenter, connection);
    }

}
