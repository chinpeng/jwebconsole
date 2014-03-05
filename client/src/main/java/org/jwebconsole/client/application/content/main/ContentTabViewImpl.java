package org.jwebconsole.client.application.content.main;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class ContentTabViewImpl extends ViewWithUiHandlers<ContentTabUiHandlers> implements ContentTabView {

    private static final String NAME_TOKEN_ATTRIBUTE = "nameToken";

    @UiField
    SimpleContainer threadPanel;

    @UiField
    SimpleContainer memoryPanel;

    @UiField
    TabPanel tabPanel;

    private List<Component> panelWidgets = new ArrayList<Component>();

    interface Binder extends UiBinder<Widget, ContentTabViewImpl> {
    }


    @Inject
    ContentTabViewImpl(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        initHandlers();
        initTabList();
    }

    private void initTabList() {
        panelWidgets.add(threadPanel);
        panelWidgets.add(memoryPanel);
    }

    private void initHandlers() {
        tabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
            @Override
            public void onSelection(SelectionEvent<Widget> event) {
                Component component = (Component) event.getSelectedItem();
                String token = component.getData(NAME_TOKEN_ATTRIBUTE);
                getUiHandlers().onActiveTabSelected(token);
            }
        });
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == ContentTabPresenter.SLOT_THREADS) {
            threadPanel.clear();
            threadPanel.add(content);
            threadPanel.forceLayout();
        }
        if (slot == ContentTabPresenter.SLOT_MEMORY) {
            memoryPanel.clear();
            memoryPanel.add(content);
            memoryPanel.forceLayout();
        }
    }

    @Override
    public void setMemoryNameToken(String token) {
        this.memoryPanel.setData(NAME_TOKEN_ATTRIBUTE, token);
    }

    @Override
    public void setThreadsNameToken(String token) {
        this.threadPanel.setData(NAME_TOKEN_ATTRIBUTE, token);
    }

    @Override
    public void applySelectionByNameToken(String token) {
        for (Component component : panelWidgets) {
            String widgetToken = component.getData(NAME_TOKEN_ATTRIBUTE);
            if (token.equals(widgetToken)) {
                tabPanel.setActiveWidget(component);
            }
        }
    }

}
