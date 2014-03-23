package org.jwebconsole.client.application.content.overview;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.HasSlots;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import org.jwebconsole.client.application.content.thread.widget.chart.ThreadCountChartPresenter;
import org.jwebconsole.client.place.AppParams;
import org.jwebconsole.client.util.monad.option.Option;

public class OverviewPresenterFacade {

    private ThreadCountChartPresenter threadCountChartPresenter;
    private PlaceManager placeManager;

    @Inject
    public OverviewPresenterFacade(ThreadCountChartPresenter threadCountChartPresenter, PlaceManager placeManager) {
        this.threadCountChartPresenter = threadCountChartPresenter;
        this.placeManager = placeManager;
    }

    public void revealThreadCountChartPresenter(HasSlots parent, String connectionId) {
        threadCountChartPresenter.init(connectionId);
        parent.setInSlot(OverviewPresenter.THREAD_CHART_WIDGET_SLOT, threadCountChartPresenter);
    }


    public Option<String> getConnectionId() {
        String result = placeManager.getCurrentPlaceRequest().getParameter(AppParams.HOST_ID, null);
        return Option.create(result);
    }

    public void stopThreadChart() {
        threadCountChartPresenter.destroy();
    }
}
