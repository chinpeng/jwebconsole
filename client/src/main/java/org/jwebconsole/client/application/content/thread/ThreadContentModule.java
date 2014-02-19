package org.jwebconsole.client.application.content.thread;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import org.jwebconsole.client.application.content.thread.widget.chart.ThreadCountChartModule;

public class ThreadContentModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ThreadContentPresenter.class, ThreadContentView.class, ThreadContentViewImpl.class, ThreadContentPresenter.ThreadContentProxy.class);
        installWidgets();
    }

    private void installWidgets() {
        install(new ThreadCountChartModule());
    }
}
