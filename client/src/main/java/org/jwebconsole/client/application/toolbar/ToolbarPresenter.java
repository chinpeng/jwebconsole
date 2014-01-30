package org.jwebconsole.client.application.toolbar;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.jwebconsole.client.application.ApplicationPresenter;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.jwebconsole.client.event.RevealOnStartEventHandler;
import org.jwebconsole.client.event.popup.RevealConnectionPopupEvent;


public class ToolbarPresenter extends Presenter<ToolbarView, ToolbarPresenter.ToolbarProxy> implements ToolbarUiHandlers, RevealOnStartEventHandler {

    @ProxyCodeSplit
    public interface ToolbarProxy extends Proxy<ToolbarPresenter> {
    }

    @Inject
    public ToolbarPresenter(EventBus eventBus, ToolbarView view, ToolbarProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_TOOLBAR);
        view.setUiHandlers(this);
    }


    @ProxyEvent
    @Override
    public void onApplicationStart(RevealOnStartEvent event) {
        forceReveal();
    }

    @Override
    public void openConnectionWindow() {
        getEventBus().fireEvent(new RevealConnectionPopupEvent());
    }

}
