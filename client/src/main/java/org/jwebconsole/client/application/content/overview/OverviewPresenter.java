



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
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.place.NameTokens;

public class OverviewPresenter extends Presenter<OverviewView, OverviewPresenter.OverviewProxy>
        implements OverviewUiHandlers,HostSelectedEventHandler {

    public static final Object THREAD_CHART_WIDGET_SLOT = new Object();

    private ThreadCountChartPresenter threadCountChartPresenter;

    @NameToken(NameTokens.overview)
    @ProxyCodeSplit
    public interface OverviewProxy extends TabContentProxyPlace<OverviewPresenter> {
    }

    @Inject
    public OverviewPresenter(EventBus eventBus, OverviewView view, OverviewProxy proxy, ThreadCountChartPresenter threadCountChartPresenter) {
        super(eventBus, view, proxy, ContentTabPresenter.TYPE_SET_TAB_CONTENT);
        this.threadCountChartPresenter = threadCountChartPresenter;
        getView().setUiHandlers(this);
        getEventBus().addHandler(HostSelectedEvent.TYPE, this);
    }

    @TabInfo(container = ContentTabPresenter.class)
    static TabData getTabLabel() {
        return new TabDataBasic("Overview", 0);
    }

    @Override
    public void onHostSelected(HostSelectedEvent event) {
        if (event.getConnection() != null) {
            revealThreadCountChartPresenter(this, event.getConnection());
        }
    }

    @Override
    protected void onBind() {
        super.onBind();
    }

    public void revealThreadCountChartPresenter(HasSlots parent, HostConnection connection) {
        threadCountChartPresenter.init(connection);
        parent.setInSlot(THREAD_CHART_WIDGET_SLOT, threadCountChartPresenter);
    }


}
