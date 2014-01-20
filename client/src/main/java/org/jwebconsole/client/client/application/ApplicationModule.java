package org.jwebconsole.client.client.application;

import org.jwebconsole.client.client.application.home.HomeModule;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import org.jwebconsole.client.client.application.error.ErrorModule;
import org.jwebconsole.client.client.application.north.NorthModule;
import org.jwebconsole.client.client.application.east.EastModule;
import org.jwebconsole.client.client.application.west.WestModule;
import org.jwebconsole.client.client.application.links.LinksModule;

public class ApplicationModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new LinksModule());
    install(new WestModule());
    install(new EastModule());
    install(new NorthModule());
    install(new ErrorModule());
    install(new HomeModule());

    bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
        ApplicationPresenter.MyProxy.class);
  }
}
