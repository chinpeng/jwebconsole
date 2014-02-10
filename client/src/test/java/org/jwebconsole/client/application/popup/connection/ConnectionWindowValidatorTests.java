package org.jwebconsole.client.application.popup.connection;

import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.bundle.AppValidationId;
import org.jwebconsole.client.bundle.ValidationMessageConverter;
import org.jwebconsole.client.model.base.ValidationMessage;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class ConnectionWindowValidatorTests extends Mockito {

    private ConnectionWindowView view;
    private ValidationMessageConverter converter;

    @Before
    public void init() {
        this.view = mock(ConnectionWindowView.class);
        this.converter = mock(ValidationMessageConverter.class);
    }

    @Test
    public void shouldShowBigPortNumberValidation() {
        ConnectionWindowValidator validator = new ConnectionWindowValidator(view, converter);
        validator.validateView(createMessages(AppValidationId.BIG_PORT_NUMBER));
        verify(view).markPortInvalid(anyString());
    }

    @Test
    public void shouldShowEmptyPortValidation() {
        ConnectionWindowValidator validator = new ConnectionWindowValidator(view, converter);
        validator.validateView(createMessages(AppValidationId.PORT_EMPTY_MESSAGE));
        verify(view).markPortInvalid(anyString());
    }

    @Test
    public void shouldShowNegativePortValidation() {
        ConnectionWindowValidator validator = new ConnectionWindowValidator(view, converter);
        validator.validateView(createMessages(AppValidationId.PORT_NEGATIVE));
        verify(view).markPortInvalid(anyString());
    }

    @Test
    public void shouldShowHostNameValidation() {
        ConnectionWindowValidator validator = new ConnectionWindowValidator(view, converter);
        validator.validateView(createMessages(AppValidationId.HOST_NAME_EMPTY));
        verify(view).markHostInvalid(anyString());
    }

    @Test
    public void shouldShowHostAlreadyCreatedMessage() {
        ConnectionWindowValidator validator = new ConnectionWindowValidator(view, converter);
        validator.validateView(createMessages(AppValidationId.HOST_ALREADY_CREATED));
        verify(view).markHostInvalid(anyString());
    }

    @Test
    public void shouldShowHostDeletedMessage() {
        ConnectionWindowValidator validator = new ConnectionWindowValidator(view, converter);
        validator.validateView(createMessages(AppValidationId.HOST_ALREADY_DELETED));
        verify(view).markHostInvalid(anyString());
    }

    @Test
    public void shouldShowUnableToConnectMessage() {
        ConnectionWindowValidator validator = new ConnectionWindowValidator(view, converter);
        validator.validateView(createMessages(AppValidationId.UNABLE_TO_CONNECT));
        verify(view).markHostInvalid(anyString());
    }

    private List<ValidationMessage> createMessages(AppValidationId validationId) {
        ArrayList<ValidationMessage> result = new ArrayList<ValidationMessage>();
        result.add(new ValidationMessage(validationId.getId(), "Test"));
        return result;
    }

}
