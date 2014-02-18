package org.jwebconsole.client.application.content.thread;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.bundle.AppResources;
import org.jwebconsole.client.event.info.PrintInfoEvent;
import org.jwebconsole.client.model.thread.ThreadCountListResponse;
import org.jwebconsole.client.place.NameTokens;
import org.jwebconsole.client.service.ServiceFactory;

public class ThreadContentPresenterFacade {

    private EventBus eventBus;
    private AppResources resources;
    private ServiceFactory serviceFactory;
    private PlaceManager placeManager;

    @Inject
    public ThreadContentPresenterFacade(EventBus eventBus, AppResources resources, ServiceFactory serviceFactory, PlaceManager placeManager) {
        this.eventBus = eventBus;
        this.resources = resources;
        this.serviceFactory = serviceFactory;
        this.placeManager = placeManager;
    }

    public void printEmptyHostIdMessage() {
        eventBus.fireEvent(new PrintInfoEvent(resources.getMessages().hostIdIsEmpty()));
    }

    public void makeThreadCountRequest(String hostId, MethodCallback<ThreadCountListResponse> callback) {
        serviceFactory.getThreadService().getThreadInfo(hostId, callback);
    }

    public void redirectToErrorPlace() {
        placeManager.revealErrorPlace(NameTokens.thread);
    }

}
