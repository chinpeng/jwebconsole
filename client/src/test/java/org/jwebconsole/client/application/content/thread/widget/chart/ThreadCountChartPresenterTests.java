package org.jwebconsole.client.application.content.thread.widget.chart;

import com.google.web.bindery.event.shared.SimpleEventBus;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.model.base.ErrorMessage;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.thread.count.ThreadCountEntity;
import org.jwebconsole.client.model.thread.count.ThreadCountListResponse;
import org.jwebconsole.client.service.AppCallback;
import org.jwebconsole.client.service.SuccessCallback;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ThreadCountChartPresenterTests extends Mockito {

    private SimpleEventBus eventBus;
    private ThreadCountChartView view;
    private ThreadCountChartPresenterFacade facade;
    private HostConnection connection;
    private List<ThreadCountEntity> entities;
    private Date date;
    private ThreadCountListResponse response;
    private Date minDate;
    private Date maxDate;
    private int maxCount;
    private int minCount;
    private ThreadCountEntity entity;

    @Before
    @SuppressWarnings("unchecked")
    public void init() {
        this.eventBus = new SimpleEventBus();
        this.view = mock(ThreadCountChartView.class);
        this.facade = mock(ThreadCountChartPresenterFacade.class);
        this.connection = mock(HostConnection.class);
        when(connection.getId()).thenReturn("test-id");
        this.date = new Date();
        this.entity = new ThreadCountEntity(1, "test-id", date, 1, 2);
        this.entities = Collections.singletonList(entity);
        this.response = new ThreadCountListResponse();
        response.setBody(entities);
        this.minDate = mock(Date.class);
        this.maxDate = mock(Date.class);
        this.minCount = 1;
        this.maxCount = 2;
        when(facade.getMaxAxisBound(anyList())).thenReturn(maxCount);
        when(facade.getMinAxisBound(anyList())).thenReturn(minCount);
        when(facade.getMinDate(anyList())).thenReturn(minDate);
        when(facade.getMaxDate(anyList())).thenReturn(maxDate);
    }

    @Test
    public void shouldDestroyTimerOnInit() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        verify(facade).destroyTimer();
    }

    @Test
    public void shouldMaskOnInit() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        verify(view).mask(anyString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldUnmaskOnFailure() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        ArgumentCaptor<AppCallback> captor = ArgumentCaptor.forClass(AppCallback.class);
        verify(facade).getLastFifteenThreadInfoRows(anyString(), captor.capture());
        captor.getValue().onFailure(null, new RuntimeException());
        verify(view).unmask();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldUnmaskOnError() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        ArgumentCaptor<AppCallback> captor = ArgumentCaptor.forClass(AppCallback.class);
        verify(facade).getLastFifteenThreadInfoRows(anyString(), captor.capture());
        ThreadCountListResponse errorResponse = new ThreadCountListResponse();
        errorResponse.setError(new ErrorMessage(1, "error"));
        captor.getValue().onSuccess(null, errorResponse);
        verify(view).unmask();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldUnmaskOnSuccess() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        ArgumentCaptor<AppCallback> captor = ArgumentCaptor.forClass(AppCallback.class);
        verify(facade).getLastFifteenThreadInfoRows(anyString(), captor.capture());
        captor.getValue().onSuccess(null, response);
        verify(view).unmask();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldAppendBodyWithDefaultValues() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        ArgumentCaptor<AppCallback> captor = ArgumentCaptor.forClass(AppCallback.class);
        verify(facade).getLastFifteenThreadInfoRows(anyString(), captor.capture());
        captor.getValue().onSuccess(null, response);
        verify(facade).appendWithDefaultValues(response.getBody());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSetDateAxisBounds() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        ArgumentCaptor<AppCallback> captor = ArgumentCaptor.forClass(AppCallback.class);
        verify(facade).getLastFifteenThreadInfoRows(anyString(), captor.capture());
        captor.getValue().onSuccess(null, response);
        verify(view).setMaxDate(facade.getMaxDate(anyList()));
        verify(view).setMinDate(facade.getMinDate(anyList()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSetNumberAxisBounds() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        ArgumentCaptor<AppCallback> captor = ArgumentCaptor.forClass(AppCallback.class);
        verify(facade).getLastFifteenThreadInfoRows(anyString(), captor.capture());
        captor.getValue().onSuccess(null, response);
        verify(view).setMinThreadAxis(facade.getMinAxisBound(anyList()));
        verify(view).setMaxThreadAxis(facade.getMaxAxisBound(anyList()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldClearChartOnInit() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        ArgumentCaptor<AppCallback> captor = ArgumentCaptor.forClass(AppCallback.class);
        verify(facade).getLastFifteenThreadInfoRows(anyString(), captor.capture());
        captor.getValue().onSuccess(null, response);
        verify(view).clearChart();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldPopulateChartWithEntities() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        ArgumentCaptor<AppCallback> captor = ArgumentCaptor.forClass(AppCallback.class);
        verify(facade).getLastFifteenThreadInfoRows(anyString(), captor.capture());
        captor.getValue().onSuccess(null, response);
        verify(view).addThreadCountEntity(entity);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldShowChart() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        ArgumentCaptor<AppCallback> captor = ArgumentCaptor.forClass(AppCallback.class);
        verify(facade).getLastFifteenThreadInfoRows(anyString(), captor.capture());
        captor.getValue().onSuccess(null, response);
        verify(view).refreshChart();
        verify(view).showChart();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRefreshChartOnTimerUpdate() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        ArgumentCaptor<SuccessCallback> captor = ArgumentCaptor.forClass(SuccessCallback.class);
        verify(facade).scheduleUpdateTimer(anyString(), captor.capture());
        captor.getValue().onSuccess(entity);
        verify(view).refreshChart();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProvideChartAxisBoundsOnTimerUpdate() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        ArgumentCaptor<SuccessCallback> captor = ArgumentCaptor.forClass(SuccessCallback.class);
        verify(facade).scheduleUpdateTimer(anyString(), captor.capture());
        captor.getValue().onSuccess(entity);
        verify(view).setMinThreadAxis(facade.getMinAxisBound(anyList()));
        verify(view).setMaxThreadAxis(facade.getMaxAxisBound(anyList()));
        verify(view).setMaxDate(facade.getMaxDate(anyList()));
        verify(view).setMinDate(facade.getMinDate(anyList()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldClearChartOnTimerUpdate() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        ArgumentCaptor<SuccessCallback> captor = ArgumentCaptor.forClass(SuccessCallback.class);
        verify(facade).scheduleUpdateTimer(anyString(), captor.capture());
        captor.getValue().onSuccess(entity);
        verify(view).clearChart();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRemoveLastEntityOnTimerUpdate() {
        ThreadCountChartPresenter presenter = new ThreadCountChartPresenter(eventBus, view, facade);
        presenter.init(connection);
        ArgumentCaptor<SuccessCallback> captor = ArgumentCaptor.forClass(SuccessCallback.class);
        verify(facade).scheduleUpdateTimer(anyString(), captor.capture());
        captor.getValue().onSuccess(entity);
        verify(view, times(0)).addThreadCountEntity(any(ThreadCountEntity.class));
    }

}
