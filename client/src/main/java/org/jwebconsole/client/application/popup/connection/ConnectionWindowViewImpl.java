package org.jwebconsole.client.application.popup.connection;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import org.jwebconsole.client.bundle.AppResources;

public class ConnectionWindowViewImpl extends ViewWithUiHandlers<ConnectionWindowUiHandlers> implements ConnectionWindowView {

    @UiField
    Window window;
    @UiField
    TextButton connectButton;
    @UiField
    TextButton cancelButton;

    interface Binder extends UiBinder<Window, ConnectionWindowViewImpl> {
    }

    @Inject
    public ConnectionWindowViewImpl(Binder binder, AppResources resources) {
        initWidget(binder.createAndBindUi(this));
        resources.connectionWindow().ensureInjected();
        init();
    }

    private void init() {
        initCancelButton();
        initConnectButton();
    }

    private void initConnectButton() {
        connectButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                com.google.gwt.user.client.Window.alert("connected!");
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
    }

    @Override
    public void hideDialog() {
        window.hide();
    }

}