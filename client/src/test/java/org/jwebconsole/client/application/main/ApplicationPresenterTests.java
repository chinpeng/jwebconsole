package org.jwebconsole.client.application.main;

import com.google.web.bindery.event.shared.SimpleEventBus;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.main.event.HideContentMaskEvent;
import org.jwebconsole.client.application.main.event.ShowContentMaskEvent;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.mockito.Mockito;
import com.google.web.bindery.event.shared.EventBus;

import static org.junit.Assert.assertEquals;

public class ApplicationPresenterTests extends Mockito {

    private ApplicationView view;
    private ApplicationPresenter.ApplicationProxy proxy;
    private EventBus mockEventBus;
    private ApplicationInitializer initializer;
    private EventBus eventBus;

    @Before
    public void init() {
        view = mock(ApplicationView.class);
        proxy = mock(ApplicationPresenter.ApplicationProxy.class);
        mockEventBus = mock(EventBus.class);
        initializer = mock(ApplicationInitializer.class);
        this.eventBus = new SimpleEventBus();
    }

    @Test
    public void shouldFireRevealOnStartEvent() {
        ApplicationPresenter presenter = new ApplicationPresenter(mockEventBus, view, proxy, initializer);
        presenter.onReveal();
        verify(mockEventBus).fireEvent(any(RevealOnStartEvent.class));
    }

    @Test
    @SuppressWarnings("unused")
    public void shouldInitAppOnStart() {
        ApplicationPresenter presenter = new ApplicationPresenter(mockEventBus, view, proxy, initializer);
        verify(initializer).init();
    }

    @Test
    @SuppressWarnings("unused")
    public void shouldSubscribeOnShowContentMaskEvent() {
        ApplicationPresenter presenter = new ApplicationPresenter(eventBus, view, proxy, initializer);
        eventBus.fireEvent(new ShowContentMaskEvent());
        verify(view).showContentMask();
    }

    @Test
    @SuppressWarnings("unused")
    public void shouldSubscribeOnHideContentMaskEvent() {
        ApplicationPresenter presenter = new ApplicationPresenter(eventBus, view, proxy, initializer);
        eventBus.fireEvent(new HideContentMaskEvent());
        verify(view).hideContentMask();
    }


}
