package org.jwebconsole.client.application.content.thread.widget.chart;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import org.jwebconsole.client.bundle.AppResources;

import javax.inject.Inject;

public class ThreadCountChartViewImpl extends ViewWithUiHandlers<ThreadCountChartUiHandlers> implements ThreadCountChartView {

    private final AppResources appResources;

    interface Binder extends UiBinder<HTMLPanel, ThreadCountChartViewImpl> {
    }

    @Inject
    ThreadCountChartViewImpl(Binder uiBinder, AppResources appResources) {
        this.appResources = appResources;
        initWidget(uiBinder.createAndBindUi(this));
        init();
    }

    private void init() {

    }

}