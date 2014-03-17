package org.jwebconsole.client.application.content.thread.widget.chart;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.thread.count.ThreadCountEntity;
import org.jwebconsole.client.model.thread.count.ThreadCountListResponse;
import org.jwebconsole.client.service.AppCallback;
import org.jwebconsole.client.service.SuccessCallback;

import java.util.LinkedList;
import java.util.List;

public class ThreadCountChartPresenter extends PresenterWidget<ThreadCountChartView> {

    private ThreadCountChartPresenterFacade facade;
    private LinkedList<ThreadCountEntity> entities = new LinkedList<ThreadCountEntity>();
    private String connectionId;

    @Inject
    public ThreadCountChartPresenter(EventBus eventBus, ThreadCountChartView view, ThreadCountChartPresenterFacade facade) {
        super(eventBus, view);
        this.facade = facade;
    }

    public void init(String connectionId) {
        facade.destroyTimer();
        this.connectionId = connectionId;
        makeServerRequest();
        startChartUpdating();
    }

    private void makeServerRequest() {
        getView().mask(facade.getLoadingMessage());
        facade.getLastFifteenThreadInfoRows(connectionId, new AppCallback<ThreadCountListResponse>() {

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

    private void startChartUpdating() {
        facade.scheduleUpdateTimer(connectionId, new SuccessCallback<ThreadCountEntity>() {
            @Override
            public void onSuccess(ThreadCountEntity body) {
                entities.add(body);
                updateView();
            }
        });
    }

    private void updateView() {
        getView().clearChart();
        if (!entities.isEmpty()) {
            entities.removeFirst();
        }
        for (ThreadCountEntity threadCountEntity : entities) {
            getView().addThreadCountEntity(threadCountEntity);
        }
        provideChartAxisBounds(entities);
        getView().refreshChart();
    }

    private void processThreadCountListResponse(List<ThreadCountEntity> items) {
        facade.appendWithDefaultValues(items);
        this.entities = new LinkedList<ThreadCountEntity>(items);
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
    }

    private void provideChartAxisBounds(List<ThreadCountEntity> items) {
        getView().setMinThreadAxis(facade.getMinAxisBound(items));
        getView().setMaxThreadAxis(facade.getMaxAxisBound(items));
        getView().setMinDate(facade.getMinDate(items));
        getView().setMaxDate(facade.getMaxDate(items));
    }

    public void destroy() {
        facade.destroyTimer();
    }
}
