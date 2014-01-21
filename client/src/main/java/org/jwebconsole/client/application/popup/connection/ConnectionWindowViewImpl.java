package org.jwebconsole.client.application.popup.connection;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

public class ConnectionWindowViewImpl extends ViewWithUiHandlers<ConnectionWindowUiHandlers> implements ConnectionWindowView {

    @UiField
    Window window;

    interface Binder extends UiBinder<HTMLPanel, ConnectionWindowViewImpl> {
    }

    @Inject
    public ConnectionWindowViewImpl(Binder binder) {
        HTMLPanel rootElement = binder.createAndBindUi(this);
    }

    public void showDialog() {
        window.show();
        window.getHideButton().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                getUiHandlers().hideDialog();
            }
        });
    }

    @Override
    public void hideDialog() {
        window.hide();
    }

}