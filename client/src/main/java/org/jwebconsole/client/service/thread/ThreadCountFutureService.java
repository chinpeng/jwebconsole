package org.jwebconsole.client.service.thread;

import org.jwebconsole.client.model.thread.count.ThreadCountListResponse;
import org.jwebconsole.client.util.monad.future.Future;

public class ThreadCountFutureService {

    private ThreadCountService service;

    public ThreadCountFutureService(ThreadCountService service) {
        this.service = service;
    }

    public Future<ThreadCountListResponse> getThreadCount(String hostId) {
        return new Future<>((callback) -> service.getThreadCount(hostId, callback));
    }

    public Future<ThreadCountListResponse> getLastNumberOfThreadCount(Integer number, String hostId) {
        return new Future<>((callback) -> service.getLastNumberOfThreadCount(number, hostId, callback));
    }
}
