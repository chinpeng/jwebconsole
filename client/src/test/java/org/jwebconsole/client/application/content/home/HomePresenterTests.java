package org.jwebconsole.client.application.content.home;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.event.popup.RevealAddConnectionPopupEvent;
import org.jwebconsole.client.util.TestUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class HomePresenterTests extends Mockito {

    private HomeView view;
    private HomePresenter.HomeProxy proxy;
    private EventBus eventBus;

    @Before
    public void init() {
        this.view = mock(HomeView.class);
        this.proxy = mock(HomePresenter.HomeProxy.class);
        this.eventBus = mock(EventBus.class);
    }

    @Test
    public void shouldSetUiHandlers() {
        HomePresenter presenter = new HomePresenter(eventBus, view, proxy);
        verify(view).setUiHandlers(presenter);
    }


    @Test
    public void shouldFireRevealConnectionPopupEventOnButtonClick() {
        HomePresenter presenter = new HomePresenter(eventBus, view, proxy);
        ArgumentCaptor<GwtEvent> captor = ArgumentCaptor.forClass(GwtEvent.class);
        presenter.onCreateConnectionButtonClicked();
        verify(eventBus).fireEvent(captor.capture());
        assertEquals(captor.getValue().getClass(), RevealAddConnectionPopupEvent.class);
    }

    @Test
    public void shouldBeSetInContentSlot() {
        HomePresenter presenter = new HomePresenter(eventBus, view, proxy);
        TestUtils.verifyRevealedInSlot(presenter, eventBus, ApplicationPresenter.SLOT_CONTENT_PANEL);
    }

}
