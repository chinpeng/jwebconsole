package org.jwebconsole.client.bundle;

import com.google.inject.Inject;

public class AppResources {

    private AppIcons icons;

    private AppStyles styles;

    @Inject
    public AppResources(AppIcons icons, AppStyles styles) {
        this.icons = icons;
        this.styles = styles;
    }

    public AppIcons getIcons() {
        return icons;
    }

    public AppStyles getStyles() {
        return styles;
    }
}
