package org.jwebconsole.client.application.content.thread.model;

import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import org.jwebconsole.client.model.thread.info.ThreadInfoEntity;

public interface ThreadInfoPropertyAccessor extends PropertyAccess<ThreadInfoEntity> {

    ValueProvider<ThreadInfoEntity, Integer> id();

    ValueProvider<ThreadInfoEntity, Long> threadId();

    ValueProvider<ThreadInfoEntity, String> hostId();

    ValueProvider<ThreadInfoEntity, String> name();

    @Editor.Path("id")
    ModelKeyProvider<ThreadInfoEntity> key();

}
