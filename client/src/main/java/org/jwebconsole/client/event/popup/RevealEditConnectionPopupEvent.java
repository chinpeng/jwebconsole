package org.jwebconsole.client.event.popup;

import com.google.gwt.event.shared.GwtEvent;
import org.jwebconsole.client.model.host.HostConnection;

public class RevealEditConnectionPopupEvent extends GwtEvent<RevealEditConnectionPopupEventHandler> {

    public static Type<RevealEditConnectionPopupEventHandler> TYPE = new Type<RevealEditConnectionPopupEventHandler>();

    @Override
    public Type<RevealEditConnectionPopupEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RevealEditConnectionPopupEventHandler handler) {
        handler.onRevealEditEvent(this);
    }

    public static Type<RevealEditConnectionPopupEventHandler> getType() {
        return TYPE;
    }

}
