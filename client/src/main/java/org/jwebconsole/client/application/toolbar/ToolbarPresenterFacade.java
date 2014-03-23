package org.jwebconsole.client.application.toolbar;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.bundle.AppValidationId;
import org.jwebconsole.client.bundle.ValidationMessageConverter;
import org.jwebconsole.client.common.InfoHolder;
import org.jwebconsole.client.model.base.SimpleResponse;
import org.jwebconsole.client.model.base.ValidationMessage;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.jwebconsole.client.place.AppParams;
import org.jwebconsole.client.place.NameTokens;
import org.jwebconsole.client.service.FutureServiceFactory;
import org.jwebconsole.client.service.ServiceFactory;
import org.jwebconsole.client.util.monad.future.Future;
import org.jwebconsole.client.util.monad.option.Option;

import java.util.List;

public class ToolbarPresenterFacade {

    private ValidationMessageConverter converter;
    private InfoHolder infoHolder;
    private PlaceManager placeManager;
    private FutureServiceFactory futureServiceFactory;

    @Inject
    public ToolbarPresenterFacade(ServiceFactory serviceFactory,
                                  ValidationMessageConverter converter,
                                  InfoHolder infoHolder,
                                  PlaceManager placeManager,
                                  FutureServiceFactory futureServiceFactory) {
        this.converter = converter;
        this.infoHolder = infoHolder;
        this.placeManager = placeManager;
        this.futureServiceFactory = futureServiceFactory;
    }

    public Future<SimpleResponse> deleteHost(String hostId) {
        return futureServiceFactory.getHostService().delete(hostId);
    }

    public void printValidationMessages(List<ValidationMessage> messages) {
        for (ValidationMessage validationMessage : messages) {
            AppValidationId id = AppValidationId.fromId(validationMessage.getId());
            String message = converter.getMessage(id);
            infoHolder.printInfo(message);
        }
    }

    public void printUnknownErrorMessage() {
        infoHolder.printUnknownError();
    }

    public void redirectToHome() {
        placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.home).build());
    }

    public Option<String> getCurrentConnectionId() {
        String connectionId = placeManager.getCurrentPlaceRequest().getParameter(AppParams.HOST_ID, null);
        return Option.create(connectionId);
    }

}
