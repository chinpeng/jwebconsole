package org.jwebconsole.client.application.toolbar;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class ToolbarViewImpl extends ViewWithUiHandlers<ToolbarUiHandlers> implements ToolbarView {

    @UiField
    ToolBar toolbar;

    @UiField
    TextButton createConnectionButton;
    @UiField
    TextButton editConnectionButton;
    @UiField
    TextButton deleteConnectionButton;

    @Override
    public void disableEditButtons() {
        editConnectionButton.disable();
        deleteConnectionButton.disable();
    }

    @Override
    public void enableEditButtons() {
        editConnectionButton.enable();
        deleteConnectionButton.enable();
    }

    interface Binder extends UiBinder<Widget, ToolbarViewImpl> {
    }

    @Inject
    public ToolbarViewImpl(Binder binder) {
        initWidget(binder.createAndBindUi(this));
        init();
    }

    private void init() {
        initCreateButton();
        initDeleteButton();
    }

    private void initDeleteButton() {
        deleteConnectionButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {

            }
        });
    }

    private void initCreateButton() {
        createConnectionButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                getUiHandlers().openConnectionWindow();
            }
        });
    }


}