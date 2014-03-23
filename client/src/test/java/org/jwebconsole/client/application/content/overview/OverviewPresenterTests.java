package org.jwebconsole.client.application.content.overview;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasSlots;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.content.main.ContentTabPresenter;
import org.jwebconsole.client.util.TestUtils;
import org.jwebconsole.client.util.monad.option.Option;
import org.mockito.Mockito;

public class OverviewPresenterTests extends Mockito {

    private OverviewView view;
    private OverviewPresenterFacade facade;
    private EventBus eventBus;
    private OverviewPresenter.OverviewProxy proxy;

    @Before
    public void init() {
        this.view = mock(OverviewView.class);
        this.facade = mock(OverviewPresenterFacade.class);
        this.eventBus = mock(EventBus.class);
        this.proxy = mock(OverviewPresenter.OverviewProxy.class);
    }

    @Test
    public void shouldSetUiHandlers() {
        OverviewPresenter presenter = new OverviewPresenter(eventBus, view, proxy, facade);
        verify(view).setUiHandlers(presenter);
    }

    @Test
    public void shouldBeSetInSlot() {
        OverviewPresenter presenter = new OverviewPresenter(eventBus, view, proxy, facade);
        TestUtils.verifyRevealedInSlot(presenter, eventBus, ContentTabPresenter.TYPE_SET_TAB_CONTENT);
    }

    @Test
    public void shouldInitChartOnConnectionExists() {
        OverviewPresenter presenter = new OverviewPresenter(eventBus, view, proxy, facade);
        when(facade.getConnectionId()).thenReturn(Option.create("test-id"));
        presenter.onReset();
        verify(facade).revealThreadCountChartPresenter(presenter, "test-id");
    }

    @Test
    public void shouldNotInitThreadChartOnEmptyConnectionId() {
        OverviewPresenter presenter = new OverviewPresenter(eventBus, view, proxy, facade);
        when(facade.getConnectionId()).thenReturn(Option.getEmpty());
        presenter.onReset();
        verify(facade, never()).revealThreadCountChartPresenter(any(HasSlots.class), anyString());
    }

    @Test
    public void shouldDestroyThreadChartOnHide() {
        OverviewPresenter presenter = new OverviewPresenter(eventBus, view, proxy, facade);
        when(facade.getConnectionId()).thenReturn(null);
        presenter.onHide();
        verify(facade).stopThreadChart();
    }


}
