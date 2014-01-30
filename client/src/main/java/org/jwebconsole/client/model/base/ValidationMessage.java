package org.jwebconsole.client.model.base;

public class ValidationMessage {

    public ValidationMessage() {
    }

    public ValidationMessage(Integer id, String message) {
        this.id = id;
        this.message = message;
    }

    private Integer id;

    private String message;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
