package org.jwebconsole.client.application.content.thread.widget.chart;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;
import org.jwebconsole.client.model.thread.count.ThreadCountEntity;

import java.util.Date;

public interface ThreadCountChartView extends View, HasUiHandlers<ThreadCountChartUiHandlers> {

    void setMinThreadAxis(Integer value);

    void setMaxThreadAxis(Integer value);

    void refreshChart();

    void addThreadCountEntity(ThreadCountEntity threadCountEntity);

    void clearChart();

    void mask(String message);

    void unmask();

    void showChart();

    void setMinDate(Date date);

    void setMaxDate(Date date);

}
