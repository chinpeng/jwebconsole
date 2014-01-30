package org.jwebconsole.client.bundle;

import com.google.inject.Inject;
import org.jwebconsole.client.bundle.messages.Messages;

import java.util.HashMap;
import java.util.Map;

public class ValidationMessages {

    public static final Integer HOST_NAME_EMPTY_ID = 1;
    public static final Integer PORT_EMPTY_ID = 2;
    public static final Integer PORT_MUST_BE_POSITIVE_ID = 3;
    public static final Integer BIG_PORT_NUMBER_ID = 4;
    public static final Integer HOST_CREATED_ID = 5;
    public static final Integer HOST_DELETED_ID = 6;

    private final Messages msg;
    Map<Integer, String> messages = new HashMap<Integer, String>();

    @Inject
    public ValidationMessages(Messages msg) {
        this.msg = msg;
        init();
    }

    private void init() {
        messages.put(HOST_NAME_EMPTY_ID, msg.hostNameIsEmptyMessage());
        messages.put(PORT_EMPTY_ID, msg.portIsEmptyMessage());
        messages.put(PORT_MUST_BE_POSITIVE_ID, msg.portMustBePositive());
        messages.put(BIG_PORT_NUMBER_ID, msg.bigNumberForPort());
        messages.put(HOST_CREATED_ID, msg.hostAlreadyCreated());
        messages.put(HOST_DELETED_ID, msg.hostAlreadyDeleted());
    }

    public String getById(Integer id) {
        return messages.get(id);
    }


}
