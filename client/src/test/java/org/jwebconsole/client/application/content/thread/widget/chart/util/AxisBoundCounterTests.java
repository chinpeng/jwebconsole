package org.jwebconsole.client.application.content.thread.widget.chart.util;

import org.junit.Test;
import org.jwebconsole.client.model.thread.count.ThreadCountEntity;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AxisBoundCounterTests extends Mockito {

    @Test
    public void shouldProvideDefaultMinValueWhenListIsEmpty() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        int result = axisBoundCounter.getMinAxisBound(new ArrayList<ThreadCountEntity>());
        assertEquals(result, 0);
    }

    @Test
    public void shouldProvideDefaultMaxValueWhenListIsEmpty() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        int result = axisBoundCounter.getMaxAxisBound(new ArrayList<ThreadCountEntity>());
        assertEquals(result, 50);
    }

    @Test
    public void shouldProvideDefaultMaxValueWhenListIsNull() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        int result = axisBoundCounter.getMaxAxisBound(null);
        assertEquals(result, 50);
    }

    @Test
    public void shouldProvideDefaultMinValueWhenListIsNull() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        int result = axisBoundCounter.getMinAxisBound(null);
        assertEquals(result, 0);
    }

    @Test
    public void shouldProvideMinValueWithDefaultOffset() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        List<ThreadCountEntity> items = createList(50, 60);
        int result = axisBoundCounter.getMinAxisBound(items);
        assertEquals(45, result);
    }

    @Test
    public void shouldReturnZeroMinValueWhenThreadCountIsSmall() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        List<ThreadCountEntity> items = createList(2, 60);
        int result = axisBoundCounter.getMinAxisBound(items);
        assertEquals(0, result);
    }

    @Test
    public void shouldReturnZeroMinValueWhenThreadCountIsNegative() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        List<ThreadCountEntity> items = createList(-2, 60);
        int result = axisBoundCounter.getMinAxisBound(items);
        assertEquals(0, result);
    }

    @Test
    public void shouldReturnMinimumOfSequence() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        List<ThreadCountEntity> items = createList(10, 60);
        items.addAll(createList(20, 50));
        int result = axisBoundCounter.getMinAxisBound(items);
        assertEquals(5, result);
    }

    @Test
    public void shouldReturnZeroMinWhenOneOfItemsIsSmall() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        List<ThreadCountEntity> items = createList(10, 60);
        items.addAll(createList(4, 50));
        int result = axisBoundCounter.getMinAxisBound(items);
        assertEquals(0, result);
    }

    @Test
    public void shouldReturnMaxAxisBoundWithOffset() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        List<ThreadCountEntity> items = createList(10, 60);
        int result = axisBoundCounter.getMaxAxisBound(items);
        assertEquals(65, result);
    }

    @Test
    public void shouldReturnOffsetWhenMaxValueIsSmall() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        List<ThreadCountEntity> items = createList(0, 0);
        int result = axisBoundCounter.getMaxAxisBound(items);
        assertEquals(5, result);
    }

    @Test
    public void shouldReturnOffsetWhenMaxValueIsNegative() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        List<ThreadCountEntity> items = createList(-20, -10);
        int result = axisBoundCounter.getMaxAxisBound(items);
        assertEquals(5, result);
    }

    @Test
    public void shouldChooseMaxOfPeakAndCurrentThreadCount() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        List<ThreadCountEntity> items = createList(30, 10);
        int result = axisBoundCounter.getMaxAxisBound(items);
        assertEquals(35, result);
    }

    @Test
    public void shouldChooseMaxFromSequence() {
        AxisBoundCounter axisBoundCounter = new AxisBoundCounter();
        List<ThreadCountEntity> items = createList(30, 10);
        items.addAll(createList(35, 25));
        int result = axisBoundCounter.getMaxAxisBound(items);
        assertEquals(40, result);
    }


    private List<ThreadCountEntity> createList(int threadCount, int peakThreadCount) {
        List<ThreadCountEntity> result = new ArrayList<ThreadCountEntity>();
        ThreadCountEntity entity = new ThreadCountEntity();
        entity.setThreadCount(threadCount);
        entity.setPeakThreadCount(peakThreadCount);
        result.add(entity);
        return result;
    }

}
