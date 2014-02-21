package org.jwebconsole.client.application.content.thread.widget.chart.util;

import org.jwebconsole.client.model.thread.ThreadCountEntity;

import java.util.List;

public class AxisBoundCounter {

    public Integer getMinAxisBound(List<ThreadCountEntity> entities) {
        return 0;
    }

    public Integer getMaxAxisBound(List<ThreadCountEntity> entities) {
        return 100;
    }

}
