package org.jwebconsole.client.application.content.thread;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import org.jwebconsole.client.bundle.AppResources;

import javax.inject.Inject;

public class ThreadContentViewImpl extends ViewWithUiHandlers<ThreadContentUiHandlers> implements ThreadContentView{

    private final AppResources appResources;

    interface Binder extends UiBinder<Widget, ThreadContentViewImpl> {
    }

    @Inject
    ThreadContentViewImpl(Binder uiBinder, AppResources appResources) {
        this.appResources = appResources;
        initWidget(uiBinder.createAndBindUi(this));
        init();
    }

    private void init() {

    }

}