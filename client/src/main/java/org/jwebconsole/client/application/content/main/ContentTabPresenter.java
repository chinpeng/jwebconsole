package org.jwebconsole.client.application.content.main;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.place.NameTokens;


public class ContentTabPresenter extends Presenter<ContentTabPresenter.MyView, ContentTabPresenter.ContentTabProxy> implements ContentTabPresenterUiHandlers {
    interface MyView extends View, HasUiHandlers<ContentTabPresenterUiHandlers> {
    }


    @ProxyStandard
    @NameToken(NameTokens.other)
    public interface ContentTabProxy extends ProxyPlace<ContentTabPresenter> {
    }

    @Inject
    public ContentTabPresenter(EventBus eventBus, MyView view, ContentTabProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_CONTENT_PANEL);
        getView().setUiHandlers(this);
    }


}
