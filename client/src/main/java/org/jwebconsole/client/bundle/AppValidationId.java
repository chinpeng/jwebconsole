package org.jwebconsole.client.bundle;

public enum AppValidationId {

    MESSAGE_UNKNOWN(0),
    HOST_NAME_EMPTY(1),
    PORT_EMPTY_MESSAGE(2),
    PORT_NEGATIVE(3),
    BIG_PORT_NUMBER(4),
    HOST_ALREADY_CREATED(5),
    HOST_ALREADY_DELETED(6),
    UNABLE_TO_CONNECT(7);



    private Integer id;

    private AppValidationId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static AppValidationId fromId(Integer id) {
        for (AppValidationId item : values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return MESSAGE_UNKNOWN;
    }
}
