package org.jwebconsole.client.application.content.thread;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;
import org.jwebconsole.client.model.thread.details.ThreadDetailsEntity;
import org.jwebconsole.client.model.thread.info.ThreadInfoEntity;

import java.util.List;

public interface ThreadContentView extends View, HasUiHandlers<ThreadContentUiHandlers> {

    void fillThreads(List<ThreadInfoEntity> entities);

    void fillThreadDetails(List<ThreadDetailsEntity> entities);

    void clearStackTracePanel();

    void setSelection(ThreadInfoEntity thread);
}
