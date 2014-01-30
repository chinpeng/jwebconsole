package org.jwebconsole.client.bundle;

public enum AppValidationId {

    HOST_NAME_EMPTY(1),
    PORT_EMPTY_MESSAGE(3),
    PORT_NEGATIVE(3),
    BIG_PORT_NUMBER(4),
    HOST_ALREADY_CREATED(5),
    HOST_ALREADY_DELETED(6);



    private Integer id;

    private AppValidationId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
