package org.jwebconsole.client.bundle;

import com.google.inject.Inject;
import org.jwebconsole.client.bundle.messages.ErrorMessages;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessageConverter {

    private ErrorMessages errorMessages;
    private Map<AppErrorId, String> messagesMap = new HashMap<AppErrorId, String>();

    @Inject
    public ErrorMessageConverter(ErrorMessages errorMessages) {
        this.errorMessages = errorMessages;
        init();
    }

    private void init() {
        messagesMap.put(AppErrorId.UNKNOWN_ERROR, errorMessages.unknownError());
        messagesMap.put(AppErrorId.HOST_NOT_FOUND, errorMessages.hostNotFound());
        messagesMap.put(AppErrorId.DB_FAILURE, errorMessages.dbFailure());
    }

    public String getMessage(AppErrorId appErrorId) {
        return messagesMap.get(appErrorId);
    }

}
