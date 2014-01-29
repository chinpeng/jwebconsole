package org.jwebconsole.client.widget;

import com.google.gwt.resources.client.CssResource;

public interface LoadingIndicatorStyles extends CssResource {

    @ClassName("indicator-main")
    String indicatorMain();

    @ClassName("loading-text")
    String loadingText();

    @ClassName("indicator-image")
    String indicatorImage();

    @ClassName("indicator-block")
    String indicatorBlock();
}
