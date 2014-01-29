package org.jwebconsole.client.widget;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class LoadingIndicator {

    private HasWidgets panel;
    private Widget indicator;

    public LoadingIndicator(HasWidgets panel) {
        this.panel = panel;
    }

    public void show() {
        this.indicator = createIndicator();
    }

    private Widget createIndicator() {
        Widget indicator = new Widget();
        return indicator;
    }

    public void hide()  {
        panel.remove(indicator);
    }


}
