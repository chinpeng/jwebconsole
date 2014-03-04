package org.jwebconsole.client.application.content.main;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import javax.inject.Inject;


public class ContentTabViewImpl extends ViewWithUiHandlers<ContentTabUiHandlers> implements ContentTabPresenter.MyView {
    interface Binder extends UiBinder<Widget, ContentTabViewImpl> {
    }


    @Inject
    ContentTabViewImpl(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {

    }
}
