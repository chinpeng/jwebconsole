package org.jwebconsole.client.bundle;

import com.google.inject.Inject;
import org.jwebconsole.client.bundle.messages.ValidationMessages;

import java.util.HashMap;
import java.util.Map;

public class ValidationMessageConverter {


    private ValidationMessages validationMessages;
    private Map<AppValidationId, String> messagesMap = new HashMap<AppValidationId, String>();

    @Inject
    public ValidationMessageConverter(ValidationMessages validationMessages) {
        this.validationMessages = validationMessages;
        init();
    }

    private void init() {
        messagesMap.put(AppValidationId.MESSAGE_UNKNOWN, validationMessages.unknownMessage());
        messagesMap.put(AppValidationId.HOST_NAME_EMPTY, validationMessages.hostnameIsEmpty());
        messagesMap.put(AppValidationId.PORT_EMPTY_MESSAGE, validationMessages.portIsEmpty());
        messagesMap.put(AppValidationId.PORT_NEGATIVE, validationMessages.portNumberNegative());
        messagesMap.put(AppValidationId.BIG_PORT_NUMBER, validationMessages.bigPortNumber());
        messagesMap.put(AppValidationId.HOST_ALREADY_CREATED, validationMessages.hostAlreadyCreated());
        messagesMap.put(AppValidationId.HOST_ALREADY_DELETED, validationMessages.hostAlreadyDeleted());
        messagesMap.put(AppValidationId.UNABLE_TO_CONNECT, validationMessages.unableToConnectMessage());
    }

    public String getMessage(AppValidationId appValidationId) {
        return messagesMap.get(appValidationId);
    }

}
