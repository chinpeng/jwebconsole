package org.jwebconsole.client.model.base;

import java.util.List;

public class BaseResponse<T> {

    T body;

    List<ValidationMessage> messages;

    ErrorMessage error;

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

    public ErrorMessage getError() {
        return error;
    }

    public void setError(ErrorMessage error) {
        this.error = error;
    }

    public boolean isValid() {
        return messages == null || messages.isEmpty();
    }

    public boolean isError() {
        return error != null;
    }

    public boolean isSuccess() {
        return isValid() && !isError();
    }
}
