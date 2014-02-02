package org.jwebconsole.client.application.popup.connection;

import com.google.web.bindery.event.shared.EventBus;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.popup.connection.event.HostCreatedEvent;
import org.jwebconsole.client.bundle.AppValidationId;
import org.jwebconsole.client.event.popup.RevealConnectionPopupEvent;
import org.jwebconsole.client.model.base.ValidationMessage;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConnectionWindowPresenterTests extends Mockito {

    private ConnectionWindowView view;
    private EventBus eventBus;
    private ConnectionWindowPresenter.ConnectionWindowProxy proxy;
    private ConnectionWindowPresenterFacade facade;
    private Method method;
    private HostConnectionResponse response;
    private HostConnection body;


    @Before
    public void init() {
        this.view = mock(ConnectionWindowView.class);
        this.eventBus = mock(EventBus.class);
        this.proxy = mock(ConnectionWindowPresenter.ConnectionWindowProxy.class);
        this.facade = mock(ConnectionWindowPresenterFacade.class);
        this.method = mock(Method.class);
        this.response = new HostConnectionResponse();
        this.body = mock(HostConnection.class);
        response.setBody(body);
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

    @Test
    public void shouldClearViewFieldsOnInit() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        presenter.onBind();
        verify(view).showDialog();
    }

    @Test
    public void shouldHideDialogOnCancel() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        presenter.hideDialog();
        verify(view).hideDialog();
    }

    @Test
    public void shouldNotMakeRequestIfFieldsAreInvalid() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(false);
        presenter.connectHost();
        verify(view, times(0)).showLoadingMask();
    }

    @Test
    public void shouldShowLoadingMaskWhenRequestingServer() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        presenter.connectHost();
        verify(view).showLoadingMask();
    }

    @Test
    public void shouldRequestServerWithCorrectConnectionData() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        when(view.getHostName()).thenReturn("test-host");
        presenter.connectHost();
        ArgumentCaptor<HostConnection> argumentCaptor = ArgumentCaptor.forClass(HostConnection.class);
        verify(facade).connect(argumentCaptor.capture(), any(MethodCallback.class));
        assertEquals(argumentCaptor.getValue().getName(), "test-host");
    }

    @Test
    public void shouldDisplayErrorOnFailure() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(facade).connect(any(HostConnection.class), argumentCaptor.capture());
        Throwable error = mock(Throwable.class);
        when(error.getMessage()).thenReturn("failure");
        argumentCaptor.getValue().onFailure(null, error);
        verify(facade).displayError("failure");
    }

    @Test
    public void shouldHideLoadingMaskOnFailure() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(facade).connect(any(HostConnection.class), argumentCaptor.capture());
        Throwable error = mock(Throwable.class);
        when(error.getMessage()).thenReturn("failure");
        argumentCaptor.getValue().onFailure(null, error);
        verify(view).hideMask();
    }

    @Test
    public void shouldHideLoadingMaskOnSuccess() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(facade).connect(any(HostConnection.class), argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess(method, response);
        verify(view).hideMask();
    }

    @Test
    public void shouldHideDialogOnSuccessResponse() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(facade).connect(any(HostConnection.class), argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess(method, response);
        verify(view).hideDialog();
    }
    @Test
    public void shouldFireHostCreationEventOnSuccess() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(facade).connect(any(HostConnection.class), argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess(method, response);
        verify(eventBus).fireEvent(any(HostCreatedEvent.class));
    }

    @Test
    public void shouldDisplayErrorIfResponseIsError() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        response.setError("error");
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(facade).connect(any(HostConnection.class), argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess(method, response);
        verify(facade).displayError("error");
    }

    @Test
    public void shouldProvideValidationMessageToViewIfValidationFailed() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(facade.getMessage(any(AppValidationId.class))).thenReturn("invalid");
        when(view.isFieldsValid()).thenReturn(true);
        response.setMessages(createValidations());
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(facade).connect(any(HostConnection.class), argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess(method, response);
        verify(view).markHostInvalid("invalid");
    }

    private List<ValidationMessage> createValidations() {
        List<ValidationMessage> result = new ArrayList<ValidationMessage>();
        result.add(new ValidationMessage(1, "test"));
        return result;
    }


}
