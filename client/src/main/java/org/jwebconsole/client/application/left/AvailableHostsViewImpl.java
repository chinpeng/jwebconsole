package org.jwebconsole.client.application.left;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.core.client.ValueProvider;
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

    interface Binder extends UiBinder<Widget, AvailableHostsViewImpl> {
    }

    @UiField
    HTMLPanel main;

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
        tree.getStyle().setLeafIcon(appResources.getIcons().hostAvailableIcon());
        initClickHandler();
    }

    private void initClickHandler() {
       tree.getSelectionModel().addSelectionHandler(new SelectionHandler<HostConnection>() {
           @Override
           public void onSelection(SelectionEvent<HostConnection> event) {
               getUiHandlers().onTreeItemSelected(event.getSelectedItem());
           }
       });
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
    public void fillTree(List<HostConnection> connections) {
        for (HostConnection connection : connections) {
            store.add(connection);
        }
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


}