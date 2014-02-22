package org.jwebconsole.client.bundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import org.jwebconsole.client.application.popup.connection.ConnectionWindowStyles;

public interface AppStyles extends ClientBundle {

    AppStyles INSTANCE = GWT.create(AppStyles.class);

    @Source("css/connection/connection-window.css")
    ConnectionWindowStyles connectionWindow();

}
