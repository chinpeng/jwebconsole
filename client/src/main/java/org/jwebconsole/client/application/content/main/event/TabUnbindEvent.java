package org.jwebconsole.client.application.content.main.event;

import com.google.gwt.event.shared.GwtEvent;

public class TabUnbindEvent extends GwtEvent<TabUnbindEventHandler> {
    public static Type<TabUnbindEventHandler> TYPE = new Type<TabUnbindEventHandler>();

    public Type<TabUnbindEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(TabUnbindEventHandler handler) {
        handler.onTabUnbind(this);
    }
}
