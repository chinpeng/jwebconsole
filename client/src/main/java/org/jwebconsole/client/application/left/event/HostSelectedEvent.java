package org.jwebconsole.client.application.left.event;

import com.google.gwt.event.shared.GwtEvent;
import org.jwebconsole.client.model.host.HostConnection;

public class HostSelectedEvent extends GwtEvent<HostSelectedEventHandler> {

    public static final Type<HostSelectedEventHandler> TYPE = new Type<HostSelectedEventHandler>();

    private HostConnection connection;

    public HostSelectedEvent(HostConnection connection) {
        this.connection = connection;
    }

    public static Type<HostSelectedEventHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<HostSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HostSelectedEventHandler handler) {
        handler.onHostSelected(this);
    }

    public HostConnection getConnection() {
        return connection;
    }
}
