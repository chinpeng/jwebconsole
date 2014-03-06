package org.jwebconsole.client.application.content.main;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.content.home.HomePresenter;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.place.NameTokens;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class ContentTabPresenterTests extends Mockito {

    private PlaceManager placeManager;
    private ContentTabView view;
    private ContentTabPresenter.ContentTabProxy proxy;
    private EventBus eventBus;
    private PlaceRequest request;

    @Before
    public void init() {
        this.view = mock(ContentTabView.class);
        this.proxy = mock(ContentTabPresenter.ContentTabProxy.class);
        this.eventBus = mock(EventBus.class);
        this.placeManager = mock(PlaceManager.class);
        this.request = mock(PlaceRequest.class);
        when(placeManager.getCurrentPlaceRequest()).thenReturn(request);
    }

    @Test
    public void shouldSetUiHandlers() {
        ContentTabPresenter presenter = new ContentTabPresenter(eventBus, view, proxy, placeManager);
        verify(view).setUiHandlers(presenter);
    }

    @Test
    public void shouldBeSetInContentSlot() {
        ContentTabPresenter presenter = new ContentTabPresenter(eventBus, view, proxy, placeManager);
        presenter.forceReveal();
        ArgumentCaptor<RevealContentEvent> captor = ArgumentCaptor.forClass(RevealContentEvent.class);
        verify(eventBus).fireEventFromSource(captor.capture(), anyObject());
        assertEquals(captor.getValue().getAssociatedType(), ApplicationPresenter.SLOT_CONTENT_PANEL);
    }

    @Test
    public void shouldSetAvailableTabsToView() {
        ContentTabPresenter presenter = new ContentTabPresenter(eventBus, view, proxy, placeManager);
        presenter.onBind();
        verify(view).setMemoryNameToken(NameTokens.memory);
        verify(view).setThreadsNameToken(NameTokens.thread);
    }

    @Test
    public void shouldApplyActiveTabFromRequest() {
        ContentTabPresenter presenter = new ContentTabPresenter(eventBus, view, proxy, placeManager);
        when(request.getNameToken()).thenReturn("test-token");
        presenter.onReset();
        verify(view).applySelectionByNameToken("test-token");
    }

}
