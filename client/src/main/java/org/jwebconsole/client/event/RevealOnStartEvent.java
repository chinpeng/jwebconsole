package org.jwebconsole.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class RevealOnStartEvent extends GwtEvent<RevealOnStartEventHandler> {

    public static Type<RevealOnStartEventHandler> TYPE = new Type<RevealOnStartEventHandler>();

    @Override
    public Type<RevealOnStartEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RevealOnStartEventHandler handler) {
        handler.onApplicationStart(this);
    }

    public static Type<RevealOnStartEventHandler> getType() {
        return TYPE;
    }

}
