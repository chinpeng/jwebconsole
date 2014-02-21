package org.jwebconsole.client.application.content.thread.widget.chart;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.thread.ThreadCountEntity;
import org.jwebconsole.client.model.thread.ThreadCountListResponse;
import org.jwebconsole.client.service.SuccessCallback;

import java.util.List;

public class ThreadCountChartPresenter extends PresenterWidget<ThreadCountChartView> {

    private ThreadCountChartPresenterFacade facade;
    private HostConnection selectedConnection;

    @Inject
    public ThreadCountChartPresenter(EventBus eventBus, ThreadCountChartView view, ThreadCountChartPresenterFacade facade) {
        super(eventBus, view);
        this.facade = facade;
    }

    public void init(HostConnection selectedConnection) {
        this.selectedConnection = selectedConnection;
        makeServerRequest();
    }

    private void makeServerRequest() {
        getView().mask(facade.getLoadingMessage());
        facade.getLastFifteenThreadInfoRows(selectedConnection.getId(), new SuccessCallback<ThreadCountListResponse>() {

            @Override
            public void beforeResponse() {
                getView().unmask();
            }

            @Override
            public void onSuccess(ThreadCountListResponse response) {
                processThreadCountListResponse(response.getBody());
            }
        });
    }

    @Override
    public void unbind() {
        super.unbind();
        getView().hideChart();
        getView().disableAutoResize();
    }

    private void processThreadCountListResponse(List<ThreadCountEntity> items) {
        initView();
        provideChartAxisBounds(items);
        populateChartWithEntities(items);
        showChart();
    }

    private void showChart() {
        getView().refreshChart();
        getView().showChart();
    }

    private void populateChartWithEntities(List<ThreadCountEntity> items) {
        for (ThreadCountEntity entity : items) {
            getView().addThreadCountEntity(entity);
        }
    }

    private void initView() {
        getView().clearChart();
        getView().enableAutoResize();
    }

    private void provideChartAxisBounds(List<ThreadCountEntity> items) {
        getView().setMinThreadAxis(facade.getMinAxisBound(items));
        getView().setMaxThreadAxis(facade.getMaxAxisBound(items));
    }

}
