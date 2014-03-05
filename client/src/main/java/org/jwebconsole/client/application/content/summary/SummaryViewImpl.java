package org.jwebconsole.client.application.content.summary;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import org.jwebconsole.client.bundle.AppResources;

import javax.inject.Inject;


public class SummaryViewImpl extends ViewImpl implements SummaryView {
    private final AppResources appResources;

    interface Binder extends UiBinder<Widget, SummaryViewImpl> {
    }

    @Inject
    SummaryViewImpl(Binder uiBinder, AppResources appResources) {
        this.appResources = appResources;
        initWidget(uiBinder.createAndBindUi(this));
    }
}
