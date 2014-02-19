package org.jwebconsole.client.application.content.thread;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import org.jwebconsole.client.application.content.thread.widget.chart.ThreadCountChartPresenter;
import org.jwebconsole.client.application.content.thread.widget.chart.ThreadCountChartWidget;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.model.thread.ThreadCountEntity;

import javax.inject.Inject;
import java.util.List;

public class ThreadContentViewImpl extends ViewWithUiHandlers<ThreadContentUiHandlers> implements ThreadContentView {

    private final AppResources appResources;

    @UiField
    HTMLPanel mainPanel;

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
            mainPanel.clear();
            mainPanel.add(content);
        }
    }
}