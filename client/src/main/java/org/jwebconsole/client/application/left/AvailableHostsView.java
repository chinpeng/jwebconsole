package org.jwebconsole.client.application.left;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;
import org.jwebconsole.client.model.host.HostConnection;

import java.util.List;

interface AvailableHostsView extends View, HasUiHandlers<AvailableHostsUiHandlers> {

    void fillTree(List<HostConnection> connections);

    void addHost(HostConnection connection);

    void showLoadingMask();

    void hideLoadingMask();

    void deleteHostConnection(HostConnection deletedHost);

    void changeHost(HostConnection connection);
}