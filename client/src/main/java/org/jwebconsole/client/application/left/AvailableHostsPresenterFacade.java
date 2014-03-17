package org.jwebconsole.client.application.left;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.application.content.main.event.TabHiddenEvent;
import org.jwebconsole.client.application.content.main.event.TabHiddenEventHandler;
import org.jwebconsole.client.application.content.main.event.TabRevealedEvent;
import org.jwebconsole.client.application.content.main.event.TabRevealedEventHandler;
import org.jwebconsole.client.model.host.HostConnectionListResponse;
import org.jwebconsole.client.place.AppParams;
import org.jwebconsole.client.place.NameTokens;
import org.jwebconsole.client.service.ServiceFactory;

public class AvailableHostsPresenterFacade implements TabRevealedEventHandler, TabHiddenEventHandler {

    private final ServiceFactory serviceFactory;
    private final EventBus eventBus;
    private PlaceManager placeManager;
    private Timer timer;
    private boolean tabRevealed;

    @Inject
    public AvailableHostsPresenterFacade(ServiceFactory serviceFactory, PlaceManager placeManager, EventBus eventBus) {
        this.serviceFactory = serviceFactory;
        this.placeManager = placeManager;
        this.eventBus = eventBus;
        initHandlers();
    }

    private void initHandlers() {
        eventBus.addHandler(TabRevealedEvent.TYPE, this);
        eventBus.addHandler(TabHiddenEvent.TYPE, this);
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

    public void rescheduleTimer() {
        if (timer != null) {
            timer.cancel();
            timer.run();
        }
    }

    public boolean isAtHomePlace() {
        return placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.home);
    }

    public void revealCurrentPlaceWithHostId(String hostId) {
        String nameToken = placeManager.getCurrentPlaceRequest().getNameToken();
        PlaceRequest request = new PlaceRequest.Builder()
                .nameToken(nameToken)
                .with(AppParams.HOST_ID, hostId)
                .build();
        placeManager.revealPlace(request);
    }

    @Override
    public void onTabRevealed(TabRevealedEvent event) {
        tabRevealed = true;
    }

    @Override
    public void onTabUnbind(TabHiddenEvent event) {
        tabRevealed = false;
    }


    public boolean isTabNameToken() {
        return tabRevealed;
    }
}
