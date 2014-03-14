package org.jwebconsole.client.application.content.thread;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.grid.*;
import org.jwebconsole.client.application.content.thread.model.ThreadInfoPropertyAccessor;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.model.thread.details.ThreadDetailsEntity;
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
    Grid<ThreadInfoEntity> grid;
    @UiField
    VerticalLayoutContainer.VerticalLayoutData gridLayoutData;
    @UiField
    VerticalLayoutContainer mainContainer;
    @UiField
    VerticalLayoutContainer threadDetailsPanel;

    interface Binder extends UiBinder<Widget, ThreadContentViewImpl> {
    }

    @Inject
    ThreadContentViewImpl(Binder uiBinder, AppResources appResources) {
        this.appResources = appResources;
        initGrid();
        initWidget(uiBinder.createAndBindUi(this));
        initLayoutData();
        initView();
        initGridHandlers();
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
                gridLayoutData.setHeight(.94d);
                mainContainer.forceLayout();

            }
        });
        chartPanel.addExpandHandler(new ExpandEvent.ExpandHandler() {
            @Override
            public void onExpand(ExpandEvent event) {
                gridLayoutData.setHeight(.5d);
                mainContainer.forceLayout();
            }
        });
    }

    private void initView() {
        gridView.setAutoFill(true);
        gridView.setForceFit(true);
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
        store.clear();
        store.addAll(entities);
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
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == ThreadContentPresenter.THREAD_CHART_WIDGET_SLOT) {
            chartPanel.clear();
            chartPanel.add(content);
            chartPanel.forceLayout();
        }
    }


}