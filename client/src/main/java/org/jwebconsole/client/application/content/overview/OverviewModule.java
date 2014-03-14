package org.jwebconsole.client.application.content.overview;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class OverviewModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(OverviewPresenter.class, OverviewView.class, OverviewViewImpl.class, OverviewPresenter.OverviewProxy.class);
    }
}
