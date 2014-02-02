package org.jwebconsole.client.application;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import org.jwebconsole.client.bundle.AppResources;

import javax.inject.Inject;

public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {

    @UiField
    HTMLPanel toolbar;
    @UiField
    HTMLPanel rightPanel;
    @UiField
    HTMLPanel leftPanel;

    public interface Binder extends UiBinder<Widget, ApplicationView> {
    }

    @Inject
    public ApplicationView(Binder uiBinder, AppResources resources) {
        resources.app().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        super.setInSlot(slot, content);
        if (slot == ApplicationPresenter.SLOT_TOOLBAR) {
            toolbar.clear();
            toolbar.add(content);
        }
        if (slot == ApplicationPresenter.SLOT_LEFT_PANEL) {
            leftPanel.clear();
            leftPanel.add(content);
        }
    }
}
