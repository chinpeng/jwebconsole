package org.jwebconsole.client.model.base;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class BaseResponseTests extends Mockito {

    @Test
    public void shouldBeValidIfResponseMessagesAreNull() {
        BaseResponse<Object> response = new BaseResponse<Object>();
        assertTrue(response.isValid());
    }

    @Test
    public void shouldBeValidIfResponseMessagesAreEmpty() {
        BaseResponse<Object> response = new BaseResponse<Object>();
        response.setMessages(new ArrayList<ValidationMessage>());
        assertTrue(response.isValid());
    }

    @Test
    public void shouldBeInvalidIfContainsValidationMessages() {
        BaseResponse<Object> response = new BaseResponse<Object>();
        List<ValidationMessage> messages = new ArrayList<ValidationMessage>();
        messages.add(new ValidationMessage(1, "Test"));
        response.setMessages(messages);
        assertFalse(response.isValid());

    }

    @Test
    public void shouldBeSuccessIfErrorIsNull() {
        BaseResponse<Object> response = new BaseResponse<Object>();
        assertTrue(response.isSuccess());
    }

    @Test
    public void shouldBeSuccessIfErrorIsEmpty() {
        BaseResponse<Object> response = new BaseResponse<Object>();
        response.setError("");
        assertTrue(response.isSuccess());
    }

    @Test
    public void shouldBeNotSuccessIfErrorIsFull() {
        BaseResponse<Object> response = new BaseResponse<Object>();
        response.setError("error");
        assertFalse(response.isSuccess());
    }

}
