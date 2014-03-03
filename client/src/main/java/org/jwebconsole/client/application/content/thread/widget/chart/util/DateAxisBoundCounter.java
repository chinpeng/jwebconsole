package org.jwebconsole.client.application.content.thread.widget.chart.util;

import org.jwebconsole.client.model.thread.count.ThreadCountEntity;

import java.util.*;

public class DateAxisBoundCounter {

    private Date defaultDate;

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

    public DateAxisBoundCounter() {

    }

    public DateAxisBoundCounter(Date defaultDate) {
        this.defaultDate = defaultDate;
    }

    public Date getMinDate(List<ThreadCountEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return getDefaultDate();
        } else {
            return countMinDate(entities);
        }
    }

    public Date getMaxDate(List<ThreadCountEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return getDefaultDate();
        } else {
            return countMaxDate(entities);
        }
    }

    private Date countMinDate(List<ThreadCountEntity> entities) {
        ArrayList<ThreadCountEntity> copy = new ArrayList<ThreadCountEntity>(entities);
        Collections.sort(copy, TIME_COMPARATOR);
        return copy.get(0).getTime();
    }

    private Date countMaxDate(List<ThreadCountEntity> entities) {
        ArrayList<ThreadCountEntity> copy = new ArrayList<ThreadCountEntity>(entities);
        Collections.sort(copy, TIME_COMPARATOR);
        return copy.get(copy.size() - 1).getTime();
    }

    private Date getDefaultDate() {
        if (defaultDate == null) {
            return new Date();
        }
        return defaultDate;
    }

}
