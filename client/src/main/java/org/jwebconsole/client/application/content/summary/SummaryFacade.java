package org.jwebconsole.client.application.content.summary;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.HasSlots;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.application.content.thread.ThreadContentPresenter;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.summary.SummaryResponse;
import org.jwebconsole.client.place.AppParams;
import org.jwebconsole.client.service.ServiceFactory;

/**
 * Created by amednikov
 * Date: 07.03.14
 * Time: 14:53
 */
public class SummaryFacade {

    ServiceFactory serviceFactory;
    private PlaceManager placeManager;

    @Inject
    public SummaryFacade(ServiceFactory serviceFactory, PlaceManager placeManager) {
        this.serviceFactory = serviceFactory;
        this.placeManager = placeManager;
    }

    public void makeSummaryRequest(String hostId, MethodCallback<SummaryResponse> callback){
        serviceFactory.getSummaryService().getSummary(hostId, callback);
    }

    public String getCurrentConnectionId() {
        return placeManager.getCurrentPlaceRequest().getParameter(AppParams.HOST_ID, null);
    }
}
