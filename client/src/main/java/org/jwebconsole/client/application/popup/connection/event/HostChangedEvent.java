package org.jwebconsole.client.application.popup.connection.event;

import com.google.gwt.event.shared.GwtEvent;
import org.jwebconsole.client.model.host.HostConnection;

public class HostChangedEvent extends GwtEvent<HostChangedEventHandler> {

    public static final Type<HostChangedEventHandler> TYPE = new Type<HostChangedEventHandler>();

    private HostConnection connection;

    public HostChangedEvent(HostConnection connection) {
        this.connection = connection;
    }

    @Override
    public Type<HostChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HostChangedEventHandler handler) {
        handler.onHostChanged(this);
    }

    public HostConnection getConnection() {
        return connection;
    }
}
