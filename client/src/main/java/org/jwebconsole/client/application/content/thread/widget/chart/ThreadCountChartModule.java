package org.jwebconsole.client.application.content.thread.widget.chart;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ThreadCountChartModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        bindSingletonPresenterWidget(ThreadCountChartPresenter.class, ThreadCountChartView.class, ThreadCountChartViewImpl.class);
    }

}
