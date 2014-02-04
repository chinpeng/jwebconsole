package org.jwebconsole.client.bundle;

import com.google.inject.Inject;
import org.jwebconsole.client.bundle.messages.Messages;

public class AppResources {

    private AppIcons icons;

    private AppStyles styles;

    private Messages messages;

    @Inject
    public AppResources(AppIcons icons, AppStyles styles, Messages messages) {
        this.icons = icons;
        this.styles = styles;
        this.messages = messages;
    }

    public AppIcons getIcons() {
        return icons;
    }

    public AppStyles getStyles() {
        return styles;
    }

    public Messages getMessages() {
        return messages;
    }
}
