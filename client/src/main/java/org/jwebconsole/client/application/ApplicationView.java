package org.jwebconsole.client.application;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.sencha.gxt.widget.core.client.ContentPanel;
import org.jwebconsole.client.bundle.AppStyles;

import javax.inject.Inject;

public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {

    @UiField
    HTMLPanel toolbar;
    @UiField
    HTMLPanel leftPanel;
    @UiField
    ContentPanel contentPanel;

    public interface Binder extends UiBinder<Widget, ApplicationView> {
    }

    @Inject
    public ApplicationView(Binder uiBinder, AppStyles resources) {
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
        if (slot == ApplicationPresenter.SLOT_CONTENT_PANEL) {
            contentPanel.clear();
            contentPanel.add(content);
        }
    }
}
