package org.jwebconsole.client.application.left;

import com.google.web.bindery.event.shared.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.left.event.HostSelectedEvent;
import org.jwebconsole.client.model.host.HostConnection;
import org.mockito.Mockito;

public class AvailableHostsPresenterTests extends Mockito {

    private AvailableHostsView view;
    private AvailableHostsPresenterFacade facade;
    private EventBus eventBus;
    private AvailableHostsPresenter.AvailableHostsProxy proxy;

    @Before
    public void init() {
        this.view = mock(AvailableHostsView.class);
        this.facade = mock(AvailableHostsPresenterFacade.class);
        this.eventBus = mock(EventBus.class);
        this.proxy = mock(AvailableHostsPresenter.AvailableHostsProxy.class);
    }

    @Test
    public void shouldFireClickedEventWhenTreeItemWasSelected() {
        AvailableHostsPresenter presenter = new AvailableHostsPresenter(eventBus, view, proxy, facade);
        presenter.onTreeItemSelected(any(HostConnection.class));
        verify(eventBus).fireEvent(any(HostSelectedEvent.class));
    }


}
