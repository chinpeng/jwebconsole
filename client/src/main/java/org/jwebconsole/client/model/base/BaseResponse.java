package org.jwebconsole.client.model.base;

import java.util.List;

public class BaseResponse<T> {

    T body;

    List<ValidationMessage> messages;

    String error;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public List<ValidationMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ValidationMessage> messages) {
        this.messages = messages;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isValid() {
        return messages == null || messages.isEmpty();
    }

    public boolean isError() {
        return error != null && !error.isEmpty();
    }

    public boolean isSuccess() {
        return isValid() && !isError();
    }
}
