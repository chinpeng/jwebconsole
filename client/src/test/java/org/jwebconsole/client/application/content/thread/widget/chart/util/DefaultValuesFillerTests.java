package org.jwebconsole.client.application.content.thread.widget.chart.util;

import org.junit.Test;
import org.jwebconsole.client.model.thread.count.ThreadCountEntity;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DefaultValuesFillerTests extends Mockito {

    @Test
    public void shouldNotFillIfFillSizeIsEmpty() {
        DefaultValuesFiller filler = new DefaultValuesFiller();
        ArrayList<ThreadCountEntity> source = new ArrayList<ThreadCountEntity>();
        filler.fillWithDefaultValues(0, source);
        assertEquals(source.size(), 0);
    }

    @Test
    public void shouldNotFillIfFillSizeMatchesListSize() {
        DefaultValuesFiller filler = new DefaultValuesFiller();
        ArrayList<ThreadCountEntity> source = new ArrayList<ThreadCountEntity>();
        source.add(new ThreadCountEntity());
        filler.fillWithDefaultValues(1, source);
        assertEquals(source.size(), 1);
    }

    @Test
    public void shouldNotFillIfFillSizeLessThenListSize() {
        DefaultValuesFiller filler = new DefaultValuesFiller();
        ArrayList<ThreadCountEntity> source = new ArrayList<ThreadCountEntity>();
        source.add(new ThreadCountEntity());
        source.add(new ThreadCountEntity());
        filler.fillWithDefaultValues(1, source);
        assertEquals(source.size(), 2);
    }

    @Test
    public void shouldFill() {
        DefaultValuesFiller filler = new DefaultValuesFiller();
        ArrayList<ThreadCountEntity> source = new ArrayList<ThreadCountEntity>();
        source.add(new ThreadCountEntity());
        source.add(new ThreadCountEntity());
        filler.fillWithDefaultValues(3, source);
        assertEquals(source.size(), 3);
    }

    @Test
    public void shouldInsertToHeadOfList() {
        DefaultValuesFiller filler = new DefaultValuesFiller();
        ArrayList<ThreadCountEntity> source = new ArrayList<ThreadCountEntity>();
        ThreadCountEntity entity = new ThreadCountEntity();
        source.add(entity);
        filler.fillWithDefaultValues(2, source);
        assertEquals(source.get(1), entity);
    }

    @Test
    public void shouldFillThreadCountWithZeroes() {
        DefaultValuesFiller filler = new DefaultValuesFiller();
        ArrayList<ThreadCountEntity> source = new ArrayList<ThreadCountEntity>();
        source.add(new ThreadCountEntity());
        filler.fillWithDefaultValues(2, source);
        assertEquals(source.get(0).getThreadCount().intValue(), 0);
    }

    @Test
     public void shouldFillPeakThreadCountWithZeroes() {
        DefaultValuesFiller filler = new DefaultValuesFiller();
        ArrayList<ThreadCountEntity> source = new ArrayList<ThreadCountEntity>();
        source.add(new ThreadCountEntity());
        filler.fillWithDefaultValues(2, source);
        assertEquals(source.get(0).getPeakThreadCount().intValue(), 0);
    }

    @Test
    public void shouldDateWithoutTime() {
        DefaultValuesFiller filler = new DefaultValuesFiller();
        ArrayList<ThreadCountEntity> source = new ArrayList<ThreadCountEntity>();
        source.add(new ThreadCountEntity());
        filler.fillWithDefaultValues(2, source);
        Date date = source.get(0).getTime();
        assertEquals(date.getHours(), 0);
        assertEquals(date.getMinutes(), 0);
        assertEquals(date.getSeconds(), 0);
    }

}
