package org.jwebconsole.client.application.content.main;

import com.google.gwt.event.shared.GwtEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import org.jwebconsole.client.application.main.ApplicationPresenter;


public class ContentTabPresenter extends Presenter<ContentTabView, ContentTabPresenter.ContentTabProxy> implements ContentTabUiHandlers {


    @ContentSlot
    public static final GwtEvent.Type<RevealContentHandler<?>> SLOT_THREADS = new GwtEvent.Type<RevealContentHandler<?>>();

    @ProxyStandard
    public interface ContentTabProxy extends Proxy<ContentTabPresenter> {
    }

    @Inject
    public ContentTabPresenter(EventBus eventBus, ContentTabView view, ContentTabProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_CONTENT_PANEL);
        getView().setUiHandlers(this);
    }


}
