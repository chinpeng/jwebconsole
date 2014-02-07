package org.jwebconsole.client.application.popup.connection;

import com.google.inject.Inject;
import org.jwebconsole.client.bundle.AppValidationId;
import org.jwebconsole.client.bundle.ValidationMessageConverter;
import org.jwebconsole.client.model.base.ValidationMessage;

import java.util.List;

public class ConnectionWindowViewValidator {

    private ConnectionWindowView view;
    private ValidationMessageConverter converter;

    @Inject
    public ConnectionWindowViewValidator(ConnectionWindowView view, ValidationMessageConverter converter) {
        this.view = view;
        this.converter = converter;
    }

    public void validateView(List<ValidationMessage> messages) {
        for (ValidationMessage validationMessage : messages) {
            Integer id = validationMessage.getId();
            if (id.equals(AppValidationId.BIG_PORT_NUMBER.getId())) {
                view.markPortInvalid(getMessage(AppValidationId.BIG_PORT_NUMBER));
            }
            if (id.equals(AppValidationId.HOST_ALREADY_CREATED.getId())) {
                view.markHostInvalid(getMessage(AppValidationId.HOST_ALREADY_CREATED));
            }
            if (id.equals(AppValidationId.HOST_ALREADY_DELETED.getId())) {
                view.markHostInvalid(getMessage(AppValidationId.HOST_ALREADY_DELETED));
            }
            if (id.equals(AppValidationId.PORT_EMPTY_MESSAGE.getId())) {
                view.markPortInvalid(getMessage(AppValidationId.PORT_EMPTY_MESSAGE));
            }
            if (id.equals(AppValidationId.HOST_NAME_EMPTY.getId())) {
                view.markHostInvalid(getMessage(AppValidationId.HOST_NAME_EMPTY));
            }
            if (id.equals(AppValidationId.PORT_NEGATIVE.getId())) {
                view.markPortInvalid(getMessage(AppValidationId.PORT_NEGATIVE));
            }
            if (id.equals(AppValidationId.UNABLE_TO_CONNECT.getId())) {
                view.markHostInvalid(getMessage(AppValidationId.UNABLE_TO_CONNECT));
            }
        }
    }

    public String getMessage(AppValidationId appValidationId) {
        return converter.getMessage(appValidationId);
    }


}
