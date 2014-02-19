package org.jwebconsole.client.application.content.thread.widget.chart.model;

import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import org.jwebconsole.client.model.thread.ThreadCountEntity;

import java.util.Date;

public interface ThreadCountEntityPropertyAccessor extends PropertyAccess<ThreadCountEntity> {

    ValueProvider<ThreadCountEntity, Integer> threadCount();

    ValueProvider<ThreadCountEntity, Integer> peakThreadCount();

    ValueProvider<ThreadCountEntity, Date> time();

    ValueProvider<ThreadCountEntity, Integer> id();

    @Editor.Path("id")
    ModelKeyProvider<ThreadCountEntity> nameKey();

}
