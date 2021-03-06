package org.jwebconsole.client.application.content.main.widget;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.TabPanel;
import org.jwebconsole.client.application.content.main.ContentTabUiHandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExtTabPanelWrapperWidget implements TabPanel {

    private com.sencha.gxt.widget.core.client.TabPanel extTabPanel;
    private ContentTabUiHandlers handlers;

    List<ExtTabWrapper> tabs = new ArrayList<ExtTabWrapper>();

    public ExtTabPanelWrapperWidget(com.sencha.gxt.widget.core.client.TabPanel extTabPanel, ContentTabUiHandlers handlers) {
        this.extTabPanel = extTabPanel;
        this.handlers = handlers;
        initListener();
    }

    private void initListener() {
        extTabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
            @Override
            public void onSelection(SelectionEvent<Widget> widgetSelectionEvent) {
                redirectToToken(widgetSelectionEvent.getSelectedItem());
            }
        });
    }

    private void redirectToToken(Widget selectedItem) {
        ExtTabWrapper selectedTab = findByWidget(selectedItem);
        if (selectedTab != null) {
            handlers.redirect(selectedTab.getHistoryToken());
        }
    }

    private ExtTabWrapper findByWidget(Widget selectedItem) {
        for (ExtTabWrapper tab : tabs) {
            if (tab.asWidget() == selectedItem.asWidget()) {
                return tab;
            }
        }
        return null;
    }

    @Override
    public Tab addTab(TabData tabData, String historyToken) {
        ExtTabWrapper result = new ExtTabWrapper(extTabPanel, tabData.getLabel(), historyToken, tabData.getPriority());
        tabs.add(result);
        reorderTabs();
        refreshTabs();
        return result;
    }

    private void refreshTabs() {
        removeTabsFromView();
        for (ExtTabWrapper tabWrapper : tabs) {
            extTabPanel.add(tabWrapper.asWidget(), tabWrapper.getConfig());
        }
    }

    private void reorderTabs() {
        Collections.sort(tabs, new Comparator<ExtTabWrapper>() {
            @Override
            public int compare(ExtTabWrapper first, ExtTabWrapper second) {
                if (first.getPriority() > second.getPriority()) return 1;
                if (first.getPriority() < second.getPriority()) return -1;
                return 0;
            }
        });
    }

    @Override
    public void removeTab(Tab tab) {
        extTabPanel.remove(tab.asWidget());
        tabs.remove(tab);
    }

    @Override
    public void removeTabs() {
        removeTabsFromView();
        tabs.clear();
    }

    private void removeTabsFromView() {
        for (ExtTabWrapper wrapper : tabs) {
            extTabPanel.remove(wrapper.asWidget());
        }
    }

    @Override
    public void setActiveTab(Tab tab) {
        tab.activate();
    }

    @Override
    public void changeTab(Tab tab, TabData tabData, String historyToken) {
        tab.setText(tabData.getLabel());
        tab.setTargetHistoryToken(historyToken);
    }


}
