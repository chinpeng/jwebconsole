package org.jwebconsole.client.application.content.home;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class HomeModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        bindPresenter(HomePresenter.class, HomeView.class, HomeViewImpl.class, HomePresenter.HomeProxy.class);
    }

}
