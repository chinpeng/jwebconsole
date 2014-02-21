package org.jwebconsole.client.model.base;

public class ErrorMessage {

    private Integer id;
    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(Integer id, String message) {
        this.id = id;
        this.message = message;
    }

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
