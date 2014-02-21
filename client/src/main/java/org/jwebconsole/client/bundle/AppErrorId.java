package org.jwebconsole.client.bundle;

public enum AppErrorId {

    UNKNOWN_ERROR(1),
    DB_FAILURE(2),
    HOST_NOT_FOUND(3);

    private Integer id;

    private AppErrorId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static AppErrorId fromId(Integer id) {
        for (AppErrorId item : values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return UNKNOWN_ERROR;
    }

}
