package org.jwebconsole.client.application.toolbar.event;

import com.google.gwt.event.shared.GwtEvent;

public class HostDeletionStartedEvent extends GwtEvent<HostDeletionStartedEventHandler> {

    public static final Type<HostDeletionStartedEventHandler> TYPE = new Type<HostDeletionStartedEventHandler>();

    @Override
    public Type<HostDeletionStartedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HostDeletionStartedEventHandler handler) {
        handler.onHostDeletionStarted(this);
    }
}
