package org.jwebconsole.client.bundle;

import com.google.inject.Inject;
import org.jwebconsole.client.bundle.messages.Messages;

public class AppResources {

    private AppIcons icons;

    private Messages messages;

    @Inject
    public AppResources(AppIcons icons, Messages messages) {
        this.icons = icons;
        this.messages = messages;
    }

    public AppIcons getIcons() {
        return icons;
    }

    public Messages getMessages() {
        return messages;
    }
}
