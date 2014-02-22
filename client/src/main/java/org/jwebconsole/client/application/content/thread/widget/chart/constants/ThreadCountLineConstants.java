package org.jwebconsole.client.application.content.thread.widget.chart.constants;

import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;

public class ThreadCountLineConstants {
    public static final RGB FILL_COLOR = new RGB(32, 68, 186);
    public static final RGB STROKE_COLOR = new RGB(32, 68, 186);
    public static final Sprite PATH_SPRITE = Primitives.diamond(0, 0, 6);
    public static final Chart.Position POSITION = Chart.Position.LEFT;
}

