package org.jwebconsole.client.application.content.thread.widget.chart;

import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.application.content.thread.widget.chart.util.AxisBoundCounter;
import org.jwebconsole.client.application.content.thread.widget.chart.util.DateAxisBoundCounter;
import org.jwebconsole.client.application.content.thread.widget.chart.util.DefaultValuesFiller;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.model.thread.count.ThreadCountEntity;
import org.jwebconsole.client.model.thread.count.ThreadCountListResponse;
import org.jwebconsole.client.service.AppCallback;
import org.jwebconsole.client.service.ServiceFactory;
import org.jwebconsole.client.service.SuccessCallback;

import java.util.Date;
import java.util.List;

public class ThreadCountChartPresenterFacade {

    private ServiceFactory serviceFactory;
    private AppResources appResources;
    private AxisBoundCounter axisBoundCounter;
    private DateAxisBoundCounter dateAxisBoundCounter;
    private DefaultValuesFiller filler;

    private static final Integer DEFAULT_QUERY_ROW_COUNT = 30;
    private static final Integer UPDATE_TIME = 3000;
    private static final Integer ONE_QUERY_ROW_COUNT = 1;
    private Timer timer;


    @Inject
    public ThreadCountChartPresenterFacade(ServiceFactory serviceFactory,
                                           AppResources appResources,
                                           AxisBoundCounter axisBoundCounter,
                                           DateAxisBoundCounter dateAxisBoundCounter,
                                           DefaultValuesFiller filler) {
        this.serviceFactory = serviceFactory;
        this.appResources = appResources;
        this.axisBoundCounter = axisBoundCounter;
        this.dateAxisBoundCounter = dateAxisBoundCounter;
        this.filler = filler;
    }

    public void getLastFifteenThreadInfoRows(String hostId, MethodCallback<ThreadCountListResponse> callback) {
        serviceFactory.getThreadService().getLastNumberOfThreadCount(DEFAULT_QUERY_ROW_COUNT, hostId, callback);
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

    public void scheduleUpdateTimer(final String hostId, final SuccessCallback<ThreadCountEntity> callback) {
        this.timer = new Timer() {
            @Override
            public void run() {
                serviceFactory.getThreadService().getLastNumberOfThreadCount(ONE_QUERY_ROW_COUNT, hostId, new AppCallback<ThreadCountListResponse>() {
                    @Override
                    public void onSuccess(ThreadCountListResponse response) {
                        if (response.getBody() != null && !response.getBody().isEmpty()) {
                            callback.onSuccess(response.getBody().get(0));
                        }
                    }
                });
            }
        };
        timer.scheduleRepeating(UPDATE_TIME);
    }


    public void destroyTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public Date getMinDate(List<ThreadCountEntity> entities) {
        return dateAxisBoundCounter.getMinDate(entities);
    }

    public Date getMaxDate(List<ThreadCountEntity> entities) {
        return dateAxisBoundCounter.getMaxDate(entities);
    }

    public void appendWithDefaultValues(List<ThreadCountEntity> entities) {
        filler.fillWithDefaultValues(DEFAULT_QUERY_ROW_COUNT, entities);
    }
}
