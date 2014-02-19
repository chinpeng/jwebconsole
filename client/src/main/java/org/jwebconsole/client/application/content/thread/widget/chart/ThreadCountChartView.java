package org.jwebconsole.client.application.content.thread.widget.chart;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;
import org.jwebconsole.client.model.thread.ThreadCountEntity;

public interface ThreadCountChartView extends View, HasUiHandlers<ThreadCountChartUiHandlers> {

    void setMinThreadAxis(Integer value);

    void setMaxThreadAxis(Integer value);

    void refreshChart();

    void addThreadCountEntity(ThreadCountEntity threadCountEntity);

    void clear();

}
