package org.jwebconsole.client.application.popup.connection.event;

import com.google.gwt.event.shared.GwtEvent;
import org.jwebconsole.client.model.host.HostConnection;

public class HostCreatedEvent extends GwtEvent<HostCreatedEventHandler> {

    public static final Type<HostCreatedEventHandler> TYPE = new Type<HostCreatedEventHandler>();

    private HostConnection connection;

    public HostCreatedEvent(HostConnection connection) {
        this.connection = connection;
    }

    @Override
    public Type<HostCreatedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HostCreatedEventHandler handler) {
        handler.onHostCreated(this);
    }

    public HostConnection getConnection() {
        return connection;
    }

    public void setConnection(HostConnection connection) {
        this.connection = connection;
    }
}
