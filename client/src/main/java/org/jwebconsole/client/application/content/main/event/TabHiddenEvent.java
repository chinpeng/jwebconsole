package org.jwebconsole.client.application.content.main.event;

import com.google.gwt.event.shared.GwtEvent;

public class TabHiddenEvent extends GwtEvent<TabHiddenEventHandler> {
    public static Type<TabHiddenEventHandler> TYPE = new Type<TabHiddenEventHandler>();

    public Type<TabHiddenEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(TabHiddenEventHandler handler) {
        handler.onTabUnbind(this);
    }
}
