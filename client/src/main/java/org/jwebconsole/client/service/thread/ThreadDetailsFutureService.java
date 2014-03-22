package org.jwebconsole.client.service.thread;

import org.jwebconsole.client.model.thread.details.ThreadDetailsListResponse;
import org.jwebconsole.client.util.monad.future.Future;

public class ThreadDetailsFutureService {

    private ThreadDetailsService service;

    public ThreadDetailsFutureService(ThreadDetailsService service) {
        this.service = service;
    }


    public Future<ThreadDetailsListResponse> getThreadDetails(String hostId, Long threadId) {
        return new Future<>((callback) -> service.getThreadDetails(hostId, threadId, callback));
    }
}
