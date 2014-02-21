package org.jwebconsole.client.application.content.thread.widget.chart;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.FramedPanel;
import org.jwebconsole.client.application.content.thread.widget.chart.constants.DateAxisConstants;
import org.jwebconsole.client.application.content.thread.widget.chart.constants.NumberAxisConstants;
import org.jwebconsole.client.application.content.thread.widget.chart.constants.PeakThreadCountConstants;
import org.jwebconsole.client.application.content.thread.widget.chart.constants.ThreadCountLineConstants;
import org.jwebconsole.client.application.content.thread.widget.chart.model.ThreadCountEntityPropertyAccessor;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.model.thread.ThreadCountEntity;

import javax.inject.Inject;
import java.util.Date;

public class ThreadCountChartViewImpl extends ViewWithUiHandlers<ThreadCountChartUiHandlers> implements ThreadCountChartView {

    private static final ThreadCountEntityPropertyAccessor accessor = GWT.create(ThreadCountEntityPropertyAccessor.class);

    private AppResources appResources;
    private ListStore<ThreadCountEntity> store;
    private Chart<ThreadCountEntity> chart;
    private HandlerRegistration resizeHandler;

    @UiField
    FramedPanel chartPanel;

    interface Binder extends UiBinder<Widget, ThreadCountChartViewImpl> {
    }

    @Inject
    ThreadCountChartViewImpl(Binder uiBinder, AppResources appResources) {
        this.appResources = appResources;
        appResources.getStyles().threadCountChartStyles().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
        init();
    }

    @Override
    public void setMinThreadAxis(Integer value) {
        getThreadCountAxis().setMinimum(value);
    }

    @Override
    public void setMaxThreadAxis(Integer value) {
        getThreadCountAxis().setMaximum(value);
    }

    @Override
    public void refreshChart() {
        chart.redrawChart();
    }

    @Override
    public void addThreadCountEntity(ThreadCountEntity threadCountEntity) {
        store.add(threadCountEntity);
    }

    @Override
    public void clearChart() {
        store.clear();
    }

    @Override
    public void enableAutoResize() {
        resizeHandler = Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                chart.onResize(chartPanel.getOffsetWidth(), chart.getOffsetHeight());
            }
        });
    }

    @Override
    public void disableAutoResize() {
        if (resizeHandler != null) {
            resizeHandler.removeHandler();
        }
    }

    @Override
    public void mask(String message) {
        chartPanel.mask(message);
    }

    @Override
    public void unmask() {
        chartPanel.unmask();
    }

    @Override
    public void showChart() {
        chart.show();
    }

    @Override
    public void hideChart() {
        chart.hide();
    }

    private void init() {
        chart = new Chart<ThreadCountEntity>();
        chart.setShadowChart(true);
        store = new ListStore<ThreadCountEntity>(accessor.nameKey());
        chart.setStore(store);
        chart.addAxis(createNumericAxis());
        chart.addAxis(createDateAxis());
        chart.addSeries(createThreadCountLineSeries());
        chart.addSeries(createPeakThreadCountLineSeries());
        chart.hide();
        chartPanel.add(chart);
    }

    private CategoryAxis<ThreadCountEntity, Date> createDateAxis() {
        CategoryAxis<ThreadCountEntity, Date> catAxis = new CategoryAxis<ThreadCountEntity, Date>();
        catAxis.setPosition(Chart.Position.BOTTOM);
        catAxis.setField(accessor.time());
        TextSprite title = new TextSprite(appResources.getMessages().chartTimeAxisTitle());
        title.setFontSize(DateAxisConstants.TITLE_FONT_SIZE);
        catAxis.setTitleConfig(title);
        catAxis.setLabelProvider(new LabelProvider<Date>() {
            @Override
            public String getLabel(Date item) {
                DateTimeFormat format = DateAxisConstants.DATE_FORMAT;
                return format.format(item);
            }
        });
        return catAxis;
    }

    private NumericAxis<ThreadCountEntity> createNumericAxis() {
        NumericAxis<ThreadCountEntity> axis = new NumericAxis<ThreadCountEntity>();
        axis.setPosition(Chart.Position.LEFT);
        axis.addField(accessor.threadCount());
        axis.addField(accessor.peakThreadCount());
        TextSprite title = new TextSprite(appResources.getMessages().threadChartTitle());
        title.setFontSize(NumberAxisConstants.TITLE_FONT_SIZE);
        axis.setTitleConfig(title);
        axis.setMinorTickSteps(NumberAxisConstants.TICK_STEPS);
        axis.setDisplayGrid(true);
        PathSprite odd = new PathSprite();
        odd.setOpacity(NumberAxisConstants.OPACITY);
        odd.setFill(NumberAxisConstants.FILL_COLOR);
        odd.setStroke(NumberAxisConstants.STROKE_COLOR);
        odd.setStrokeWidth(NumberAxisConstants.STROKE_WIDTH);
        axis.setGridOddConfig(odd);
        return axis;
    }

    private LineSeries<ThreadCountEntity> createThreadCountLineSeries() {
        final LineSeries<ThreadCountEntity> series = new LineSeries<ThreadCountEntity>();
        series.setYAxisPosition(ThreadCountLineConstants.POSITION);
        series.setYField(accessor.threadCount());
        series.setStroke(ThreadCountLineConstants.STROKE_COLOR);
        series.setShowMarkers(true);
        series.setSmooth(true);
        series.setFill(ThreadCountLineConstants.FILL_COLOR);
        PathSprite marker = ThreadCountLineConstants.PATH_SPRITE;
        marker.setFill(ThreadCountLineConstants.FILL_COLOR);
        series.setMarkerConfig(marker);
        series.setHighlighting(true);
        return series;
    }

    private LineSeries<ThreadCountEntity> createPeakThreadCountLineSeries() {
        final LineSeries<ThreadCountEntity> series = new LineSeries<ThreadCountEntity>();
        series.setYAxisPosition(PeakThreadCountConstants.POSITION);
        series.setYField(accessor.peakThreadCount());
        series.setStroke(PeakThreadCountConstants.STROKE_COLOR);
        series.setShowMarkers(true);
        Sprite marker = PeakThreadCountConstants.PATH_SPRITE;
        marker.setFill(PeakThreadCountConstants.FILL_COLOR);
        series.setMarkerConfig(marker);
        series.setHighlighting(true);
        return series;
    }

    @SuppressWarnings("unchecked")
    private NumericAxis<ThreadCountEntity> getThreadCountAxis() {
        return (NumericAxis<ThreadCountEntity>) chart.getAxis(Chart.Position.LEFT);
    }


}