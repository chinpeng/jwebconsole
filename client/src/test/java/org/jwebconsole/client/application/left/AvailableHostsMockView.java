package org.jwebconsole.client.application.left;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.jwebconsole.client.model.host.HostConnection;

import java.util.List;

public class AvailableHostsMockView implements AvailableHostsView {

    private boolean enabled = true;
    private boolean selectionFired = false;
    private AvailableHostsUiHandlers handlers;
    List<HostConnection> connections;
    private boolean selectionMade = false;
    private HostConnection selectedItem;

    @Override
    public void addHost(HostConnection connection) {
        connections.add(connection);
        changeHost(connection);
    }

    @Override
    public void showLoadingMask() {

    }

    @Override
    public void hideLoadingMask() {

    }

    @Override
    public void deleteHostConnection(HostConnection deletedHost) {

    }

    @Override
    public void changeHost(HostConnection connection) {

    }

    @Override
    public void disableSelectionHandler() {
        this.enabled = false;
    }

    @Override
    public void enableSelectionHandler() {
        this.enabled = true;
    }

    @Override
    public void clearStore() {

    }

    @Override
    public void addConnection(HostConnection connection) {

    }

    @Override
    public void setSelection(HostConnection connection) {
        if (enabled) {
            selectionFired = true;
        }
        this.selectionMade = true;
        this.selectedItem = connection;
    }

    @Override
    public void setUiHandlers(AvailableHostsUiHandlers availableHostsUiHandlers) {
        this.handlers = availableHostsUiHandlers;
    }

    @Override
    public void addToSlot(Object o, IsWidget isWidget) {

    }

    @Override
    public void removeFromSlot(Object o, IsWidget isWidget) {

    }

    @Override
    public void setInSlot(Object o, IsWidget isWidget) {

    }

    @Override
    public Widget asWidget() {
        return null;
    }

    public boolean isSelectionFired() {
        return selectionFired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isSelectionMade() {
        return selectionMade;
    }

    public HostConnection getSelectedItem() {
        return selectedItem;
    }

    public List<HostConnection> getConnections() {
        return connections;
    }
}
