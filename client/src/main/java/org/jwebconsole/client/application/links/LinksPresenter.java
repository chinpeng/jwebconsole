package org.jwebconsole.client.application.links;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.annotations.NameToken;
import org.jwebconsole.client.application.ApplicationPresenter;
import org.jwebconsole.client.place.NameTokens;

public class LinksPresenter extends Presenter<LinksPresenter.MyView, LinksPresenter.MyProxy> {
  interface MyView extends View {
  }

  @NameToken(NameTokens.links)
  @ProxyStandard
  public interface MyProxy extends ProxyPlace<LinksPresenter> {
  }

  @Inject
  public LinksPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetCenter);

  }

}
