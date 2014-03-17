package org.jwebconsole.client.util;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.container.ResizeContainer;

public class ContainerUtils {

    public static void clearAndPut(ResizeContainer container, IsWidget widget) {
        container.clear();
        container.add(widget);
        container.forceLayout();
    }

}
