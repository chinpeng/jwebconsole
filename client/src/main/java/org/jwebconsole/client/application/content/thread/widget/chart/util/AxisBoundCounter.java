package org.jwebconsole.client.application.content.thread.widget.chart.util;

import org.jwebconsole.client.model.thread.ThreadCountEntity;

import java.util.List;

public class AxisBoundCounter {

    private static final Integer DEFAULT_MIN_VALUE = 0;
    private static final Integer DEFAULT_MAX_VALUE = 50;
    private static final Integer DEFAULT_OFFSET_INCREMENT = 5;

    public Integer getMinAxisBound(List<ThreadCountEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return DEFAULT_MIN_VALUE;
        } else {
            return countNonEmptyMinValue(entities);
        }
    }

    public Integer getMaxAxisBound(List<ThreadCountEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return DEFAULT_MAX_VALUE;
        } else {
            return countNonEmptyMaxValue(entities);
        }
    }

    private Integer countNonEmptyMaxValue(List<ThreadCountEntity> entities) {
        int maxValue = 0;
        for (ThreadCountEntity threadCountEntity : entities) {
            if (threadCountEntity.getThreadCount() > maxValue || threadCountEntity.getPeakThreadCount() > maxValue) {
                maxValue = Math.max(threadCountEntity.getThreadCount(), threadCountEntity.getPeakThreadCount());
            }
        }
        return maxValue + DEFAULT_OFFSET_INCREMENT;
    }

    private Integer countNonEmptyMinValue(List<ThreadCountEntity> entities) {
        int minValue = Integer.MAX_VALUE;
        for (ThreadCountEntity threadCountEntity : entities) {
            if (threadCountEntity.getThreadCount() < minValue) {
                minValue = threadCountEntity.getThreadCount();
            }
        }
        return Math.max(0, minValue - DEFAULT_OFFSET_INCREMENT);
    }

}
