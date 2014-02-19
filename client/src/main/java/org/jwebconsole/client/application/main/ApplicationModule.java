package org.jwebconsole.client.application.main;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import org.jwebconsole.client.application.content.thread.ThreadContentModule;
import org.jwebconsole.client.application.left.AvailableHostsModule;
import org.jwebconsole.client.application.popup.connection.ConnectionWindowModule;
import org.jwebconsole.client.application.toolbar.ToolbarModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class, ApplicationPresenter.ApplicationProxy.class);
        install(new ToolbarModule());
        install(new ConnectionWindowModule());
        install(new AvailableHostsModule());
        install(new ThreadContentModule());
    }
}
