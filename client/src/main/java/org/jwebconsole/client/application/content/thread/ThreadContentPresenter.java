package org.jwebconsole.client.application.content.thread;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.TabDataBasic;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.TabContentProxy;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import org.jwebconsole.client.application.content.main.ContentTabPresenter;
import org.jwebconsole.client.application.left.event.HostSelectedEvent;
import org.jwebconsole.client.application.left.event.HostSelectedEventHandler;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.thread.details.ThreadDetailsListResponse;
import org.jwebconsole.client.model.thread.info.ThreadInfoEntity;
import org.jwebconsole.client.model.thread.info.ThreadInfoListResponse;
import org.jwebconsole.client.place.NameTokens;
import org.jwebconsole.client.service.AppCallback;

public class ThreadContentPresenter extends Presenter<ThreadContentView, ThreadContentPresenter.ThreadContentProxy>
        implements ThreadContentUiHandlers, HostSelectedEventHandler {

    private final ThreadContentPresenterFacade facade;

    public static final Object THREAD_CHART_WIDGET_SLOT = new Object();
    private HostConnection connection;

    @Inject
    public ThreadContentPresenter(EventBus eventBus, ThreadContentView view, ThreadContentProxy proxy, ThreadContentPresenterFacade facade) {
        super(eventBus, view, proxy, ContentTabPresenter.TYPE_SET_TAB_CONTENT);
        this.facade = facade;
        init();
    }

    private void init() {
        getView().setUiHandlers(this);
        getEventBus().addHandler(HostSelectedEvent.TYPE, this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        facade.disableThreadInfoTimer();
        getView().clearStackTracePanel();
    }

    @Override
    public void onHostSelected(HostSelectedEvent event) {
        if (event.getConnection() != null) {
            facade.revealThreadCountChartPresenter(this, event.getConnection());
            makeRequest(event.getConnection());
            this.connection = event.getConnection();
        } else {
            facade.disableChart();
        }
    }

    private void makeRequest(HostConnection connection) {
        facade.scheduleThreadInfoRequest(connection.getId(), new AppCallback<ThreadInfoListResponse>() {
            @Override
            public void onSuccess(ThreadInfoListResponse response) {
                getView().fillThreads(response.getBody());
            }
        });
    }

    @TabInfo(container = ContentTabPresenter.class)
    static TabData getTabLabel() {
        return new TabDataBasic("Thread", 0);
    }

    @Override
    public void onThreadSelected(ThreadInfoEntity thread) {
        if (connection != null) {
            facade.makeThreadDetailsRequest(connection.getId(), thread.getThreadId(), new AppCallback<ThreadDetailsListResponse>() {
                @Override
                public void onSuccess(ThreadDetailsListResponse response) {
                    getView().fillThreadDetails(response.getBody());
                }
            });
        }
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.thread)
    public interface ThreadContentProxy extends TabContentProxyPlace<ThreadContentPresenter> {

    }

}
