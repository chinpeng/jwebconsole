package org.jwebconsole.client.service;

import com.google.inject.Inject;
import org.jwebconsole.client.service.host.HostService;
import org.jwebconsole.client.service.summary.SummaryService;
import org.jwebconsole.client.service.thread.ThreadCountService;
import org.jwebconsole.client.service.thread.ThreadDetailsService;
import org.jwebconsole.client.service.thread.ThreadInfoService;

public class ServiceFactory {

    @Inject
    private HostService hostService;

    @Inject
    private ThreadCountService threadService;

    @Inject
    private ThreadInfoService threadInfoService;

    @Inject
    private SummaryService summaryService;

    @Inject
    private ThreadDetailsService threadDetailsService;

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

    public ThreadDetailsService getThreadDetailsService() {
        return threadDetailsService;
    }
}
