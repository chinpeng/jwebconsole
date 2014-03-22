package org.jwebconsole.client.service.host;

import com.google.inject.Inject;
import org.jwebconsole.client.model.base.SimpleResponse;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionListResponse;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.jwebconsole.client.util.monad.future.Future;

public class HostFutureService {

    private HostService hostService;

    @Inject
    public HostFutureService(HostService hostService) {
        this.hostService = hostService;
    }

    public Future<HostConnectionResponse> getHost(String id) {
        return new Future<>((callback) -> hostService.getHost(id, callback));
    }

    public Future<HostConnectionListResponse> getHostsStatus() {
        return new Future<>(hostService::getHostsStatus);
    }

    public Future<HostConnectionResponse> addNewHost(HostConnection connection) {
        return new Future<>((callback) -> hostService.addNewHost(connection, callback));
    }

    public Future<SimpleResponse> delete(String id) {
        return new Future<>((callback) -> hostService.delete(id, callback));
    }

    public Future<HostConnectionResponse> editHost(String id, HostConnection connection) {
        return new Future<>((callback) -> hostService.editHost(id, connection, callback));
    }
}
