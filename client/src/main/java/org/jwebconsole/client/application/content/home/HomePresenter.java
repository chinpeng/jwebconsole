package org.jwebconsole.client.application.content.home;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.event.popup.RevealAddConnectionPopupEvent;
import org.jwebconsole.client.place.NameTokens;

public class HomePresenter extends Presenter<HomeView, HomePresenter.HomeProxy> implements HomeUiHandlers {

    @Inject
    public HomePresenter(EventBus eventBus, HomeView view, HomeProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_CONTENT_PANEL);
        init();
    }

    private void init() {
        getView().setUiHandlers(this);
    }

    @Override
    public void onCreateConnectionButtonClicked() {
        getEventBus().fireEvent(new RevealAddConnectionPopupEvent());
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.home)
    public interface HomeProxy extends ProxyPlace<HomePresenter> {

    }

}
