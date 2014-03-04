package org.jwebconsole.client.application.content.thread.widget.chart;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import org.jwebconsole.client.application.content.main.ContentTabModule;

public class ThreadCountChartModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        bindSingletonPresenterWidget(ThreadCountChartPresenter.class, ThreadCountChartView.class, ThreadCountChartViewImpl.class);
    }

}
