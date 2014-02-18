package org.jwebconsole.client.application;

import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.mockito.Mockito;
import com.google.web.bindery.event.shared.EventBus;

import static org.junit.Assert.assertEquals;

public class ApplicationPresenterTests extends Mockito {

    private ApplicationView view;
    private ApplicationPresenter.ApplicationProxy proxy;
    private EventBus eventBus;
    private ApplicationInitializer initializer;

    @Before
    public void init() {
        view = mock(ApplicationView.class);
        proxy = mock(ApplicationPresenter.ApplicationProxy.class);
        eventBus = mock(EventBus.class);
        initializer = mock(ApplicationInitializer.class);
    }

    @Test
    public void shouldFireRevealOnStartEvent() {
        ApplicationPresenter presenter = new ApplicationPresenter(eventBus, view, proxy, initializer);
        presenter.onReveal();
        verify(eventBus).fireEvent(any(RevealOnStartEvent.class));
    }

    @Test
    @SuppressWarnings("unused")
    public void shouldInitAppOnStart() {
        ApplicationPresenter presenter = new ApplicationPresenter(eventBus, view, proxy, initializer);
        verify(initializer).init();
    }


}
