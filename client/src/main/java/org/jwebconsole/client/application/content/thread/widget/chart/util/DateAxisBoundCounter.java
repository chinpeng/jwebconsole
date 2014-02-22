package org.jwebconsole.client.application.content.thread.widget.chart.util;

import org.jwebconsole.client.model.thread.ThreadCountEntity;

import java.util.*;

public class DateAxisBoundCounter {

    private static final Comparator<ThreadCountEntity> TIME_COMPARATOR = new Comparator<ThreadCountEntity>() {

        @Override
        public int compare(ThreadCountEntity first, ThreadCountEntity second) {
            long firstTime = first.getTime().getTime();
            long secondTime = second.getTime().getTime();
            if (firstTime < secondTime) {
                return -1;
            } else if (firstTime > secondTime) {
                return 1;
            }
            return 0;
        }
    };

    public Date getMinDate(List<ThreadCountEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return new Date();
        } else {
            return countMinDate(entities);
        }
    }

    private Date countMinDate(List<ThreadCountEntity> entities) {
        ArrayList<ThreadCountEntity> copy = new ArrayList<ThreadCountEntity>(entities);
        Collections.sort(copy, TIME_COMPARATOR);
        return copy.get(0).getTime();
    }

    public Date getMaxDate(List<ThreadCountEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return new Date();
        } else {
            return countMaxDate(entities);
        }
    }

    private Date countMaxDate(List<ThreadCountEntity> entities) {
        ArrayList<ThreadCountEntity> copy = new ArrayList<ThreadCountEntity>(entities);
        Collections.sort(copy, TIME_COMPARATOR);
        return copy.get(copy.size() - 1).getTime();
    }

}
