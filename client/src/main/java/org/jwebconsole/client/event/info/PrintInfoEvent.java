package org.jwebconsole.client.event.info;

import com.google.gwt.event.shared.GwtEvent;
import org.jwebconsole.client.model.base.ErrorMessage;

public class PrintInfoEvent extends GwtEvent<PrintInfoEventHandler> {

    public static final Type<PrintInfoEventHandler> TYPE = new Type<PrintInfoEventHandler>();

    private String title = "";
    private String text;
    private ErrorMessage errorMessage;

    public PrintInfoEvent(String title) {
        this.title = title;
    }

    public PrintInfoEvent(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public PrintInfoEvent(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
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

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
