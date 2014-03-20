package org.jwebconsole.client.application.content.memory;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import org.jwebconsole.client.application.content.main.ContentTabPresenter;
import org.jwebconsole.client.bundle.messages.Messages;
import org.jwebconsole.client.application.content.main.ContentTabs;
import org.jwebconsole.client.place.NameTokens;

public class MemoryPresenter extends Presenter<MemoryView, MemoryPresenter.MemoryProxy> implements MemoryUiHandlers {

    private final MemoryPresenterFacade facade;

    @Inject
    public MemoryPresenter(EventBus eventBus, MemoryView view, MemoryProxy proxy, MemoryPresenterFacade facade) {
        super(eventBus, view, proxy, ContentTabPresenter.TYPE_SET_TAB_CONTENT);
        this.facade = facade;
        init();
    }

    private void init() {
        getView().setUiHandlers(this);
    }

    @SuppressWarnings("unused")
    @TabInfo(container = ContentTabPresenter.class)
    static TabData getTabLabel(Messages messages) {
        return ContentTabs.MEMORY_TAB.toTabData(messages.tabMemoryHeaderText());
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.memory)
    public interface MemoryProxy extends TabContentProxyPlace<MemoryPresenter> {
    }

    @Override
    protected void onBind() {
        super.onBind();
    }

}
