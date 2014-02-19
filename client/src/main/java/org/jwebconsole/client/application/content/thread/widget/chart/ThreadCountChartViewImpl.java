package org.jwebconsole.client.application.content.thread.widget.chart;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.widget.core.client.FramedPanel;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.model.thread.ThreadCountEntity;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThreadCountChartViewImpl extends ViewWithUiHandlers<ThreadCountChartUiHandlers> implements ThreadCountChartView {

    private final AppResources appResources;
    private ThreadCountChartWidget chartWidget;

    @UiField
    FramedPanel chartPanel;

    interface Binder extends UiBinder<Widget, ThreadCountChartViewImpl> {
    }

    @Inject
    ThreadCountChartViewImpl(Binder uiBinder, AppResources appResources, ThreadCountChartWidget chartWidget) {
        this.appResources = appResources;
        this.chartWidget = chartWidget;
        appResources.getStyles().threadCountChartStyles().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
        init();
    }

    private void init() {
        chartWidget.init();
        chartWidget.populate(createMockEntities());
        chartPanel.add(chartWidget);
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                chartWidget.refresh(chartPanel.getOffsetWidth() - 30);
            }
        });
    }

    private List<ThreadCountEntity> createMockEntities() {
        List<ThreadCountEntity> result = new ArrayList<ThreadCountEntity>();
        result.add(new ThreadCountEntity(10, "test", new Date(), 20, 25));
        result.add(new ThreadCountEntity(11, "test", new Date(), 21, 26));
        result.add(new ThreadCountEntity(12, "test", new Date(), 23, 27));
        return result;
    }

    @Override
    public void setMinThreadAxis(Integer value) {

    }

    @Override
    public void setMaxThreadAxis(Integer value) {

    }

    @Override
    public void refreshChart() {

    }

    @Override
    public void addThreadCountEntity(ThreadCountEntity threadCountEntity) {

    }

    @Override
    public void clear() {

    }


}