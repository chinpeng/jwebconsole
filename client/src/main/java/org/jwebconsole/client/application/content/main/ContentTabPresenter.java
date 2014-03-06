package org.jwebconsole.client.application.content.main;

import com.google.gwt.event.shared.GwtEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.place.ContentTabs;
import org.jwebconsole.client.place.NameTokens;
import org.jwebconsole.client.util.PlaceRequestUtils;


public class ContentTabPresenter extends Presenter<ContentTabView, ContentTabPresenter.ContentTabProxy> implements ContentTabUiHandlers {


    @ContentSlot
    public static final GwtEvent.Type<RevealContentHandler<?>> SLOT_THREADS = new GwtEvent.Type<RevealContentHandler<?>>();

    @ContentSlot
    public static final GwtEvent.Type<RevealContentHandler<?>> SLOT_MEMORY = new GwtEvent.Type<RevealContentHandler<?>>();

    private final PlaceManager placeManager;

    @ProxyStandard
    public interface ContentTabProxy extends Proxy<ContentTabPresenter> {
    }

    @Inject
    public ContentTabPresenter(EventBus eventBus, ContentTabView view, ContentTabProxy proxy, PlaceManager placeManager) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_CONTENT_PANEL);
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onBind() {
        super.onBind();
        initViewTabsWithNameTokens();
    }

    @Override
    public void onActiveTabSelected(String token) {
        PlaceRequest request = PlaceRequestUtils.getPlaceRequestWithReplacedToken(placeManager.getCurrentPlaceRequest(), token);
        placeManager.revealPlace(request);
    }

    private void initViewTabsWithNameTokens() {
        getView().setMemoryNameToken(ContentTabs.MEMORY_TAB.getNameToken());
        getView().setThreadsNameToken(ContentTabs.THREAD_TAB.getNameToken());
    }

    @Override
    protected void onReset() {
        super.onReset();
        applyActiveTabFromRequest();
    }

    private void applyActiveTabFromRequest() {
        String token = placeManager.getCurrentPlaceRequest().getNameToken();
        getView().applySelectionByNameToken(token);
    }
}
