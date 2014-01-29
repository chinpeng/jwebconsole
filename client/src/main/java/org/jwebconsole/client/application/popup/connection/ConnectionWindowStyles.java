package org.jwebconsole.client.application.popup.connection;

import com.google.gwt.resources.client.CssResource;

public interface ConnectionWindowStyles extends CssResource {

    @ClassName("buttons-right-align")
    String buttonsAlign();

    @ClassName("content-margin")
    String contentMargin();

    int popupHeight();
}
