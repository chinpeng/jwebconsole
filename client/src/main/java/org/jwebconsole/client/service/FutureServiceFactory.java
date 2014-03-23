package org.jwebconsole.client.service;

import com.google.inject.Inject;
import org.jwebconsole.client.service.host.HostFutureService;
import org.jwebconsole.client.service.summary.SummaryFutureService;
import org.jwebconsole.client.service.thread.ThreadCountFutureService;
import org.jwebconsole.client.service.thread.ThreadDetailsFutureService;
import org.jwebconsole.client.service.thread.ThreadInfoFutureService;

public class FutureServiceFactory {

    @Inject
    private HostFutureService hostService;

    @Inject
    private SummaryFutureService summaryService;

    @Inject
    private ThreadCountFutureService threadCountService;

    @Inject
    private ThreadDetailsFutureService threadDetailsService;

    @Inject
    private ThreadInfoFutureService threadInfoService;

    public HostFutureService getHostService() {
        return hostService;
    }

    public SummaryFutureService getSummaryService() {
        return summaryService;
    }

    public ThreadCountFutureService getThreadCountService() {
        return threadCountService;
    }

    public ThreadDetailsFutureService getThreadDetailsService() {
        return threadDetailsService;
    }

    public ThreadInfoFutureService getThreadInfoService() {
        return threadInfoService;
    }
}
