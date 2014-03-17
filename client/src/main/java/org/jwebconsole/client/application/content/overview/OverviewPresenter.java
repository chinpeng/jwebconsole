


package org.jwebconsole.client.application.content.overview;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasSlots;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.TabDataBasic;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import org.jwebconsole.client.application.content.main.ContentTabPresenter;
import org.jwebconsole.client.application.content.thread.widget.chart.ThreadCountChartPresenter;
import org.jwebconsole.client.application.left.event.HostSelectedEvent;
import org.jwebconsole.client.application.left.event.HostSelectedEventHandler;
import org.jwebconsole.client.bundle.messages.Messages;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.place.ContentTabs;
import org.jwebconsole.client.place.NameTokens;

public class OverviewPresenter extends Presenter<OverviewView, OverviewPresenter.OverviewProxy>
        implements OverviewUiHandlers {

    public static final Object THREAD_CHART_WIDGET_SLOT = new Object();

    private OverviewPresenterFacade facade;

    @NameToken(NameTokens.overview)
    @ProxyCodeSplit
    public interface OverviewProxy extends TabContentProxyPlace<OverviewPresenter> {
    }

    @Inject
    public OverviewPresenter(EventBus eventBus, OverviewView view, OverviewProxy proxy, OverviewPresenterFacade facade) {
        super(eventBus, view, proxy, ContentTabPresenter.TYPE_SET_TAB_CONTENT);
        this.facade = facade;
        getView().setUiHandlers(this);
    }

    @SuppressWarnings("unused")
    @TabInfo(container = ContentTabPresenter.class)
    static TabData getTabLabel(Messages messages) {
        return ContentTabs.OVERVIEW_TAB.toTabData(messages.tabOverviewHeaderText());
    }

    @Override
    protected void onReset() {
        super.onReset();
        initChart();
    }

    private void initChart() {
        String connectionId = facade.getConnectionId();
        if (connectionId != null) {
            facade.revealThreadCountChartPresenter(this, connectionId);
        }
    }


}
