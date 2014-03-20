package org.jwebconsole.client.application.content.thread;

import com.google.web.bindery.event.shared.EventBus;
import org.fusesource.restygwt.client.MethodCallback;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.content.main.ContentTabPresenter;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.thread.details.ThreadDetailsEntity;
import org.jwebconsole.client.model.thread.details.ThreadDetailsListResponse;
import org.jwebconsole.client.model.thread.info.ThreadInfoEntity;
import org.jwebconsole.client.model.thread.info.ThreadInfoListResponse;
import org.jwebconsole.client.util.TestUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadContentPresenterTests extends Mockito {

    private ThreadContentView view;
    private ThreadContentPresenter.ThreadContentProxy proxy;
    private EventBus eventBus;
    private ThreadContentPresenterFacade facade;
    private HostConnection connection;
    private ThreadInfoListResponse threadInfoListResponse;
    private ThreadDetailsListResponse threadDetailsResponse;
    private ThreadInfoEntity thread;

    @Before
    public void init() {
        this.view = mock(ThreadContentView.class);
        this.proxy = mock(ThreadContentPresenter.ThreadContentProxy.class);
        this.eventBus = mock(EventBus.class);
        this.facade = mock(ThreadContentPresenterFacade.class);
        this.connection = mock(HostConnection.class);
        when(connection.getId()).thenReturn("test-id");
        this.threadInfoListResponse = mock(ThreadInfoListResponse.class);
        this.threadDetailsResponse = mock(ThreadDetailsListResponse.class);
        this.thread = mock(ThreadInfoEntity.class);
        when(thread.getThreadId()).thenReturn(10L);
    }

    @Test
    public void shouldSetUiHandlers() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        verify(view).setUiHandlers(presenter);
    }

    @Test
    public void shouldBeSetInSlot() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        TestUtils.verifyRevealedInSlot(presenter, eventBus, ContentTabPresenter.TYPE_SET_TAB_CONTENT);
    }

    @Test
    public void shouldClearStackTracePanelOnReset() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        presenter.onReset();
        verify(view).clearStackTracePanel();
    }

    @Test
    public void shouldDisableTimersOnReset() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        presenter.onReset();
        verify(facade).disableChart();
        verify(facade).disableThreadInfoTimer();
    }

    @Test
    public void shouldDisableTimersOnHide() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        presenter.onHide();
        verify(facade).disableChart();
        verify(facade).disableThreadInfoTimer();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldStartTimersWhenConnectionIdExists() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn("test-id");
        presenter.onReset();
        verify(facade).revealThreadCountChartPresenter(presenter, "test-id");
        verify(facade).scheduleThreadInfoRequest(anyString(), any(MethodCallback.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotStartTimersWhenConnectionIdExists() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn(null);
        presenter.onReset();
        verify(facade, never()).revealThreadCountChartPresenter(presenter, "test-id");
        verify(facade, never()).scheduleThreadInfoRequest(anyString(), any(MethodCallback.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFillViewOnSuccessThreadInfoResponse() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn("test-id");
        List<ThreadInfoEntity> body = new ArrayList<ThreadInfoEntity>();
        when(threadInfoListResponse.getBody()).thenReturn(body);
        presenter.onReset();
        ArgumentCaptor<MethodCallback> captor = ArgumentCaptor.forClass(MethodCallback.class);
        verify(facade).scheduleThreadInfoRequest(anyString(), captor.capture());
        captor.getValue().onSuccess(null, threadInfoListResponse);
        verify(view).fillThreads(body);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFillThreadDetails() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn("test-id");
        presenter.onReset();
        presenter.onThreadSelected(thread);
        List<ThreadDetailsEntity> body = new ArrayList<ThreadDetailsEntity>();
        when(threadDetailsResponse.getBody()).thenReturn(body);
        ArgumentCaptor<MethodCallback> captor = ArgumentCaptor.forClass(MethodCallback.class);
        verify(facade).makeThreadDetailsRequest(anyString(), anyLong(), captor.capture());
        captor.getValue().onSuccess(null, threadDetailsResponse);
        verify(view).fillThreadDetails(body);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRestoreSelection() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn("test-id");
        presenter.onReset();
        presenter.onThreadSelected(thread);
        presenter.onReset();
        List<ThreadInfoEntity> body = Collections.singletonList(thread);
        when(threadInfoListResponse.getBody()).thenReturn(body);
        ArgumentCaptor<MethodCallback> captor = ArgumentCaptor.forClass(MethodCallback.class);
        verify(facade, times(2)).scheduleThreadInfoRequest(anyString(), captor.capture());
        captor.getValue().onSuccess(null, threadInfoListResponse);
        verify(view).setSelection(thread);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotRestoreSelectionIfThreadIsDead() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        when(facade.getCurrentConnectionId()).thenReturn("test-id");
        presenter.onReset();
        presenter.onThreadSelected(thread);
        presenter.onReset();
        List<ThreadInfoEntity> body = new ArrayList<ThreadInfoEntity>();
        when(threadInfoListResponse.getBody()).thenReturn(body);
        ArgumentCaptor<MethodCallback> captor = ArgumentCaptor.forClass(MethodCallback.class);
        verify(facade, times(2)).scheduleThreadInfoRequest(anyString(), captor.capture());
        captor.getValue().onSuccess(null, threadInfoListResponse);
        verify(view, never()).setSelection(thread);
    }

}
