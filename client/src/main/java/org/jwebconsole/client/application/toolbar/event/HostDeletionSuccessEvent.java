package org.jwebconsole.client.application.toolbar.event;

import com.google.gwt.event.shared.GwtEvent;
import org.jwebconsole.client.model.host.HostConnection;

public class HostDeletionSuccessEvent extends GwtEvent<HostDeletionSuccessEventHandler> {

    public static final Type<HostDeletionSuccessEventHandler> TYPE = new Type<HostDeletionSuccessEventHandler>();

    private String connectionId;

    public HostDeletionSuccessEvent(String connectionId) {
        this.connectionId = connectionId;
    }

    @Override
    public Type<HostDeletionSuccessEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HostDeletionSuccessEventHandler handler) {
        handler.onSuccessDeletion(this);
    }

    public String getConnectionId() {
        return connectionId;
    }
}
