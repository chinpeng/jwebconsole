package org.jwebconsole.client.application.content.summary;

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
import org.jwebconsole.client.model.summary.SummaryEntity;
import org.jwebconsole.client.model.summary.SummaryResponse;
import org.jwebconsole.client.application.content.main.ContentTabs;
import org.jwebconsole.client.place.NameTokens;
import org.jwebconsole.client.service.AppCallback;


public class SummaryPresenter extends Presenter<SummaryView, SummaryPresenter.SummaryProxy> implements SummaryUiHandlers {

    private final SummaryFacade facade;

    @ProxyCodeSplit
    @NameToken(NameTokens.summary)
    public interface SummaryProxy extends TabContentProxyPlace<SummaryPresenter> {
    }

    @Inject
    public SummaryPresenter(EventBus eventBus, SummaryView view, SummaryProxy proxy, SummaryFacade facade) {
        super(eventBus, view, proxy, ContentTabPresenter.TYPE_SET_TAB_CONTENT);
        this.facade = facade;
        getView().setUiHandlers(this);
    }

    @SuppressWarnings("unused")
    @TabInfo(container = ContentTabPresenter.class)
    static TabData getTabLabel(Messages messages) {
        return ContentTabs.SUMMARY_TAB.toTabData(messages.tabSummaryHeaderText());
    }

    @Override
    protected void onReset() {
        super.onReset();
        String connectionId = facade.getCurrentConnectionId();
        if (connectionId != null) {
            makeSummaryRequest(connectionId);
        }
    }

    private void makeSummaryRequest(String connectionId) {
        facade.makeSummaryRequest(connectionId, new AppCallback<SummaryResponse>() {
            @Override
            public void onSuccess(SummaryResponse response) {
                processResponse(response.getBody());
            }
        });
    }

    private void processResponse(SummaryEntity entity) {
        getView().setName(entity.getName());
        getView().setVersion(entity.getVersion());
        getView().setArchitecture(entity.getArchitecture());
        getView().setSystemLoadAverage(entity.getSystemLoadAverage());
        getView().setAvailableProcessors(entity.getAvailableProcessors());
        getView().setProcessCPUTime(entity.getProcessCPUTime());
        getView().setCommittedVirtualMemorySize(entity.getCommittedVirtualMemorySize());
        getView().setTotalPhysicalMemorySize(entity.getTotalPhysicalMemorySize());
        getView().setFreePhysicalMemorySize(entity.getFreePhysicalMemorySize());
        getView().setTotalSwapSpaceSize(entity.getTotalSwapSpaceSize());
        getView().setFreeSwapSpaceSize(entity.getFreeSwapSpaceSize());
    }

}
