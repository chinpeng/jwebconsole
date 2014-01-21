package org.jwebconsole.client.application.popup.connection;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ConnectionWindowModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        bindPresenter(ConnectionWindowPresenter.class, ConnectionWindowView.class, ConnectionWindowViewImpl.class, ConnectionWindowPresenter.ConnectionWindowProxy.class);
    }
}
