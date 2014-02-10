package org.jwebconsole.client.application.popup.connection;

import com.google.web.bindery.event.shared.EventBus;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.application.popup.connection.state.CreateConnectionController;
import org.jwebconsole.client.event.popup.RevealAddConnectionPopupEvent;
import org.jwebconsole.client.model.base.ValidationMessage;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class ConnectionWindowPresenterTests extends Mockito {

    private ConnectionWindowView view;
    private EventBus eventBus;
    private ConnectionWindowPresenter.ConnectionWindowProxy proxy;
    private ConnectionWindowPresenterFacade facade;
    private Method method;
    private HostConnectionResponse response;
    private CreateConnectionController createController;


    @Before
    public void init() {
        this.view = mock(ConnectionWindowView.class);
        this.eventBus = mock(EventBus.class);
        this.proxy = mock(ConnectionWindowPresenter.ConnectionWindowProxy.class);
        this.facade = mock(ConnectionWindowPresenterFacade.class);
        this.method = mock(Method.class);
        this.response = new HostConnectionResponse();
        HostConnection body = mock(HostConnection.class);
        this.createController = mock(CreateConnectionController.class);
        when(facade.getController()).thenReturn(createController);
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
        presenterSpy.onRevealAddEvent(new RevealAddConnectionPopupEvent());
        verify(presenterSpy).forceReveal();
    }

    @Test
    public void shouldInitOnEvent() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        presenter.onRevealAddEvent(new RevealAddConnectionPopupEvent());
        verify(view).showDialog();
    }

    @Test
    public void shouldInitViewFieldsOnInit() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        presenter.onRevealAddEvent(new RevealAddConnectionPopupEvent());
        verify(createController).initViewOnAppear();
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
    @SuppressWarnings("unchecked")
    public void shouldDisplayErrorOnFailure() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(createController).makeRequest(argumentCaptor.capture());
        Throwable error = mock(Throwable.class);
        when(error.getMessage()).thenReturn("failure");
        argumentCaptor.getValue().onFailure(null, error);
        verify(facade).displayError("failure");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldHideLoadingMaskOnFailure() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(createController).makeRequest(argumentCaptor.capture());
        Throwable error = mock(Throwable.class);
        when(error.getMessage()).thenReturn("failure");
        argumentCaptor.getValue().onFailure(null, error);
        verify(view).hideMask();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldHideLoadingMaskOnSuccess() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(createController).makeRequest(argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess(method, response);
        verify(view).hideMask();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldHideDialogOnSuccessResponse() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(createController).makeRequest(argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess(method, response);
        verify(view).hideDialog();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFireHostChangeEventOnSuccess() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(createController).makeRequest(argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess(method, response);
        verify(createController).fireChangeEvent(any(HostConnection.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldDisplayErrorIfResponseIsError() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        response.setError("error");
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(createController).makeRequest(argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess(method, response);
        verify(facade).displayError("error");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProvideValidationMessageToViewIfValidationFailed() {
        ConnectionWindowPresenter presenter = new ConnectionWindowPresenter(eventBus, view, proxy, facade);
        when(view.isFieldsValid()).thenReturn(true);
        List<ValidationMessage> validations = createValidations();
        response.setMessages(validations);
        ArgumentCaptor<MethodCallback> argumentCaptor = ArgumentCaptor.forClass(MethodCallback.class);
        presenter.connectHost();
        verify(createController).makeRequest(argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess(method, response);
        verify(facade).validate(validations);
    }

    private List<ValidationMessage> createValidations() {
        List<ValidationMessage> result = new ArrayList<ValidationMessage>();
        result.add(new ValidationMessage(1, "test"));
        return result;
    }


}
