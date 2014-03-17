package org.jwebconsole.client.application.content.main.event;

import com.google.gwt.event.shared.EventHandler;

public interface TabHiddenEventHandler extends EventHandler {
    void onTabUnbind(TabHiddenEvent event);
}
