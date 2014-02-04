package org.jwebconsole.client.application.toolbar.event;

import com.google.gwt.event.shared.GwtEvent;
import org.jwebconsole.client.model.host.HostConnection;

public class HostDeletionSuccessEvent extends GwtEvent<HostDeletionSuccessEventHandler> {

    public static final Type<HostDeletionSuccessEventHandler> TYPE = new Type<HostDeletionSuccessEventHandler>();

    private HostConnection deletedHost;

    public HostDeletionSuccessEvent(HostConnection deletedHost) {
        this.deletedHost = deletedHost;
    }

    @Override
    public Type<HostDeletionSuccessEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HostDeletionSuccessEventHandler handler) {
        handler.onSuccessDeletion(this);
    }

    public HostConnection getDeletedHost() {
        return deletedHost;
    }
}
