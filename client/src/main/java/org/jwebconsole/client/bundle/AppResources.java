package org.jwebconsole.client.bundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import org.jwebconsole.client.application.popup.connection.ConnectionWindowStyles;
import org.jwebconsole.client.widget.LoadingIndicatorStyles;

public interface AppResources extends ClientBundle {

    AppResources INSTANCE = GWT.create(AppResources.class);

    @Source("application.css")
    Application app();

    @Source("popup/connection/connection-window.css")
    ConnectionWindowStyles connectionWindow();

    @Source("widget/indicator.css")
    LoadingIndicatorStyles indicatorStyles();

    AppImages appImages();

}
