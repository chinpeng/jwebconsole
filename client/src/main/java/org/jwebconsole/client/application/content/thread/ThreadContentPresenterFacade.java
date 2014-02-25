package org.jwebconsole.client.application.content.thread;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasSlots;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.application.content.thread.widget.chart.ThreadCountChartPresenter;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.event.info.PrintInfoEvent;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.jwebconsole.client.place.NameTokens;
import org.jwebconsole.client.service.ServiceFactory;

public class ThreadContentPresenterFacade {

    private EventBus eventBus;
    private AppResources resources;
    private ServiceFactory serviceFactory;
    private PlaceManager placeManager;
    private ThreadCountChartPresenter threadCountChartPresenter;

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


    public void revealThreadCountChartPresenter(HasSlots parent, HostConnection connection) {
        threadCountChartPresenter.init(connection);
        parent.setInSlot(ThreadContentPresenter.THREAD_CHART_WIDGET_SLOT, threadCountChartPresenter);
    }

    public void disableChart() {
        threadCountChartPresenter.destroy();
    }
}
