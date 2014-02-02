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

    void clearFields();

    void showLoadingMask();

    boolean isFieldsValid();

    void hideMask();

    String getHostName();

    Integer getPort();

    String getLogin();

    String getPassword();

    void markPortInvalid(String message);

    void markHostInvalid(String message);

}
