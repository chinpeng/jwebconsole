package org.jwebconsole.client.application.content.main;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;

public interface ContentTabView extends View, HasUiHandlers<ContentTabUiHandlers> {

    void setSummaryNameToken(String token);

    void setMemoryNameToken(String token);

    void setThreadsNameToken(String token);

    void applySelectionByNameToken(String token);
}

