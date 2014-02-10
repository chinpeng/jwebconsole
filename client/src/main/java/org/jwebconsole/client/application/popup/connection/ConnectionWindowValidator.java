package org.jwebconsole.client.application.popup.connection;

import com.google.inject.Inject;
import org.jwebconsole.client.bundle.AppValidationId;
import org.jwebconsole.client.bundle.ValidationMessageConverter;
import org.jwebconsole.client.model.base.ValidationMessage;

import java.util.List;

public class ConnectionWindowValidator {

    private ConnectionWindowView view;
    private ValidationMessageConverter converter;
    private ViewValidator portValidator;
    private ViewValidator hostNameValidator;

    @Inject
    public ConnectionWindowValidator(ConnectionWindowView view, ValidationMessageConverter converter) {
        this.view = view;
        this.converter = converter;
        initValidators();

    }

    private void initValidators() {
        this.portValidator = new ViewValidator() {
            @Override
            public void makeInvalid(String message) {
                view.markPortInvalid(message);
            }
        };
        this.hostNameValidator = new ViewValidator() {
            @Override
            public void makeInvalid(String message) {
                view.markHostInvalid(message);
            }
        };
    }

    public void validateView(List<ValidationMessage> messages) {
        for (ValidationMessage validationMessage : messages) {
            validateHost(validationMessage);
            validatePort(validationMessage);
        }

    }

    private void validatePort(ValidationMessage validationMessage) {
        checkPortInvalid(validationMessage, AppValidationId.BIG_PORT_NUMBER);
        checkPortInvalid(validationMessage, AppValidationId.PORT_EMPTY_MESSAGE);
        checkPortInvalid(validationMessage, AppValidationId.PORT_NEGATIVE);
    }

    private void validateHost(ValidationMessage validationMessage) {
        checkHostNameInvalid(validationMessage, AppValidationId.HOST_NAME_EMPTY);
        checkHostNameInvalid(validationMessage, AppValidationId.UNABLE_TO_CONNECT);
        checkHostNameInvalid(validationMessage, AppValidationId.HOST_ALREADY_CREATED);
        checkHostNameInvalid(validationMessage, AppValidationId.HOST_ALREADY_DELETED);
    }

    private void checkHostNameInvalid(ValidationMessage validationMessage, AppValidationId validationId) {
        checkInvalid(hostNameValidator, validationMessage, validationId);
    }


    private void checkPortInvalid(ValidationMessage message, AppValidationId validationId) {
        checkInvalid(portValidator, message, validationId);
    }

    private void checkInvalid(ViewValidator validator, ValidationMessage message, AppValidationId validationId) {
        if (validationId.getId().equals(message.getId())) {
            validator.makeInvalid(converter.getMessage(validationId));
        }
    }

    private interface ViewValidator {
        void makeInvalid(String message);
    }

}
