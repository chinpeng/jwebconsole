package org.jwebconsole.client.application.content.thread.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.model.thread.ThreadCountEntity;

import java.util.Date;
import java.util.List;

public class ThreadCountChartWidget implements IsWidget {

    private static final ThreadCountEntityPropertyAccessor accessor = GWT.create(ThreadCountEntityPropertyAccessor.class);

    private AppResources resources;
    private ListStore<ThreadCountEntity> store;
    private Chart<ThreadCountEntity> chart;


    @Inject
    public ThreadCountChartWidget(AppResources resources) {
        this.resources = resources;
    }

    public void init() {
        chart = new Chart<ThreadCountEntity>();
        chart.setShadowChart(true);
        store = new ListStore<ThreadCountEntity>(accessor.nameKey());
        chart.setStore(store);
        chart.addAxis(createNumericAxis());
        chart.addAxis(createDateAxis());
        chart.addSeries(createThreadCountLineSeries());
        chart.addSeries(createPeakThreadCountLineSeries());
        chart.setWidth(900);
        chart.setHeight(600);
    }

    private CategoryAxis<ThreadCountEntity, Date> createDateAxis() {
        CategoryAxis<ThreadCountEntity, Date> catAxis = new CategoryAxis<ThreadCountEntity, Date>();
        catAxis.setPosition(Chart.Position.BOTTOM);
        catAxis.setField(accessor.time());
        TextSprite title = new TextSprite("Time");
        title.setFontSize(18);
        catAxis.setTitleConfig(title);
        catAxis.setLabelProvider(new LabelProvider<Date>() {
            @Override
            public String getLabel(Date item) {
                DateTimeFormat format = DateTimeFormat.getFormat("HH:mm:SS");
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
        TextSprite title = new TextSprite("Number of threads");
        title.setFontSize(18);
        axis.setTitleConfig(title);
        axis.setMinorTickSteps(1);
        axis.setDisplayGrid(true);
        PathSprite odd = new PathSprite();
        odd.setOpacity(1);
        odd.setFill(new Color("#ddd"));
        odd.setStroke(new Color("#bbb"));
        odd.setStrokeWidth(0.5);
        axis.setGridOddConfig(odd);
        axis.setMinimum(0);
        axis.setMaximum(100);
        return axis;
    }

    private LineSeries<ThreadCountEntity> createThreadCountLineSeries() {
        final LineSeries<ThreadCountEntity> series = new LineSeries<ThreadCountEntity>();
        series.setYAxisPosition(Chart.Position.LEFT);
        series.setYField(accessor.threadCount());
        series.setStroke(new RGB(32, 68, 186));
        series.setShowMarkers(true);
        series.setSmooth(true);
        series.setFill(new RGB(32, 68, 186));
        PathSprite marker = Primitives.diamond(0, 0, 6);
        marker.setFill(new RGB(32, 68, 186));
        series.setMarkerConfig(marker);
        series.setHighlighting(true);
        return series;
    }

    private LineSeries<ThreadCountEntity> createPeakThreadCountLineSeries() {
        final LineSeries<ThreadCountEntity> series = new LineSeries<ThreadCountEntity>();
        series.setYAxisPosition(Chart.Position.LEFT);
        series.setYField(accessor.peakThreadCount());
        series.setStroke(new RGB(194, 0, 36));
        series.setShowMarkers(true);
        Sprite marker = Primitives.square(0, 0, 6);
        marker.setFill(new RGB(194, 0, 36));
        series.setMarkerConfig(marker);
        series.setHighlighting(true);
        return series;
    }


    @Override
    public Widget asWidget() {
        return chart;
    }

    public void populate(List<ThreadCountEntity> entities) {
        store.clear();
        store.addAll(entities.subList(entities.size() - 1 - 10, entities.size() - 1));
        chart.redrawChart();
    }
}
