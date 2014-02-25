package org.jwebconsole.client.application.content.thread;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.widget.core.client.FramedPanel;
import org.jwebconsole.client.bundle.AppResources;

import javax.inject.Inject;

public class ThreadContentViewImpl extends ViewWithUiHandlers<ThreadContentUiHandlers> implements ThreadContentView {

    private final AppResources appResources;

    @UiField
    FramedPanel chartPanel;

    interface Binder extends UiBinder<Widget, ThreadContentViewImpl> {
    }

    @Inject
    ThreadContentViewImpl(Binder uiBinder, AppResources appResources) {
        this.appResources = appResources;
        initWidget(uiBinder.createAndBindUi(this));
        init();
    }

    private void init() {

    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == ThreadContentPresenter.THREAD_CHART_WIDGET_SLOT) {
            chartPanel.clear();
            chartPanel.add(content);
            chartPanel.forceLayout();
        }
    }


}