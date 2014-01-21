package org.jwebconsole.client.event.popup;

import com.google.gwt.event.shared.GwtEvent;

public class RevealConnectionPopupEvent extends GwtEvent<RevealConnectionPopupEventHandler> {

    public static final Type<RevealConnectionPopupEventHandler> TYPE = new Type<RevealConnectionPopupEventHandler>();

    @Override
    public Type<RevealConnectionPopupEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RevealConnectionPopupEventHandler handler) {
        handler.onRevealEvent(this);
    }

    public static Type<RevealConnectionPopupEventHandler> getType() {
        return TYPE;
    }
}
