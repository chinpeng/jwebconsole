package org.jwebconsole.client.application.popup.connection;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;

public interface ConnectionWindowView extends View, HasUiHandlers<ConnectionWindowUiHandlers>{

    void showDialog();

    void hideDialog();
}
