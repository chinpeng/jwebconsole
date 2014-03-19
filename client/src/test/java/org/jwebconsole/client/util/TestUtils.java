package org.jwebconsole.client.util;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class TestUtils extends Mockito {

    public static <T extends GwtEvent> void verifyEventBusFired(EventBus eventBus, Class<T> eventType) {
        ArgumentCaptor<T> captor = ArgumentCaptor.forClass(eventType);
        verify(eventBus).fireEvent(captor.capture());
        assertEquals(eventType, captor.getValue().getClass());
    }

    public static void verifyRevealedInSlot(Presenter presenter, EventBus eventBus, Object slot) {
        presenter.forceReveal();
        ArgumentCaptor<RevealContentEvent> captor = ArgumentCaptor.forClass(RevealContentEvent.class);
        verify(eventBus).fireEventFromSource(captor.capture(), anyObject());
        assertEquals(captor.getValue().getAssociatedType(), slot);
    }

}
