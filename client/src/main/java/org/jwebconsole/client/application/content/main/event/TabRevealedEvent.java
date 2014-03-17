package org.jwebconsole.client.application.content.main.event;

import com.google.gwt.event.shared.GwtEvent;

public class TabRevealedEvent extends GwtEvent<TabRevealedEventHandler> {
    public static Type<TabRevealedEventHandler> TYPE = new Type<TabRevealedEventHandler>();

    public Type<TabRevealedEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(TabRevealedEventHandler handler) {
        handler.onTabRevealed(this);
    }
}
