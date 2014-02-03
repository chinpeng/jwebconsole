package org.jwebconsole.client.application.toolbar;

import com.google.web.bindery.event.shared.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.left.event.HostSelectedEvent;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.jwebconsole.client.model.host.HostConnection;
import org.mockito.Mockito;

public class ToolbarPresenterTests extends Mockito {

    private ToolbarView view;
    private EventBus eventBus;
    private ToolbarPresenter.ToolbarProxy proxy;
    private ToolbarPresenterFacade facade;

    @Before
    public void init() {
        this.view = mock(ToolbarView.class);
        this.eventBus = mock(EventBus.class);
        this.proxy = mock(ToolbarPresenter.ToolbarProxy.class);
        this.facade = mock(ToolbarPresenterFacade.class);
    }

    @Test
    public void shouldRevealSelfOnStart() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        ToolbarPresenter presenterSpy = spy(presenter);
        presenterSpy.onApplicationStart(new RevealOnStartEvent());
        verify(presenterSpy).forceReveal();
    }

    @Test
    public void shouldSetUiHandlers() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        verify(view).setUiHandlers(presenter);
    }

    @Test
    public void shouldOpenConnectionWindow() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        presenter.openConnectionWindow();
        verify(eventBus).fireEvent(any(RevealOnStartEvent.class));
    }

    @Test
    public void shouldDisableEditButtonOnStart() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        presenter.onBind();
        verify(view).disableEditButtons();
    }

    @Test
    public void shouldSubscribeSelfOnHostSelectedEvent() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        verify(eventBus).addHandler(HostSelectedEvent.TYPE, presenter);
    }

    @Test
    public void shouldEnableEditButtonsOnHostSelectedEvent() {
        ToolbarPresenter presenter = new ToolbarPresenter(eventBus, view, proxy, facade);
        presenter.onHostSelected(new HostSelectedEvent(any(HostConnection.class)));
        verify(view).enableEditButtons();
    }

}
