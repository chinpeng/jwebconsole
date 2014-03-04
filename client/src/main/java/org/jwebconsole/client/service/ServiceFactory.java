package org.jwebconsole.client.service;

import com.google.inject.Inject;

public class ServiceFactory {

    @Inject
    private HostService hostService;

    @Inject
    private ThreadCountService threadService;

    @Inject
    private ThreadInfoService threadInfoService;

    @Inject
    private SummaryService summaryService;

    public HostService getHostService() {
        return hostService;
    }

    public ThreadCountService getThreadService() {
        return threadService;
    }

    public ThreadInfoService getThreadInfoService() {
        return threadInfoService;
    }

    public SummaryService getSummaryService() {
        return summaryService;
    }
}
