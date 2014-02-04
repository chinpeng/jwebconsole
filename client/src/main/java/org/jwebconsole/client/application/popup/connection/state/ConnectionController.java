package org.jwebconsole.client.application.popup.connection.state;

import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;

public interface ConnectionController {

    void initViewOnAppear();

    void makeRequest(MethodCallback<HostConnectionResponse> callback);

    void fireChangeEvent(HostConnection connection);
}
