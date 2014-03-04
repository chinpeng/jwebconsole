package org.jwebconsole.client.application.content.main;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ContentTabModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ContentTabPresenter.class, ContentTabView.class, ContentTabViewImpl.class, ContentTabPresenter.ContentTabProxy.class);
    }
}
