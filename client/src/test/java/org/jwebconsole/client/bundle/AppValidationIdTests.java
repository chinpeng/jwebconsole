package org.jwebconsole.client.bundle;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class AppValidationIdTests extends Mockito {

    @Test
    public void shouldReturnItemById() {
        AppValidationId result = AppValidationId.fromId(1);
        assertEquals(result, AppValidationId.HOST_NAME_EMPTY);
    }

    @Test
    public void shouldReturnUnknownMesssageForInvalidId() {
        AppValidationId result = AppValidationId.fromId(null);
        assertEquals(result, AppValidationId.MESSAGE_UNKNOWN);
    }

}
