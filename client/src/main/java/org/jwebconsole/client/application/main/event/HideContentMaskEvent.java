package org.jwebconsole.client.application.main.event;

import com.google.gwt.event.shared.GwtEvent;

public class HideContentMaskEvent extends GwtEvent<HideContentMaskEventHandler> {
    public static Type<HideContentMaskEventHandler> TYPE = new Type<HideContentMaskEventHandler>();

    public Type<HideContentMaskEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(HideContentMaskEventHandler handler) {
        handler.onHideContentMask(this);
    }
}
