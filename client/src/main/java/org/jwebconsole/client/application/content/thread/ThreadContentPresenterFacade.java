package org.jwebconsole.client.application.content.thread;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class ThreadContentPresenterFacade {

    private PlaceManager placeManager;

    @Inject
    public ThreadContentPresenterFacade(PlaceManager placeManager) {
        this.placeManager = placeManager;
    }
}
