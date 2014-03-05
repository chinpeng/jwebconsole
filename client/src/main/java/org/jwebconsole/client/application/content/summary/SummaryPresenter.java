package org.jwebconsole.client.application.content.summary;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import org.jwebconsole.client.application.content.main.ContentTabPresenter;
import org.jwebconsole.client.place.NameTokens;


public class SummaryPresenter extends Presenter<SummaryView, SummaryPresenter.SummaryProxy> /*implements HostSelectedEventHandler*/ {

    @Inject
    public SummaryPresenter(EventBus eventBus, SummaryView view, SummaryProxy proxy) {
        super(eventBus, view, proxy, ContentTabPresenter.SLOT_SUMMARY);

    }

    @ProxyCodeSplit
    @NameToken(NameTokens.summary)
    public interface SummaryProxy extends ProxyPlace<SummaryPresenter> {
    }

    @Override
    protected void onBind() {
        super.onBind();
    }

}
