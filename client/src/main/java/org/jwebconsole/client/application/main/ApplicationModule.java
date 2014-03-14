package org.jwebconsole.client.application.main;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import org.jwebconsole.client.application.content.home.HomeModule;
import org.jwebconsole.client.application.content.main.ContentTabModule;
import org.jwebconsole.client.application.content.memory.MemoryModule;
import org.jwebconsole.client.application.content.overview.OverviewModule;
import org.jwebconsole.client.application.content.summary.SummaryModule;
import org.jwebconsole.client.application.content.thread.ThreadContentModule;
import org.jwebconsole.client.application.left.AvailableHostsModule;
import org.jwebconsole.client.application.popup.connection.ConnectionWindowModule;
import org.jwebconsole.client.application.toolbar.ToolbarModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class, ApplicationPresenter.ApplicationProxy.class);
        install(new HomeModule());
        install(new ToolbarModule());
        install(new ConnectionWindowModule());
        install(new AvailableHostsModule());
        install(new ThreadContentModule());
        install(new ContentTabModule());
        install(new MemoryModule());
        install(new SummaryModule());
        install(new OverviewModule());
    }
}
