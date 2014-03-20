package org.jwebconsole.client.application.left;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.content.main.event.TabHiddenEvent;
import org.jwebconsole.client.application.content.main.event.TabRevealedEvent;
import org.jwebconsole.client.service.ServiceFactory;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AvailableHostsPresenterFacadeTests extends Mockito {

    private ServiceFactory serviceFactory;
    private PlaceManager placeManager;
    private EventBus eventBus;

    @Before
    public void init() {
        this.serviceFactory = mock(ServiceFactory.class);
        this.placeManager = mock(PlaceManager.class);
        this.eventBus = mock(EventBus.class);
    }

    @Test
    public void shouldRegisterOnTabRevealedEvent() {
        AvailableHostsPresenterFacade facade = new AvailableHostsPresenterFacade(serviceFactory, placeManager, eventBus);
        verify(eventBus).addHandler(TabRevealedEvent.TYPE, facade);
    }

    @Test
    public void shouldRegisterOnTabHideEvent() {
        AvailableHostsPresenterFacade facade = new AvailableHostsPresenterFacade(serviceFactory, placeManager, eventBus);
        verify(eventBus).addHandler(TabHiddenEvent.TYPE, facade);
    }

    @Test
    public void shouldAnswerWithTabHiddenOnTabHide() {
        AvailableHostsPresenterFacade facade = new AvailableHostsPresenterFacade(serviceFactory, placeManager, eventBus);
        facade.onTabUnbind(new TabHiddenEvent());
        assertFalse(facade.isTabNameToken());
    }

    @Test
    public void shouldAnswerWithDisplayed() {
        AvailableHostsPresenterFacade facade = new AvailableHostsPresenterFacade(serviceFactory, placeManager, eventBus);
        facade.onTabRevealed(new TabRevealedEvent());
        assertTrue(facade.isTabNameToken());
    }

}
