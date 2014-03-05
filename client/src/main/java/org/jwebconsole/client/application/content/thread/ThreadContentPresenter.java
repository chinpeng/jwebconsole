package org.jwebconsole.client.application.content.thread;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import org.jwebconsole.client.application.content.main.ContentTabPresenter;
import org.jwebconsole.client.application.left.event.HostSelectedEvent;
import org.jwebconsole.client.application.left.event.HostSelectedEventHandler;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.thread.info.ThreadInfoListResponse;
import org.jwebconsole.client.place.NameTokens;
import org.jwebconsole.client.service.AppCallback;

public class ThreadContentPresenter extends Presenter<ThreadContentView, ThreadContentPresenter.ThreadContentProxy>
        implements ThreadContentUiHandlers, HostSelectedEventHandler {

    private final ThreadContentPresenterFacade facade;

    public static final Object THREAD_CHART_WIDGET_SLOT = new Object();

    @Inject
    public ThreadContentPresenter(EventBus eventBus, ThreadContentView view, ThreadContentProxy proxy, ThreadContentPresenterFacade facade) {
        super(eventBus, view, proxy, ContentTabPresenter.SLOT_THREADS);
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
            makeRequest(event.getConnection());
        } else {
            facade.disableChart();
        }
    }

    private void makeRequest(HostConnection connection) {
        facade.makeThreadInfoRequest(connection.getId(), new AppCallback<ThreadInfoListResponse>() {
            @Override
            public void onSuccess(ThreadInfoListResponse response) {
                getView().fillThreads(response.getBody());
            }
        });
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.thread)
    public interface ThreadContentProxy extends ProxyPlace<ThreadContentPresenter> {

    }



}
