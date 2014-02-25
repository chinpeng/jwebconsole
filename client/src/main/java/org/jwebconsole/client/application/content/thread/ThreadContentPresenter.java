package org.jwebconsole.client.application.content.thread;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import org.jwebconsole.client.application.left.event.HostSelectedEvent;
import org.jwebconsole.client.application.left.event.HostSelectedEventHandler;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.place.NameTokens;

public class ThreadContentPresenter extends Presenter<ThreadContentView, ThreadContentPresenter.ThreadContentProxy>
        implements ThreadContentUiHandlers, HostSelectedEventHandler {

    private final ThreadContentPresenterFacade facade;

    public static final Object THREAD_CHART_WIDGET_SLOT = new Object();

    @Inject
    public ThreadContentPresenter(EventBus eventBus, ThreadContentView view, ThreadContentProxy proxy, ThreadContentPresenterFacade facade) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_CONTENT_PANEL);
        this.facade = facade;
        init();
    }

    private void init() {
        getView().setUiHandlers(this);
        getEventBus().addHandler(HostSelectedEvent.TYPE, this);
    }

    @Override
    public void onHostSelected(HostSelectedEvent event) {
        if (event.getConnection() != null) {
            facade.revealThreadCountChartPresenter(this, event.getConnection());
        } else {
            facade.disableChart();
        }
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.thread)
    public interface ThreadContentProxy extends ProxyPlace<ThreadContentPresenter> {

    }

}
