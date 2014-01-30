package org.jwebconsole.client.service;

import com.google.inject.Inject;

public class ServiceFactory {

    @Inject
    private HostService hostService;

    public HostService getHostService() {
        return hostService;
    }

}
