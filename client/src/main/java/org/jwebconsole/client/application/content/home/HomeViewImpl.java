package org.jwebconsole.client.application.content.home;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import org.jwebconsole.client.bundle.AppResources;

import javax.inject.Inject;

public class HomeViewImpl extends ViewWithUiHandlers<HomeUiHandlers> implements HomeView {

    private final AppResources appResources;

    @UiField
    TextButton createConnectionButton;

    interface Binder extends UiBinder<Widget, HomeViewImpl> {
    }

    @Inject
    HomeViewImpl(Binder uiBinder, AppResources appResources) {
        this.appResources = appResources;
        initWidget(uiBinder.createAndBindUi(this));
        init();
    }

    private void init() {
        createConnectionButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                getUiHandlers().onCreateConnectionButtonClicked();
            }
        });
    }

}