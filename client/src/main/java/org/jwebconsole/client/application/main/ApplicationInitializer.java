package org.jwebconsole.client.application.main;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import org.fusesource.restygwt.client.Defaults;
import org.jwebconsole.client.event.GlobalEventBusHolder;

public class ApplicationInitializer {

    private EventBus eventBus;

    @Inject
    public ApplicationInitializer(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void init() {
        GlobalEventBusHolder.setEventBus(eventBus);
        Defaults.setDateFormat("yyyy-MM-dd HH:mm:ss");
    }

}
