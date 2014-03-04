package org.jwebconsole.client.application.content.thread;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.grid.*;
import org.jwebconsole.client.application.content.thread.model.ThreadInfoPropertyAccessor;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.model.thread.info.ThreadInfoEntity;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ThreadContentViewImpl extends ViewWithUiHandlers<ThreadContentUiHandlers> implements ThreadContentView {

    private static final ThreadInfoPropertyAccessor accessor = GWT.create(ThreadInfoPropertyAccessor.class);

    private final AppResources appResources;

    @UiField
    FramedPanel chartPanel;
    @UiField(provided = true)
    ListStore<ThreadInfoEntity> store;
    @UiField(provided = true)
    ColumnModel<ThreadInfoEntity> columnModel;
    @UiField
    GridView gridView;
    @UiField
    Grid grid;

    interface Binder extends UiBinder<Widget, ThreadContentViewImpl> {
    }

    @Inject
    ThreadContentViewImpl(Binder uiBinder, AppResources appResources) {
        this.appResources = appResources;
        init();
        initWidget(uiBinder.createAndBindUi(this));
        initView();
    }

    private void initView() {
        gridView.setAutoFill(true);
        gridView.setForceFit(true);
    }

    private void init() {
        ColumnConfig<ThreadInfoEntity, String> nameColumn = new ColumnConfig<ThreadInfoEntity, String>(accessor.name(), 50, appResources.getMessages().threadNameColumnHeader());
        List<ColumnConfig<ThreadInfoEntity, ?>> columns = new ArrayList<ColumnConfig<ThreadInfoEntity, ?>>();
        columns.add(nameColumn);
        columnModel = new ColumnModel<ThreadInfoEntity>(columns);
        store = new ListStore<ThreadInfoEntity>(accessor.key());
    }

    @Override
    public void fillThreads(List<ThreadInfoEntity> entities) {
        store.clear();
        store.addAll(entities);
    }


    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == ThreadContentPresenter.THREAD_CHART_WIDGET_SLOT) {
            chartPanel.clear();
            chartPanel.add(content);
            chartPanel.forceLayout();
        }
    }


}