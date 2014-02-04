package org.jwebconsole.client.event.popup;

import com.google.gwt.event.shared.GwtEvent;

public class RevealAddConnectionPopupEvent extends GwtEvent<RevealAddConnectionPopupEventHandler> {

    public static Type<RevealAddConnectionPopupEventHandler> TYPE = new Type<RevealAddConnectionPopupEventHandler>();

    @Override
    public Type<RevealAddConnectionPopupEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RevealAddConnectionPopupEventHandler handler) {
        handler.onRevealAddEvent(this);
    }

    public static Type<RevealAddConnectionPopupEventHandler> getType() {
        return TYPE;
    }
}
