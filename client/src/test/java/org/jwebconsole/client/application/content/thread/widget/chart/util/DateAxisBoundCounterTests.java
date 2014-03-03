package org.jwebconsole.client.application.content.thread.widget.chart.util;

import org.junit.Before;
import org.junit.Test;
import org.jwebconsole.client.model.thread.count.ThreadCountEntity;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateAxisBoundCounterTests extends Mockito {

    private Date mockDate;

    @Before
    public void init() {
        this.mockDate = mock(Date.class);
    }

    @Test
    public void shouldReturnDefaultMinDateWhenListIsEmpty() {
        DateAxisBoundCounter dateAxisBoundCounter = new DateAxisBoundCounter(mockDate);
        Date result = dateAxisBoundCounter.getMinDate(new ArrayList<ThreadCountEntity>());
        assertEquals(result, mockDate);
    }

    @Test
    public void shouldReturnDefaultMinDateWhenListIsNull() {
        DateAxisBoundCounter dateAxisBoundCounter = new DateAxisBoundCounter(mockDate);
        Date result = dateAxisBoundCounter.getMinDate(null);
        assertEquals(result, mockDate);
    }

    @Test
    public void shouldReturnDefaultMaxDateWhenListIsEmpty() {
        DateAxisBoundCounter dateAxisBoundCounter = new DateAxisBoundCounter(mockDate);
        Date result = dateAxisBoundCounter.getMaxDate(new ArrayList<ThreadCountEntity>());
        assertEquals(result, mockDate);
    }

    @Test
    public void shouldReturnDefaultMaxDateWhenListIsNull() {
        DateAxisBoundCounter dateAxisBoundCounter = new DateAxisBoundCounter(mockDate);
        Date result = dateAxisBoundCounter.getMaxDate(null);
        assertEquals(result, mockDate);
    }

    @Test
    public void shouldReturnMinDate() {
        DateAxisBoundCounter dateAxisBoundCounter = new DateAxisBoundCounter();
        ThreadCountEntity first = createEntityFromDate(new Date(new Date().getTime() - 2));
        ThreadCountEntity second = createEntityFromDate(new Date());
        Date result = dateAxisBoundCounter.getMinDate(Arrays.asList(first, second));
        assertEquals(result, first.getTime());
    }

    @Test
    public void shouldReturnMaxDate() {
        DateAxisBoundCounter dateAxisBoundCounter = new DateAxisBoundCounter();
        ThreadCountEntity first = createEntityFromDate(new Date(new Date().getTime() - 2));
        ThreadCountEntity second = createEntityFromDate(new Date());
        Date result = dateAxisBoundCounter.getMaxDate(Arrays.asList(first, second));
        assertEquals(result, second.getTime());
    }

    public ThreadCountEntity createEntityFromDate(Date date) {
        ThreadCountEntity threadCountEntity = new ThreadCountEntity();
        threadCountEntity.setTime(date);
        return threadCountEntity;
    }

}
