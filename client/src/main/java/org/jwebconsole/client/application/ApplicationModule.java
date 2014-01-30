package org.jwebconsole.client.application;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import org.jwebconsole.client.application.popup.connection.ConnectionWindowModule;
import org.jwebconsole.client.application.toolbar.ToolbarModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new ToolbarModule());
        install(new ConnectionWindowModule());
        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
                ApplicationPresenter.ApplicationProxy.class);
    }
}
