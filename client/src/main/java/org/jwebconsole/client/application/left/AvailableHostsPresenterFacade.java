package org.jwebconsole.client.application.left;

import com.google.gwt.user.client.Timer;
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

    public void scheduleReceiveHosts(final int time, final MethodCallback<HostConnectionListResponse> callback) {
        getHosts(callback);
        Timer timer = new Timer() {
            @Override
            public void run() {
                serviceFactory.getHostService().getHostsStatus(callback);
            }
        };
        timer.scheduleRepeating(time);
    }

}
