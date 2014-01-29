package org.jwebconsole.client.application.popup.connection;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.jwebconsole.client.bundle.AppResources;

public class ConnectionWindowViewImpl extends ViewWithUiHandlers<ConnectionWindowUiHandlers> implements ConnectionWindowView {

    private final AppResources resources;

    @UiField
    Window window;
    @UiField
    TextButton connectButton;
    @UiField
    TextButton cancelButton;
    @UiField
    TextField hostName;
    @UiField
    PasswordField password;
    @UiField
    TextField login;
    @UiField(provided = true)
    NumberField<Integer> port;

    interface Binder extends UiBinder<Window, ConnectionWindowViewImpl> {
    }

    @Inject
    public ConnectionWindowViewImpl(Binder binder, AppResources resources) {
        port = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
        initWidget(binder.createAndBindUi(this));
        this.resources = resources;
        init();
    }

    private void init() {
        resources.connectionWindow().ensureInjected();
        initCancelButton();
        initConnectButton();
    }

    private void initConnectButton() {
        connectButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                getUiHandlers().connectHost();
            }
        });
    }

    private void initCancelButton() {
        cancelButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                getUiHandlers().hideDialog();
            }
        });
    }


    public void showDialog() {
        window.show();
        window.setHeight(resources.connectionWindow().popupHeight());
    }

    @Override
    public void hideDialog() {
        window.hide();
    }

    @Override
    public TextButton getConnectButton() {
        return connectButton;
    }

    @Override
    public TextButton getCancelButton() {
        return cancelButton;
    }

    @Override
    public TextField getHostName() {
        return hostName;
    }

    @Override
    public PasswordField getPassword() {
        return password;
    }

    @Override
    public TextField getLogin() {
        return login;
    }

    @Override
    public NumberField<Integer> getPort() {
        return port;
    }

    @Override
    public Window getWindow() {
        return window;
    }
}