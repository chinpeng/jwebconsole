package org.jwebconsole.client.bundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import org.jwebconsole.client.application.ApplicationStyles;
import org.jwebconsole.client.application.popup.connection.ConnectionWindowStyles;

public interface AppResources extends ClientBundle {

    AppResources INSTANCE = GWT.create(AppResources.class);

    @Source("css/application.css")
    ApplicationStyles app();

    @Source("css/connection/connection-window.css")
    ConnectionWindowStyles connectionWindow();


}
