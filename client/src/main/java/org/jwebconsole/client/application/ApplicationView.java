package org.jwebconsole.client.application;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.sencha.gxt.widget.core.client.ContentPanel;
import org.jwebconsole.client.bundle.AppResources;

public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {

    static {
        AppResources.INSTANCE.app().ensureInjected();
    }

    public interface Binder extends UiBinder<Widget, ApplicationView> {
    }

    @Inject
    public ApplicationView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        Window.alert(AppResources.INSTANCE.app().appToolBar());
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        super.setInSlot(slot, content);
        /*if (slot == ApplicationPresenter.SLOT_SetNorth) {
            north.setWidget(content);
        } else if (slot == ApplicationPresenter.SLOT_SetEast) {
            east.setWidget(content);
        } else if (slot == ApplicationPresenter.SLOT_SetWest) {
            west.setWidget(content);
        } else if (slot == ApplicationPresenter.SLOT_SetCenter) {
            center.setWidget(content);
        } else {

        }*/
    }
}
