package org.jwebconsole.client.application.content.memory;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import org.jwebconsole.client.bundle.AppResources;

public class MemoryViewImpl extends ViewWithUiHandlers<MemoryUiHandlers> implements MemoryView {

    private final AppResources appResources;

    interface Binder extends UiBinder<Widget, MemoryViewImpl> {
    }

    @Inject
    MemoryViewImpl(Binder uiBinder, AppResources appResources) {
        this.appResources = appResources;
        initWidget(uiBinder.createAndBindUi(this));
    }


}