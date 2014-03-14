package org.jwebconsole.client.application.content.thread;

import com.gwtplatform.mvp.client.UiHandlers;
import org.jwebconsole.client.model.thread.info.ThreadInfoEntity;

public interface ThreadContentUiHandlers extends UiHandlers {

    void onThreadSelected(ThreadInfoEntity thread);

}
