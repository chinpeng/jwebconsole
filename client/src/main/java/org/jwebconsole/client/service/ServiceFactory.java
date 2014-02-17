package org.jwebconsole.client.service;

import com.google.inject.Inject;

public class ServiceFactory {

    @Inject
    private HostService hostService;

    @Inject
    private ThreadService threadService;

    public HostService getHostService() {
        return hostService;
    }

    public ThreadService getThreadService() {
        return threadService;
    }
}
