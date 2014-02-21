package org.jwebconsole.client.application.content.thread.widget.chart;

import com.google.inject.Inject;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.application.content.thread.widget.chart.util.AxisBoundCounter;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.model.thread.ThreadCountEntity;
import org.jwebconsole.client.model.thread.ThreadCountListResponse;
import org.jwebconsole.client.service.ServiceFactory;

import java.util.List;

public class ThreadCountChartPresenterFacade {

    private ServiceFactory serviceFactory;
    private AppResources appResources;
    private AxisBoundCounter axisBoundCounter;

    private static final Integer DEFAULT_QUERY_ROW_COUNT = 15;

    @Inject
    public ThreadCountChartPresenterFacade(ServiceFactory serviceFactory, AppResources appResources, AxisBoundCounter axisBoundCounter) {
        this.serviceFactory = serviceFactory;
        this.appResources = appResources;
        this.axisBoundCounter = axisBoundCounter;
    }

    public void getLastFifteenThreadInfoRows(String hostId, MethodCallback<ThreadCountListResponse> callback) {
        serviceFactory.getThreadService().getLastNumberOfThreadInfo(DEFAULT_QUERY_ROW_COUNT, hostId, callback);
    }

    public String getLoadingMessage() {
        return appResources.getMessages().loadingMaskText();
    }

    public Integer getMinAxisBound(List<ThreadCountEntity> entities) {
        return axisBoundCounter.getMinAxisBound(entities);
    }

    public Integer getMaxAxisBound(List<ThreadCountEntity> entities) {
        return axisBoundCounter.getMaxAxisBound(entities);
    }
}
