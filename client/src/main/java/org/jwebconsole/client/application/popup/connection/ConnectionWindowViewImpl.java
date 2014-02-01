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
import org.jwebconsole.client.bundle.messages.Messages;

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

    private Messages appMessages;

    @Inject
    public ConnectionWindowViewImpl(Binder binder, AppResources resources, Messages messages) {
        this.appMessages = messages;
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
    public void clearFields() {
        hostName.clear();
        port.clear();
        login.clear();
        password.clear();
    }

    @Override
    public void showLoadingMask() {
        window.mask(appMessages.loadingMaskText());
    }

    @Override
    public boolean isFieldsValid() {
        return hostName.validate() && port.validate();
    }

    @Override
    public void hideMask() {
        window.unmask();
    }

    @Override
    public String getHostName() {
        return hostName.getValue();
    }

    @Override
    public Integer getPort() {
        return port.getValue();
    }

    @Override
    public String getLogin() {
        return login.getValue();
    }

    @Override
    public String getPassword() {
        return password.getValue();
    }
}
