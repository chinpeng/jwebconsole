package org.jwebconsole.client.application.content.overview;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;

import javax.inject.Inject;


public class OverviewViewImpl extends ViewWithUiHandlers<OverviewUiHandlers> implements OverviewView {
    @UiField
    PortalLayoutContainer portal;

    @UiField
    Portlet threadChartPortlet;

    interface Binder extends UiBinder<Widget, OverviewViewImpl> {
    }

    @Inject
    OverviewViewImpl(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        initPortal();
    }

    private void initPortal() {
        portal.setColumnWidth(0, .48);
        portal.setColumnWidth(1, .5);
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == OverviewPresenter.THREAD_CHART_WIDGET_SLOT) {
            Window.alert("bla");
            threadChartPortlet.clear();
            threadChartPortlet.add(content);
            threadChartPortlet.forceLayout();
        }
    }
}
