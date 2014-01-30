package org.jwebconsole.client.application.popup.connection;

import com.google.web.bindery.event.shared.EventBus;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.event.popup.RevealConnectionPopupEvent;
import org.mockito.Mockito;

public class ConnectionWindowPresenterTests extends Mockito {

    private ConnectionWindowView view;
    private EventBus eventBus;
    private ConnectionWindowPresenter.ConnectionWindowProxy proxy;
    private ConnectionWindowPresenterFacade facade;
    private TextField hostNameField;
    private NumberField<Integer> portField;
    private TextField loginField;
    private PasswordField passwordField;
    private Window window;


    @Before
    public void init() {
        this.view = mock(ConnectionWindowView.class);
        this.eventBus = mock(EventBus.class);
        this.proxy = mock(ConnectionWindowPresenter.ConnectionWindowProxy.class);
        this.facade = mock(ConnectionWindowPresenterFacade.class);
        initFields();
    }

    @SuppressWarnings("unchecked")
    private void initFields() {
        this.hostNameField = mock(TextField.class);
        this.portField = mock(NumberField.class);
        this.loginField = mock(TextField.class);
        this.passwordField = mock(PasswordField.class);
        this.window = mock(Window.class);
        when(view.getHostName()).thenReturn(hostNameField);
        when(view.getPort()).thenReturn(portField);
        when(view.getLogin()).thenReturn(loginField);
        when(view.getPassword()).thenReturn(passwordField);
    }


    @Test
    public void shouldSetUiHandlers() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        verify(view).setUiHandlers(presenter);
    }

    @Test
    public void shouldForceRevealOnEvent() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        ConnectionWindowPresenter presenterSpy = spy(presenter);
        presenterSpy.onRevealEvent(new RevealConnectionPopupEvent());
        verify(presenterSpy).forceReveal();
    }

    @Test
    public void shouldInitOnBind() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        presenter.onBind();
        verify(view).showDialog();
    }

}
