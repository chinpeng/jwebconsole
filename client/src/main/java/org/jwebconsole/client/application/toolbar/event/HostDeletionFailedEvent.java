package org.jwebconsole.client.application.toolbar.event;

import com.google.gwt.event.shared.GwtEvent;

public class HostDeletionFailedEvent extends GwtEvent<HostDeletionFailedEventHandler> {

    public static final Type<HostDeletionFailedEventHandler> TYPE = new Type<HostDeletionFailedEventHandler>();

    @Override
    public Type<HostDeletionFailedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HostDeletionFailedEventHandler handler) {
        handler.onDeletionFailed(this);
    }
}
