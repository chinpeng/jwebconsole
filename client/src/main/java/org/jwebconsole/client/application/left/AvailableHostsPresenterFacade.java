package org.jwebconsole.client.application.left;

import com.google.inject.Inject;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.model.host.HostConnectionListResponse;
import org.jwebconsole.client.service.ServiceFactory;

public class AvailableHostsPresenterFacade {

    private final ServiceFactory serviceFactory;

    @Inject
    public AvailableHostsPresenterFacade(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public void getHosts(MethodCallback<HostConnectionListResponse> callback) {
        serviceFactory.getHostService().getHostsStatus(callback);
    }

}
