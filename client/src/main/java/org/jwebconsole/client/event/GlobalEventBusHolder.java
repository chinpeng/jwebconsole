package org.jwebconsole.client.event;

import com.google.web.bindery.event.shared.EventBus;

public class GlobalEventBusHolder {

    private static EventBus eventBus;


    public static EventBus getEventBus() {
        return eventBus;
    }

    public static void setEventBus(EventBus eventBus) {
        GlobalEventBusHolder.eventBus = eventBus;
    }
}
