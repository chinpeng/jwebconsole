package org.jwebconsole.client.application.popup.connection.state;

import com.google.web.bindery.event.shared.EventBus;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.application.popup.connection.ConnectionWindowView;
import org.jwebconsole.client.application.popup.connection.event.HostCreatedEvent;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.jwebconsole.client.service.ServiceFactory;

public class CreateConnectionController implements ConnectionController {

    private final ConnectionWindowView view;
    private final EventBus eventBus;
    private ServiceFactory serviceFactory;

    public CreateConnectionController(ConnectionWindowView view, EventBus eventBus, ServiceFactory serviceFactory) {
        this.view = view;
        this.eventBus = eventBus;
        this.serviceFactory = serviceFactory;
    }

    @Override
    public void initViewOnAppear() {
        view.clearFields();
    }

    @Override
    public void makeRequest(MethodCallback<HostConnectionResponse> callback) {
        HostConnection connection = createConnectionFromInputs();
        serviceFactory.getHostService().addNewHost(connection, callback);
    }

    @Override
    public void fireChangeEvent(HostConnection connection) {
        eventBus.fireEvent(new HostCreatedEvent(connection));
    }

    private HostConnection createConnectionFromInputs() {
        HostConnection result = new HostConnection();
        result.setName(view.getHostName());
        result.setPort(view.getPort());
        result.setUser(view.getLogin());
        result.setPassword(view.getPassword());
        return result;
    }

}
