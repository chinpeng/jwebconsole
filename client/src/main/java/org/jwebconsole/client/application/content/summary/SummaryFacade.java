package org.jwebconsole.client.application.content.summary;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.HasSlots;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.application.content.thread.ThreadContentPresenter;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.summary.SummaryResponse;
import org.jwebconsole.client.place.AppParams;
import org.jwebconsole.client.service.FutureServiceFactory;
import org.jwebconsole.client.service.ServiceFactory;
import org.jwebconsole.client.util.monad.future.Future;
import org.jwebconsole.client.util.monad.option.Option;

/**
 * Created by amednikov
 * Date: 07.03.14
 * Time: 14:53
 */
public class SummaryFacade {

    private final FutureServiceFactory serviceFactory;
    private PlaceManager placeManager;

    @Inject
    public SummaryFacade(PlaceManager placeManager, FutureServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        this.placeManager = placeManager;
    }

    public Future<SummaryResponse> getSummary(String hostId){
        return serviceFactory.getSummaryService().getSummary(hostId);
    }

    public Option<String> getCurrentConnectionId() {
        String connectionId = placeManager.getCurrentPlaceRequest().getParameter(AppParams.HOST_ID, null);
        return Option.create(connectionId);
    }
}
