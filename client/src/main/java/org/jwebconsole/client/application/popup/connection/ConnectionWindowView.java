package org.jwebconsole.client.application.popup.connection;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

public interface ConnectionWindowView extends View, HasUiHandlers<ConnectionWindowUiHandlers> {

    void showDialog();

    void hideDialog();


    TextField getHostName();

    PasswordField getPassword();

    TextField getLogin();

    NumberField<Integer> getPort();

    Window getWindow();
}
