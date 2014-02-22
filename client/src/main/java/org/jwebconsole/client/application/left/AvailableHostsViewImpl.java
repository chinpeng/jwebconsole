package org.jwebconsole.client.application.left;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.tree.Tree;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.model.host.HostConnection;

import javax.inject.Inject;
import java.util.List;


public class AvailableHostsViewImpl extends ViewWithUiHandlers<AvailableHostsUiHandlers> implements AvailableHostsView {

    private final AppResources appResources;

    @UiField(provided = true)
    TreeStore<HostConnection> store;

    private CancellableSelectionHandler<HostConnection> selectHandler;

    interface Binder extends UiBinder<Widget, AvailableHostsViewImpl> {
    }

    @UiField
    Tree<HostConnection, String> tree;
    @UiField
    ContentPanel panel;

    @Inject
    AvailableHostsViewImpl(Binder uiBinder, AppResources appResources) {
        this.appResources = appResources;
        init();
        initWidget(uiBinder.createAndBindUi(this));
        initAfter();
    }

    private void initAfter() {
        initClickHandler();
        initIconProvider();
    }

    private void initIconProvider() {
        tree.setIconProvider(new IconProvider<HostConnection>() {
            @Override
            public ImageResource getIcon(HostConnection model) {
                if (model.getConnected() != null && model.getConnected().equals(true)) {
                    return appResources.getIcons().iconHostOn();
                } else {
                    return appResources.getIcons().iconHostOff();
                }
            }
        });
    }

    private void initClickHandler() {
        this.selectHandler = new CancellableSelectionHandler<HostConnection>() {
            @Override
            public void onSelectionMade(SelectionEvent<HostConnection> event) {
                getUiHandlers().onTreeItemSelected(event.getSelectedItem());
            }
        };
        selectHandler.enable();
        tree.getSelectionModel().addSelectionHandler(selectHandler);
    }

    private void init() {
        this.store = new TreeStore<HostConnection>(createKeyProvider());
    }

    private ModelKeyProvider<HostConnection> createKeyProvider() {
        return new ModelKeyProvider<HostConnection>() {
            @Override
            public String getKey(HostConnection item) {
                return item.getId();
            }
        };
    }

    @UiFactory
    ValueProvider<HostConnection, String> createValueProvider() {
        return new ValueProvider<HostConnection, String>() {
            @Override
            public String getValue(HostConnection connection) {
                return connection.getName() + ":" + connection.getPort();
            }

            @Override
            public void setValue(HostConnection object, String value) {

            }

            @Override
            public String getPath() {
                return "name";
            }
        };
    }

    @Override
    public void addHost(HostConnection connection) {
        store.add(connection);
    }

    @Override
    public void showLoadingMask() {
        panel.mask(appResources.getMessages().loadingMaskText());
    }

    @Override
    public void hideLoadingMask() {
        panel.unmask();
    }

    @Override
    public void deleteHostConnection(HostConnection connection) {
        store.remove(connection);
    }

    @Override
    public void changeHost(HostConnection connection) {
        store.remove(connection);
        store.add(connection);
    }

    @Override
    public void disableSelectionHandler() {
        selectHandler.cancel();
    }

    @Override
    public void enableSelectionHandler() {
        selectHandler.enable();
    }

    @Override
    public void clearStore() {
        this.store.clear();
    }

    @Override
    public void addConnection(HostConnection connection) {
        store.add(connection);
    }

    @Override
    public void setSelection(HostConnection connection) {
        tree.getSelectionModel().select(connection, false);
    }

    private static abstract class CancellableSelectionHandler<T> implements SelectionHandler<T> {

        private boolean cancelled = false;

        public void cancel() {
            cancelled = true;
        }

        public void enable() {
            cancelled = false;
        }

        @Override
        public void onSelection(SelectionEvent<T> event) {
            if (!cancelled) {
                onSelectionMade(event);
            }
        }

        public abstract void onSelectionMade(SelectionEvent<T> event);

    }

}
