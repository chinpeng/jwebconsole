package org.jwebconsole.client.application.content.main;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import org.jwebconsole.client.application.content.main.widget.ExtTabPanelWrapperWidget;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class ContentTabViewImpl extends ViewWithUiHandlers<ContentTabUiHandlers> implements ContentTabView {

    @UiField
    TabPanel tabPanel;

    @UiField
    SimpleContainer contentPanel;

    private ExtTabPanelWrapperWidget tabPanelWidget;

    interface Binder extends UiBinder<Widget, ContentTabViewImpl> {

    }

    @Inject
    ContentTabViewImpl(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == ContentTabPresenter.TYPE_SET_TAB_CONTENT) {
            contentPanel.clear();
            contentPanel.add(content);
            contentPanel.forceLayout();
        }
    }

    @Override
    public Tab addTab(TabData tabData, String historyToken) {
        return tabPanelWidget.addTab(tabData, historyToken);
    }

    @Override
    public void removeTab(Tab tab) {
        tabPanelWidget.removeTab(tab);
    }

    @Override
    public void removeTabs() {
        tabPanelWidget.removeTabs();
    }

    @Override
    public void setActiveTab(Tab tab) {
        tabPanelWidget.setActiveTab(tab);
    }

    @Override
    public void changeTab(Tab tab, TabData tabData, String historyToken) {
        tabPanelWidget.changeTab(tab, tabData, historyToken);
    }


    @Override
    public void setUiHandlers(ContentTabUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        initTab();
    }

    private void initTab() {
        this.tabPanelWidget = new ExtTabPanelWrapperWidget(tabPanel, getUiHandlers());
    }
}
