package org.jwebconsole.client.application.toolbar;

import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.model.base.SimpleResponse;
import org.jwebconsole.client.service.ServiceFactory;

public class ToolbarPresenterFacade {

    private ServiceFactory serviceFactory;

    public ToolbarPresenterFacade(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public void deleteHost(String hostId, MethodCallback<SimpleResponse> callback) {
        serviceFactory.getHostService().delete(hostId, callback);
    }

}
