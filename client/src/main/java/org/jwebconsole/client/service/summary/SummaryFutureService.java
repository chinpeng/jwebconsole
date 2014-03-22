package org.jwebconsole.client.service.summary;

import com.google.inject.Inject;
import org.jwebconsole.client.model.summary.SummaryResponse;
import org.jwebconsole.client.util.monad.future.Future;

public class SummaryFutureService {

    private SummaryService summaryService;

    @Inject
    public SummaryFutureService(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    public Future<SummaryResponse> getSummary(String hostId) {
        return new Future<>((callback) -> summaryService.getSummary(hostId, callback));
    }
}
