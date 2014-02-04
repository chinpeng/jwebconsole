package org.jwebconsole.client.common;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.sencha.gxt.widget.core.client.info.Info;
import org.jwebconsole.client.event.info.PrintInfoEvent;
import org.jwebconsole.client.event.info.PrintInfoEventHandler;

public class InfoHolder implements PrintInfoEventHandler {

    private EventBus eventBus;

    @Inject
    public InfoHolder(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.addHandler(PrintInfoEvent.TYPE, this);
    }

    public void printInfo(String title, String text) {
        Info.display(title, text);
    }

    public void printInfo(String text) {
        Info.display("", text);
    }

    @Override
    public void onEventReceived(PrintInfoEvent event) {
        printInfo(event.getTitle(), event.getText());
    }
}
