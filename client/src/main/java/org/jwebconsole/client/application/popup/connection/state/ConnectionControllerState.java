package org.jwebconsole.client.application.popup.connection.state;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import org.jwebconsole.client.application.popup.connection.ConnectionWindowView;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.service.ServiceFactory;

public class ConnectionControllerState {

    private final ConnectionWindowView view;
    private final EventBus eventBus;
    private ServiceFactory serviceFactory;
    private ConnectionController controller;

    @Inject
    public ConnectionControllerState(ConnectionWindowView view, EventBus eventBus, ServiceFactory serviceFactory) {
        this.view = view;
        this.eventBus = eventBus;
        this.serviceFactory = serviceFactory;
    }

    public void becomeCreateConnectionController() {
        this.controller = new CreateConnectionController(view, eventBus, serviceFactory);
    }

    public void becomeEditConnectionController(HostConnection connection) {
        this.controller = new EditConnectionController(view, eventBus, serviceFactory, connection);
    }

    public ConnectionController getController() {
        return this.controller;
    }

}
