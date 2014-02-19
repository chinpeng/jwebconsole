package org.jwebconsole.client.application.content.thread.widget.chart;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import org.jwebconsole.client.model.host.HostConnection;

public class ThreadCountChartPresenter extends PresenterWidget<ThreadCountChartView> {

    private HostConnection selectedConnection;

    @Inject
    public ThreadCountChartPresenter(EventBus eventBus, ThreadCountChartView view) {
        super(eventBus, view);
    }

    public void init(HostConnection selectedConnection) {
        this.selectedConnection = selectedConnection;
    }

}
