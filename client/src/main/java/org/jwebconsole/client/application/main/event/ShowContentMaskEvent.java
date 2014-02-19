package org.jwebconsole.client.application.main.event;

import com.google.gwt.event.shared.GwtEvent;

public class ShowContentMaskEvent extends GwtEvent<ShowContentMaskEventHandler> {
    public static Type<ShowContentMaskEventHandler> TYPE = new Type<ShowContentMaskEventHandler>();

    public Type<ShowContentMaskEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ShowContentMaskEventHandler handler) {
        handler.onShowContentMask(this);
    }
}
