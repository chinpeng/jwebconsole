package org.jwebconsole.client.application.content.thread;

import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasSlots;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.application.content.thread.widget.chart.ThreadCountChartPresenter;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.thread.details.ThreadDetailsListResponse;
import org.jwebconsole.client.model.thread.info.ThreadInfoListResponse;
import org.jwebconsole.client.place.AppParams;
import org.jwebconsole.client.service.ServiceFactory;

public class ThreadContentPresenterFacade {

    private EventBus eventBus;
    private AppResources resources;
    private ServiceFactory serviceFactory;
    private PlaceManager placeManager;
    private ThreadCountChartPresenter threadCountChartPresenter;
    private Timer timer;

    private static final Integer THREAD_INFO_TIMER_UPDATE_INTERVAL = 10000;

    @Inject
    public ThreadContentPresenterFacade(EventBus eventBus,
                                        AppResources resources,
                                        ServiceFactory serviceFactory,
                                        PlaceManager placeManager,
                                        ThreadCountChartPresenter threadCountChartPresenter) {
        this.eventBus = eventBus;
        this.resources = resources;
        this.serviceFactory = serviceFactory;
        this.placeManager = placeManager;
        this.threadCountChartPresenter = threadCountChartPresenter;
    }


    public void revealThreadCountChartPresenter(HasSlots parent, String connectionId) {
        threadCountChartPresenter.init(connectionId);
        parent.setInSlot(ThreadContentPresenter.THREAD_CHART_WIDGET_SLOT, threadCountChartPresenter);
    }

    public void disableChart() {
        threadCountChartPresenter.destroy();
    }

    public void scheduleThreadInfoRequest(final String hostId, final MethodCallback<ThreadInfoListResponse> callback) {
        this.timer = new Timer() {
            @Override
            public void run() {
                serviceFactory.getThreadInfoService().getThreadInfo(hostId, callback);
            }
        };
        timer.run();
        timer.scheduleRepeating(THREAD_INFO_TIMER_UPDATE_INTERVAL);
    }

    public void makeThreadDetailsRequest(String hostId, Long threadId, MethodCallback<ThreadDetailsListResponse> callback) {
        serviceFactory.getThreadDetailsService().getThreadDetails(hostId, threadId, callback);
    }

    public String getCurrentConnectionId() {
        return placeManager.getCurrentPlaceRequest().getParameter(AppParams.HOST_ID, null);
    }

    public void disableThreadInfoTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
