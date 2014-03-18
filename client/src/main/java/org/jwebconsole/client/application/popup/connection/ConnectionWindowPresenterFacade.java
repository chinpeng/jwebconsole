package org.jwebconsole.client.application.popup.connection;

import com.google.inject.Inject;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.application.popup.connection.state.ConnectionController;
import org.jwebconsole.client.application.popup.connection.state.ConnectionControllerState;
import org.jwebconsole.client.bundle.AppErrorId;
import org.jwebconsole.client.bundle.messages.Messages;
import org.jwebconsole.client.common.InfoHolder;
import org.jwebconsole.client.model.base.ErrorMessage;
import org.jwebconsole.client.model.base.ValidationMessage;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.jwebconsole.client.place.AppParams;
import org.jwebconsole.client.service.ServiceFactory;

import java.util.List;

public class ConnectionWindowPresenterFacade {

    private InfoHolder infoHolder;
    private ConnectionWindowValidator validator;
    private ConnectionControllerState state;
    private ServiceFactory serviceFactory;
    private PlaceManager placeManager;

    @Inject
    public ConnectionWindowPresenterFacade(InfoHolder infoHolder,
                                           ConnectionWindowValidator validator,
                                           ConnectionControllerState state,
                                           ServiceFactory serviceFactory,
                                           PlaceManager placeManager) {
        this.infoHolder = infoHolder;
        this.validator = validator;
        this.state = state;
        this.serviceFactory = serviceFactory;
        this.placeManager = placeManager;
    }

    public void displayError(ErrorMessage error) {
        infoHolder.printError(error);
    }

    public void displayUnknownError() {
        infoHolder.printError(new ErrorMessage(AppErrorId.UNKNOWN_ERROR.getId(), ""));
    }


    public void validate(List<ValidationMessage> validations) {
        validator.validateView(validations);
    }

    public void becomeCreateConnectionController() {
        state.becomeCreateConnectionController();
    }

    public void becomeEditConnectionController(HostConnection connection) {
        state.becomeEditConnectionController(connection);
    }

    public ConnectionController getController() {
        return state.getController();
    }

    public void makeHostRequest(MethodCallback<HostConnectionResponse> callback) {
        String hostId = placeManager.getCurrentPlaceRequest().getParameter(AppParams.HOST_ID, null);
        if (hostId != null) {
            serviceFactory.getHostService().getHost(hostId, callback);
        }
    }

}
