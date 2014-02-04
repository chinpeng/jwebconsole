package org.jwebconsole.client.application.popup.connection.state;

import com.google.web.bindery.event.shared.EventBus;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.application.popup.connection.ConnectionWindowView;
import org.jwebconsole.client.application.popup.connection.event.HostChangedEvent;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.jwebconsole.client.service.ServiceFactory;

public class EditConnectionController implements ConnectionController {

    private final ConnectionWindowView view;
    private final EventBus eventBus;
    private ServiceFactory serviceFactory;
    private HostConnection editableConnection;

    public EditConnectionController(ConnectionWindowView view, EventBus eventBus, ServiceFactory serviceFactory, HostConnection editableConnection) {
        this.view = view;
        this.eventBus = eventBus;
        this.serviceFactory = serviceFactory;
        this.editableConnection = editableConnection;
    }

    @Override
    public void initViewOnAppear() {
        view.setHostName(editableConnection.getName());
        view.setPort(editableConnection.getPort());
        view.setLogin(editableConnection.getUser());
        view.setPassword(editableConnection.getPassword());
    }

    @Override
    public void makeRequest(MethodCallback<HostConnectionResponse> callback) {
        HostConnection connection = createConnectionFromInputs();
        serviceFactory.getHostService().editHost(editableConnection.getId(), connection, callback);
    }

    private HostConnection createConnectionFromInputs() {
        HostConnection result = new HostConnection();
        result.setName(view.getHostName());
        result.setPort(view.getPort());
        result.setUser(view.getLogin());
        result.setPassword(view.getPassword());
        return result;
    }

    @Override
    public void fireChangeEvent(HostConnection connection) {
        eventBus.fireEvent(new HostChangedEvent(connection));
    }
}
