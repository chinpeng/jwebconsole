package org.jwebconsole.client.application.content.thread;

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
import org.jwebconsole.client.model.thread.details.ThreadDetailsListResponse;
import org.jwebconsole.client.model.thread.info.ThreadInfoEntity;
import org.jwebconsole.client.model.thread.info.ThreadInfoListResponse;
import org.jwebconsole.client.place.ContentTabs;
import org.jwebconsole.client.place.NameTokens;
import org.jwebconsole.client.service.AppCallback;

public class ThreadContentPresenter extends Presenter<ThreadContentView, ThreadContentPresenter.ThreadContentProxy>
        implements ThreadContentUiHandlers {

    private final ThreadContentPresenterFacade facade;

    public static final Object THREAD_CHART_WIDGET_SLOT = new Object();
    private String connectionId;

    @Inject
    public ThreadContentPresenter(EventBus eventBus, ThreadContentView view, ThreadContentProxy proxy, ThreadContentPresenterFacade facade) {
        super(eventBus, view, proxy, ContentTabPresenter.TYPE_SET_TAB_CONTENT);
        this.facade = facade;
        init();
    }

    private void init() {
        getView().setUiHandlers(this);
    }

    @Override
    public void onReset() {
        super.onReset();
        stopTimers();
        getView().clearStackTracePanel();
        prepareFromHostId();
    }

    public void prepareFromHostId() {
        this.connectionId = facade.getCurrentConnectionId();
        if (connectionId != null) {
            facade.revealThreadCountChartPresenter(this, connectionId);
            makeThreadInfoRequest();
        }
    }

    private void stopTimers() {
        facade.disableChart();
        facade.disableThreadInfoTimer();
    }

    private void makeThreadInfoRequest() {
        facade.scheduleThreadInfoRequest(connectionId, new AppCallback<ThreadInfoListResponse>() {
            @Override
            public void onSuccess(ThreadInfoListResponse response) {
                getView().fillThreads(response.getBody());
            }
        });
    }

    @SuppressWarnings("unused")
    @TabInfo(container = ContentTabPresenter.class)
    static TabData getTabLabel(Messages messages) {
        return ContentTabs.THREAD_TAB.toTabData(messages.tabThreadsHeaderText());
    }

    @Override
    public void onThreadSelected(ThreadInfoEntity thread) {
        if (connectionId != null) {
            facade.makeThreadDetailsRequest(connectionId, thread.getThreadId(), new AppCallback<ThreadDetailsListResponse>() {
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

    @Override
    public void onHide() {
        super.onHide();
        stopTimers();
    }

}
