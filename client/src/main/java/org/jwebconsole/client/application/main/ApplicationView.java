package org.jwebconsole.client.application.main;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.util.ContainerUtils;

import javax.inject.Inject;

public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {

    private final AppResources appResources;

    @UiField
    SimpleContainer leftPanel;
    @UiField
    SimpleContainer contentPanel;


    public interface Binder extends UiBinder<Widget, ApplicationView> {
    }

    @Inject
    public ApplicationView(Binder uiBinder, AppResources resources) {
        this.appResources = resources;
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        super.setInSlot(slot, content);
        if (slot == ApplicationPresenter.SLOT_LEFT_PANEL) {
            ContainerUtils.clearAndPut(leftPanel, content);
        }
        if (slot == ApplicationPresenter.SLOT_CONTENT_PANEL) {
            ContainerUtils.clearAndPut(contentPanel, content);
        }
    }

    @Override
    public void showContentMask() {
        contentPanel.mask(appResources.getMessages().loadingMaskText());
    }

    @Override
    public void hideContentMask() {
        contentPanel.unmask();
    }


}
