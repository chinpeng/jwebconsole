package org.jwebconsole.client.application.content.thread;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import org.jwebconsole.client.application.ApplicationPresenter;
import org.jwebconsole.client.place.NameTokens;

public class ThreadContentPresenter extends Presenter<ThreadContentView, ThreadContentPresenter.ThreadContentProxy>
        implements ThreadContentUiHandlers {

    private final ThreadContentPresenterFacade facade;

    @Inject
    public ThreadContentPresenter(EventBus eventBus, ThreadContentView view, ThreadContentProxy proxy, ThreadContentPresenterFacade facade) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_CONTENT_PANEL);
        this.facade = facade;
        view.setUiHandlers(this);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.thread)
    public interface ThreadContentProxy extends ProxyPlace<ThreadContentPresenter> {

    }

    @Override
    protected void onReset() {
        super.onReset();

    }
}
