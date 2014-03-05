package org.jwebconsole.client.application.content.summary;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SummaryModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(SummaryPresenter.class, SummaryView.class, SummaryViewImpl.class, SummaryPresenter.SummaryProxy.class);
    }
}
