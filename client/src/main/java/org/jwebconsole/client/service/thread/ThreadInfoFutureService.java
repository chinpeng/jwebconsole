package org.jwebconsole.client.service.thread;

import org.jwebconsole.client.model.thread.info.ThreadInfoListResponse;
import org.jwebconsole.client.util.monad.future.Future;

public class ThreadInfoFutureService {

    private ThreadInfoService service;

    public ThreadInfoFutureService(ThreadInfoService service) {
        this.service = service;
    }

    public Future<ThreadInfoListResponse> getThreadInfo(String hostId) {
        return new Future<>((callback) -> service.getThreadInfo(hostId, callback));
    }
}
