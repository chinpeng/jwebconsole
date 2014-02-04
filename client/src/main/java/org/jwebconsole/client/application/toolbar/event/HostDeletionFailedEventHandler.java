package org.jwebconsole.client.application.toolbar.event;

import com.google.gwt.event.shared.EventHandler;

public interface HostDeletionFailedEventHandler extends EventHandler {

    public void onDeletionFailed(HostDeletionFailedEvent event);

}
