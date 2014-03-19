package org.jwebconsole.client.application.content.main;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.content.main.event.TabHiddenEvent;
import org.jwebconsole.client.application.content.main.event.TabRevealedEvent;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.util.TestUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class ContentTabPresenterTests extends Mockito {

    private ContentTabPresenter.ContentTabProxy proxy;
    private ContentTabView view;
    private PlaceManager placeManager;
    private EventBus mockEventBus;

    @Before
    public void init() {
        this.mockEventBus = mock(EventBus.class);
        this.proxy = mock(ContentTabPresenter.ContentTabProxy.class);
        this.view = mock(ContentTabView.class);
        this.placeManager = mock(PlaceManager.class);
    }

    @Test
    public void shouldSetUiHandlers() {
        ContentTabPresenter presenter = new ContentTabPresenter(mockEventBus, view, proxy, placeManager);
        verify(view).setUiHandlers(presenter);
    }

    @Test
    public void shouldRedirectWithSameParameters() {
        PlaceRequest request = new PlaceRequest.Builder().nameToken("source").with("testKey", "testValue").build();
        when(placeManager.getCurrentPlaceRequest()).thenReturn(request);
        ContentTabPresenter presenter = new ContentTabPresenter(mockEventBus, view, proxy, placeManager);
        presenter.redirect("dest");
        ArgumentCaptor<PlaceRequest> captor = ArgumentCaptor.forClass(PlaceRequest.class);
        verify(placeManager).revealPlace(captor.capture());
        PlaceRequest dest = captor.getValue();
        assertEquals(dest.getNameToken(), "dest");
        assertEquals(dest.getParameter("testKey", null), "testValue");
    }

    @Test
    public void shouldFireTabRevealedEventOnReset() {
        ContentTabPresenter presenter = new ContentTabPresenter(mockEventBus, view, proxy, placeManager);
        presenter.onReset();
        TestUtils.verifyEventBusFired(mockEventBus, TabRevealedEvent.class);
    }

    @Test
    public void shouldFireTabHiddenEventOnHide() {
        ContentTabPresenter presenter = new ContentTabPresenter(mockEventBus, view, proxy, placeManager);
        presenter.onHide();
        TestUtils.verifyEventBusFired(mockEventBus, TabHiddenEvent.class);
    }

    @Test
    public void shouldBeSetInSlot() {
        ContentTabPresenter presenter = new ContentTabPresenter(mockEventBus, view, proxy, placeManager);
        TestUtils.verifyRevealedInSlot(presenter, mockEventBus, ApplicationPresenter.SLOT_CONTENT_PANEL);
    }

}
