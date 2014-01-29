package org.jwebconsole.client.bundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import org.jwebconsole.client.application.popup.connection.ConnectionWindowStyles;
import org.jwebconsole.client.widget.LoadingIndicatorStyles;

public interface AppResources extends ClientBundle {

    AppResources INSTANCE = GWT.create(AppResources.class);

    @Source("application.css")
    ApplicationStyles app();

    @Source("popup/connection/connection-window.css")
    ConnectionWindowStyles connectionWindow();

    @Source("widget/indicator.css")
    LoadingIndicatorStyles indicatorStyles();

    @Source("images/indicator.gif")
    ImageResource indicator();

    /*
    *  <div class="panel-widget">
        <p>Some text here</p>
        <div class="indicator-main">
            <div class="indicator-block">
                <div class="indicator-image">
                    <p class="loading-text">Please, wait</p>
                </div>
            </div>
        </div>
    </div>*/

}
