package org.jwebconsole.client.application.content.memory;

import com.google.web.bindery.event.shared.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.content.main.ContentTabPresenter;
import org.jwebconsole.client.util.TestUtils;
import org.mockito.Mockito;

public class MemoryPresenterTests extends Mockito {

    private MemoryView view;
    private EventBus eventBus;
    private MemoryPresenterFacade facade;
    private MemoryPresenter.MemoryProxy proxy;

    @Before
    public void init() {
        this.view = mock(MemoryView.class);
        this.eventBus = mock(EventBus.class);
        this.facade = mock(MemoryPresenterFacade.class);
        this.proxy = mock(MemoryPresenter.MemoryProxy.class);
    }

    @Test
    public void shouldSetUiHandlers() {
        MemoryPresenter presenter = new MemoryPresenter(eventBus, view, proxy, facade);
        verify(view).setUiHandlers(presenter);
    }

    @Test
    public void shouldBeSetInSlot() {
        MemoryPresenter presenter = new MemoryPresenter(eventBus, view, proxy, facade);
        TestUtils.verifyRevealedInSlot(presenter, eventBus, ContentTabPresenter.TYPE_SET_TAB_CONTENT);
    }

}
