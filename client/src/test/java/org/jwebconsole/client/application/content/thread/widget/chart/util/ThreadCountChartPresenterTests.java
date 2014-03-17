package org.jwebconsole.client.application.content.thread.widget.chart.util;

import com.google.web.bindery.event.shared.SimpleEventBus;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.content.thread.widget.chart.ThreadCountChartPresenter;
import org.jwebconsole.client.application.content.thread.widget.chart.ThreadCountChartPresenterFacade;
import org.jwebconsole.client.application.content.thread.widget.chart.ThreadCountChartView;
import org.jwebconsole.client.model.host.HostConnection;
import org.mockito.Mockito;

public class ThreadCountChartPresenterTests extends Mockito {

    private SimpleEventBus eventBus;
    private ThreadCountChartPresenterFacade facade;
    private ThreadCountChartView view;
    private HostConnection connection;

    @Before
    public void init() {
        this.eventBus = new SimpleEventBus();
        this.facade = mock(ThreadCountChartPresenterFacade.class);
        this.view = mock(ThreadCountChartView.class);
        this.connection = mock(HostConnection.class);
        when(connection.getId()).thenReturn("test-id");
    }

    @Test
    public void shouldDestroyTimerOnInit() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
    }

}
