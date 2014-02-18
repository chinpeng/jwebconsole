package org.jwebconsole.client.application.content.thread;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.place.AppParams;
import org.mockito.Mockito;

public class ThreadContentPresenterTests extends Mockito {

    private ThreadContentView view;
    private ThreadContentPresenter.ThreadContentProxy proxy;
    private EventBus eventBus;
    private ThreadContentPresenterFacade facade;
    private PlaceRequest request;

    @Before
    public void init() {
        this.view = mock(ThreadContentView.class);
        this.proxy = mock(ThreadContentPresenter.ThreadContentProxy.class);
        this.eventBus = mock(EventBus.class);
        this.facade = mock(ThreadContentPresenterFacade.class);
        this.request = mock(PlaceRequest.class);
    }

    @Test
    public void shouldSetUiHandlers() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        verify(view).setUiHandlers(presenter);
    }

    @Test
    public void shouldPrintErrorIfHostIdIsEmpty() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        when(request.getParameter(AppParams.HOST_ID, "")).thenReturn("");
        presenter.prepareFromRequest(request);
        verify(facade).printEmptyHostIdMessage();
    }

    @Test
    public void shouldPrintErrorIfHostIdContainsOnlySpaces() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        when(request.getParameter(AppParams.HOST_ID, "")).thenReturn("  ");
        presenter.prepareFromRequest(request);
        verify(facade).printEmptyHostIdMessage();
    }

    @Test
    public void shouldRedirectOnErrorPlace() {
        ThreadContentPresenter presenter = new ThreadContentPresenter(eventBus, view, proxy, facade);
        when(request.getParameter(AppParams.HOST_ID, "")).thenReturn("");
        presenter.prepareFromRequest(request);
        verify(facade).redirectToErrorPlace();
    }

    @Test
    public void shouldMakeRequestToServerIfHostExists() {

    }

}
