package org.jwebconsole.client.event.info;

import com.google.gwt.event.shared.GwtEvent;

public class PrintInfoEvent extends GwtEvent<PrintInfoEventHandler> {

    public static final Type<PrintInfoEventHandler> TYPE = new Type<PrintInfoEventHandler>();

    private String title = "";
    private String text;

    public PrintInfoEvent(String title) {
        this.title = title;
    }

    public PrintInfoEvent(String title, String text) {
        this.title = title;
        this.text = text;
    }

    @Override
    public Type<PrintInfoEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PrintInfoEventHandler handler) {
        handler.onEventReceived(this);
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
