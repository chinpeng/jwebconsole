package org.jwebconsole.client.application.content.thread.widget.chart.util;

import com.google.gwt.user.datepicker.client.CalendarUtil;
import org.jwebconsole.client.model.thread.ThreadCountEntity;

import java.util.Date;
import java.util.List;

public class DefaultValuesFiller {

    public void fillWithDefaultValues(int toSize, List<ThreadCountEntity> entities) {
        int amount = toSize - entities.size();
        if (amount > 0) {
            fillWithAmount(amount, entities);
        }
    }

    private void fillWithAmount(int amount, List<ThreadCountEntity> entities) {
        for (int i = 0; i < amount; i++) {
            entities.add(0, new ThreadCountEntity(i, "", getDateWithoutTime(), 0, 0));
        }
    }

    private Date getDateWithoutTime() {
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }


}
