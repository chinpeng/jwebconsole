package org.jwebconsole.client.application.content.summary;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.HasSlots;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.application.content.thread.ThreadContentPresenter;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.summary.SummaryResponse;
import org.jwebconsole.client.service.ServiceFactory;

/**
 * Created by amednikov
 * Date: 07.03.14
 * Time: 14:53
 */
public class SummaryFacade {

    ServiceFactory serviceFactory;

    @Inject
    public SummaryFacade(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public void makeSummaryRequest(String hostId, MethodCallback<SummaryResponse> callback){
        serviceFactory.getSummaryService().getSummary(hostId, callback);
    }
}
