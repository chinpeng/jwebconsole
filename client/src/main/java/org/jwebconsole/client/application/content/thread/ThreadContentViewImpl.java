package org.jwebconsole.client.application.content.thread;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.grid.*;
import org.jwebconsole.client.application.content.thread.model.ThreadInfoPropertyAccessor;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.model.thread.details.ThreadDetailsEntity;
import org.jwebconsole.client.model.thread.info.ThreadInfoEntity;
import org.jwebconsole.client.util.ContainerUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
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
    Grid<ThreadInfoEntity> grid;
    @UiField
    VerticalLayoutContainer.VerticalLayoutData gridLayoutData;
    @UiField
    VerticalLayoutContainer mainContainer;
    @UiField
    VerticalLayoutContainer threadDetailsPanel;
    @UiField
    VerticalLayoutContainer.VerticalLayoutData chartPanelLayoutData;

    interface Binder extends UiBinder<Widget, ThreadContentViewImpl> {
    }

    @Inject
    ThreadContentViewImpl(Binder uiBinder, AppResources appResources) {
        this.appResources = appResources;
        initGrid();
        initThreadFilter();
        initWidget(uiBinder.createAndBindUi(this));
        initLayoutData();
        initView();
        initGridHandlers();
    }

    private void initThreadFilter() {

    }

    private void initGridHandlers() {
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        grid.getSelectionModel().addSelectionHandler(new SelectionHandler<ThreadInfoEntity>() {
            @Override
            public void onSelection(SelectionEvent<ThreadInfoEntity> selectionEvent) {
                getUiHandlers().onThreadSelected(selectionEvent.getSelectedItem());
            }
        });
    }

    private void initLayoutData() {
        chartPanel.addCollapseHandler(new CollapseEvent.CollapseHandler() {
            @Override
            public void onCollapse(CollapseEvent event) {
                chartPanelLayoutData.setHeight(-1);
                gridLayoutData.setHeight(1);
                mainContainer.forceLayout();

            }
        });
        chartPanel.addExpandHandler(new ExpandEvent.ExpandHandler() {
            @Override
            public void onExpand(ExpandEvent event) {
                chartPanelLayoutData.setHeight(0.5);
                gridLayoutData.setHeight(0.5);
                mainContainer.forceLayout();
            }
        });
    }

    private void initView() {
        gridView.setAutoFill(true);
        gridView.setForceFit(true);
        grid.setHideHeaders(true);
    }

    private void initGrid() {
        ColumnConfig<ThreadInfoEntity, String> nameColumn = new ColumnConfig<ThreadInfoEntity, String>(accessor.name(), 50, appResources.getMessages().threadNameColumnHeader());
        List<ColumnConfig<ThreadInfoEntity, ?>> columns = new ArrayList<ColumnConfig<ThreadInfoEntity, ?>>();
        columns.add(nameColumn);
        columnModel = new ColumnModel<ThreadInfoEntity>(columns);
        store = new ListStore<ThreadInfoEntity>(accessor.key());
    }

    @Override
    public void fillThreads(List<ThreadInfoEntity> entities) {
        int scroll = grid.getView().getScroller().getScrollTop();
        store.clear();
        store.addAll(entities);
        grid.getView().getScroller().setScrollTop(scroll);
    }

    @Override
    public void fillThreadDetails(List<ThreadDetailsEntity> entities) {
        threadDetailsPanel.clear();
        for (ThreadDetailsEntity entity : entities) {
            threadDetailsPanel.add(new Label(entity.getStackTraceElement()));
        }
    }

    @Override
    public void clearStackTracePanel() {
        threadDetailsPanel.clear();
    }

    @Override
    public void setSelection(ThreadInfoEntity thread) {
        grid.getSelectionModel().deselectAll();
        grid.getSelectionModel().setSelection(Collections.singletonList(thread));
    }


    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == ThreadContentPresenter.THREAD_CHART_WIDGET_SLOT) {
            ContainerUtils.clearAndPut(chartPanel, content);
        }
    }


}