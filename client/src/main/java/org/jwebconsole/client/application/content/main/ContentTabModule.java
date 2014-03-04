package org.jwebconsole.client.application.content.main;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ContentTabModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ContentTabPresenter.class, ContentTabPresenter.MyView.class, ContentTabPresenterView.class, ContentTabPresenter.ContentTabProxy.class);
    }
}
