package org.jwebconsole.client.application.content.thread.widget.chart;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.axis.TimeAxis;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import org.jwebconsole.client.application.content.thread.widget.chart.constants.DateAxisConstants;
import org.jwebconsole.client.application.content.thread.widget.chart.constants.NumberAxisConstants;
import org.jwebconsole.client.application.content.thread.widget.chart.constants.PeakThreadCountConstants;
import org.jwebconsole.client.application.content.thread.widget.chart.constants.ThreadCountLineConstants;
import org.jwebconsole.client.application.content.thread.widget.chart.model.ThreadCountEntityPropertyAccessor;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.model.thread.count.ThreadCountEntity;

import javax.inject.Inject;
import java.util.Date;

public class ThreadCountChartViewImpl extends ViewWithUiHandlers<ThreadCountChartUiHandlers> implements ThreadCountChartView {

    private static final ThreadCountEntityPropertyAccessor accessor = GWT.create(ThreadCountEntityPropertyAccessor.class);
    public static final int DEFAULT_INSETS = 20;

    private AppResources appResources;
    private ListStore<ThreadCountEntity> store;
    private Chart<ThreadCountEntity> chart;
    private Integer autoIncrementId = 0;

    @UiField
    SimpleContainer chartPanel;

    interface Binder extends UiBinder<Widget, ThreadCountChartViewImpl> {
    }

    @Inject
    ThreadCountChartViewImpl(Binder uiBinder, AppResources appResources) {
        this.appResources = appResources;
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
    public void setMinDate(Date date) {
        getTimeAxis().setStartDate(date);
    }

    @Override
    public void setMaxDate(Date date) {
        getTimeAxis().setEndDate(date);
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

    private void init() {
        chart = new Chart<ThreadCountEntity>();
        chart.setShadowChart(true);
        store = new ListStore<ThreadCountEntity>(createKeyProvider());
        chart.setStore(store);
        chart.addAxis(createNumericAxis());
        chart.addAxis(createDateAxis());
        chart.addSeries(createPeakThreadCountLineSeries());
        chart.addSeries(createThreadCountLineSeries());
        chart.setLegend(createLegend());
        chart.setAnimated(true);
        chart.hide();
        chart.setDefaultInsets(DEFAULT_INSETS);
        chartPanel.add(chart);
    }

    private ModelKeyProvider<ThreadCountEntity> createKeyProvider() {
        return new ModelKeyProvider<ThreadCountEntity>() {
            @Override
            public String getKey(ThreadCountEntity threadCountEntity) {
                return incrementAndGetGeneratedId();
            }
        };
    }

    private String incrementAndGetGeneratedId() {
        autoIncrementId++;
        return autoIncrementId.toString();
    }

    private Legend<ThreadCountEntity> createLegend() {
        final Legend<ThreadCountEntity> legend = new Legend<ThreadCountEntity>();
        legend.setPosition(Chart.Position.RIGHT);
        legend.setItemHighlighting(true);
        legend.setItemHiding(true);
        return legend;
    }

    private TimeAxis<ThreadCountEntity> createDateAxis() {
        TimeAxis<ThreadCountEntity> dateAxis = new TimeAxis<ThreadCountEntity>();
        dateAxis.setPosition(Chart.Position.BOTTOM);
        dateAxis.setField(accessor.time());
        TextSprite title = new TextSprite(appResources.getMessages().chartTimeAxisTitle());
        title.setFontSize(DateAxisConstants.TITLE_FONT_SIZE);
        dateAxis.setTitleConfig(title);
        dateAxis.setLabelProvider(new LabelProvider<Date>() {
            @Override
            public String getLabel(Date item) {
                DateTimeFormat format = DateAxisConstants.DATE_FORMAT;
                return format.format(item);
            }
        });
        dateAxis.setLabelStepRatio(DateAxisConstants.STEP_RATIO);
        return dateAxis;
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
        axis.setDisplayGrid(false);
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
        series.setMarkerIndex(1);
        series.setSmooth(true);
        series.setFill(ThreadCountLineConstants.FILL_COLOR);
        Sprite marker = ThreadCountLineConstants.PATH_SPRITE;
        marker.setFill(ThreadCountLineConstants.FILL_COLOR);
        series.setMarkerConfig(marker);
        series.setHighlighting(true);
        series.setLegendTitle(appResources.getMessages().threadCountLineSeriesTitle());
        return series;
    }

    private LineSeries<ThreadCountEntity> createPeakThreadCountLineSeries() {
        final LineSeries<ThreadCountEntity> series = new LineSeries<ThreadCountEntity>();
        series.setYAxisPosition(PeakThreadCountConstants.POSITION);
        series.setYField(accessor.peakThreadCount());
        series.setStroke(PeakThreadCountConstants.STROKE_COLOR);
        series.setShowMarkers(true);
        series.setMarkerIndex(1);
        Sprite marker = PeakThreadCountConstants.PATH_SPRITE;
        marker.setFill(PeakThreadCountConstants.FILL_COLOR);
        series.setMarkerConfig(marker);
        series.setHighlighting(true);
        series.setLegendTitle(appResources.getMessages().peakThreadCountLineSeriesTitle());
        return series;
    }

    @SuppressWarnings("unchecked")
    private NumericAxis<ThreadCountEntity> getThreadCountAxis() {
        return (NumericAxis<ThreadCountEntity>) chart.getAxis(Chart.Position.LEFT);
    }

    @SuppressWarnings("unchecked")
    private TimeAxis<ThreadCountEntity> getTimeAxis() {
        return (TimeAxis<ThreadCountEntity>) chart.getAxis(Chart.Position.BOTTOM);
    }


}