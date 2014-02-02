package org.jwebconsole.client.application.left;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AvailableHostsModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(AvailableHostsPresenter.class, AvailableHostsView.class, AvailableHostsViewImpl.class, AvailableHostsPresenter.MyProxy.class);
    }
}
