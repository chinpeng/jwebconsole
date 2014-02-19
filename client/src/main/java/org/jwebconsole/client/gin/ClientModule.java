package org.jwebconsole.client.gin;

import org.jwebconsole.client.application.main.ApplicationModule;
import org.jwebconsole.client.place.NameTokens;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.client.proxy.DefaultPlaceManager;

/**
 * See more on setting up the PlaceManager on <a
 * href="// See more on: https://github.com/ArcBees/GWTP/wiki/PlaceManager">DefaultModule's > DefaultPlaceManager</a>
 */
public class ClientModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new DefaultModule(DefaultPlaceManager.class));
    install(new ApplicationModule());

    bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);
    bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.error);
    bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.error);
  }
}
