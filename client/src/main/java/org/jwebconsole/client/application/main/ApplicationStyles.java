package org.jwebconsole.client.application.main;

import com.google.gwt.resources.client.CssResource;

public interface ApplicationStyles extends CssResource {

    @ClassName("app-main-window")
    String appMainWindow();

    @ClassName("app-content-body")
    String appContentBody();

    @ClassName("app-container")
    String appContainer();

    @ClassName("app-left-panel")
    String appLeftPanel();

    @ClassName("app-right-panel")
    String appRightPanel();
}
