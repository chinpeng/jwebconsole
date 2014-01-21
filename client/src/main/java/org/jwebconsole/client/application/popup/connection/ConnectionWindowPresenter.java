package org.jwebconsole.client.application.popup.connection;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.jwebconsole.client.application.ApplicationPresenter;
import org.jwebconsole.client.event.popup.RevealConnectionPopupEvent;
import org.jwebconsole.client.event.popup.RevealConnectionPopupEventHandler;
import org.jwebconsole.client.place.NameTokens;

public class ConnectionWindowPresenter extends Presenter<ConnectionWindowView, ConnectionWindowPresenter.ConnectionWindowProxy> implements
        ConnectionWindowUiHandlers,
        RevealConnectionPopupEventHandler{

    @Inject
    public ConnectionWindowPresenter(EventBus eventBus, ConnectionWindowView view, ConnectionWindowProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_POPUP);
        view.setUiHandlers(this);
    }

    @Override
    protected void onBind() {
        super.onBind();
        getView().showDialog();
    }

    @ProxyEvent
    @Override
    public void onRevealEvent(RevealConnectionPopupEvent event) {
        forceReveal();
    }

    @Override
    public void hideDialog() {
        getView().hideDialog();
        unbind();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.newConnection)
    public interface ConnectionWindowProxy extends Proxy<ConnectionWindowPresenter> {

    }

}
