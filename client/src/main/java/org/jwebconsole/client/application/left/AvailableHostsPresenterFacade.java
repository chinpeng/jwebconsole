package org.jwebconsole.client.application.left;

import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.model.host.HostConnectionListResponse;
import org.jwebconsole.client.place.AppParams;
import org.jwebconsole.client.place.NameTokens;
import org.jwebconsole.client.service.ServiceFactory;

public class AvailableHostsPresenterFacade {

    private final ServiceFactory serviceFactory;
    private PlaceManager placeManager;
    private Timer timer;

    @Inject
    public AvailableHostsPresenterFacade(ServiceFactory serviceFactory, PlaceManager placeManager) {
        this.serviceFactory = serviceFactory;
        this.placeManager = placeManager;
    }

    public void getHosts(MethodCallback<HostConnectionListResponse> callback) {
        serviceFactory.getHostService().getHostsStatus(callback);
    }

    public void scheduleReceiveHosts(final int time, final MethodCallback<HostConnectionListResponse> callback) {
        this.timer = new Timer() {
            @Override
            public void run() {
                serviceFactory.getHostService().getHostsStatus(callback);
            }
        };
        timer.scheduleRepeating(time);
    }

    public void revealThreadContentPlace(String hostId) {
        PlaceRequest request = new PlaceRequest.Builder()
                .nameToken(NameTokens.getThread())
                .with(AppParams.HOST_ID, hostId)
                .build();
        placeManager.revealPlace(request);
    }

    public void revealHomePlace() {
        placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.home).build());
    }

    public String getHostIdFromPlaceRequest() {
        return placeManager.getCurrentPlaceRequest().getParameter(AppParams.HOST_ID, "");
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
