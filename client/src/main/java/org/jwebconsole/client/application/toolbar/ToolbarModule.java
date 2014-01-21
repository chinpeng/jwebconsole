package org.jwebconsole.client.application.toolbar;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ToolbarModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        bindPresenter(ToolbarPresenter.class, ToolbarView.class, ToolbarViewImpl.class, ToolbarPresenter.ToolbarProxy.class);
    }

}
