package org.jwebconsole.client.application;

import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.mockito.Mockito;
import com.google.web.bindery.event.shared.EventBus;

public class ApplicationPresenterTests extends Mockito {

    private ApplicationView view;
    private ApplicationPresenter.ApplicationProxy proxy;
    private EventBus eventBus;

    @Before
    public void init() {
        view = mock(ApplicationView.class);
        proxy = mock(ApplicationPresenter.ApplicationProxy.class);
        eventBus = mock(EventBus.class);
    }

    @Test
    public void shouldFireRevealOnStartEvent() {
        ApplicationPresenter presenter = new ApplicationPresenter(eventBus, view, proxy);
        presenter.onReveal();
        verify(eventBus).fireEvent(any(RevealOnStartEvent.class));
    }

}
