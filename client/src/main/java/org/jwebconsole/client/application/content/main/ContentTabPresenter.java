package org.jwebconsole.client.application.content.main;

import com.google.gwt.event.shared.GwtEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ChangeTabHandler;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.RequestTabsHandler;
import com.gwtplatform.mvp.client.TabContainerPresenter;
import com.gwtplatform.mvp.client.annotations.ChangeTab;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.annotations.RequestTabs;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.place.ContentTabs;
import org.jwebconsole.client.place.NameTokens;
import org.jwebconsole.client.util.PlaceRequestUtils;


public class ContentTabPresenter extends TabContainerPresenter<ContentTabView, ContentTabPresenter.ContentTabProxy> implements ContentTabUiHandlers {

    @RequestTabs
    public static final GwtEvent.Type<RequestTabsHandler> TYPE_REQUEST_TABS = new GwtEvent.Type<RequestTabsHandler>();

    @ChangeTab
    public static final GwtEvent.Type<ChangeTabHandler> TYPE_CHANGE_TAB = new GwtEvent.Type<ChangeTabHandler>();

    @ContentSlot
    public static final GwtEvent.Type<RevealContentHandler<?>> TYPE_SET_TAB_CONTENT = new GwtEvent.Type<RevealContentHandler<?>>();

    @Override
    public void redirect(String nameToken) {

    }

    @ProxyStandard
    public interface ContentTabProxy extends Proxy<ContentTabPresenter> {
    }

    @Inject
    public ContentTabPresenter(EventBus eventBus, ContentTabView view, ContentTabProxy proxy) {
        super(eventBus, view, proxy, TYPE_SET_TAB_CONTENT, TYPE_REQUEST_TABS, TYPE_CHANGE_TAB, ApplicationPresenter.SLOT_CONTENT_PANEL);
        getView().setUiHandlers(this);
    }

    @Override
    protected void onBind() {
        super.onBind();
    }

}
