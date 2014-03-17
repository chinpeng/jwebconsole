package org.jwebconsole.client.application.content.main.widget;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.Tab;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;

public class ExtTabWrapper implements Tab {

    private TabItemConfig config;
    private HTMLPanel widget = new HTMLPanel("");
    private String historyToken;
    private TabPanel tabPanel;

    public ExtTabWrapper(TabPanel extTabPanel, String label, String historyToken) {
        this.tabPanel = extTabPanel;
        this.config = new TabItemConfig(label);
        this.historyToken = historyToken;
        tabPanel.add(widget, config);
    }

    @Override
    public void activate() {
        tabPanel.setActiveWidget(widget);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void deactivate() {

    }

    @Override
    public float getPriority() {
        return 0;
    }

    @Override
    public String getText() {
        return this.config.getText();
    }

    @Override
    public void setTargetHistoryToken(String historyToken) {
        this.historyToken = historyToken;
    }

    @Override
    public void setText(String text) {
        this.config.setText(text);
    }

    public String getHistoryToken() {
        return historyToken;
    }
}
