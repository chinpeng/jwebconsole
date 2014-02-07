package org.jwebconsole.client.application.popup.connection;

import com.google.inject.Inject;
import com.sencha.gxt.widget.core.client.info.Info;
import org.fusesource.restygwt.client.MethodCallback;

import org.jwebconsole.client.application.popup.connection.state.ConnectionController;
import org.jwebconsole.client.application.popup.connection.state.ConnectionControllerState;
import org.jwebconsole.client.application.popup.connection.state.CreateConnectionController;
import org.jwebconsole.client.bundle.AppValidationId;
import org.jwebconsole.client.bundle.ValidationMessageConverter;
import org.jwebconsole.client.bundle.messages.Messages;
import org.jwebconsole.client.common.InfoHolder;
import org.jwebconsole.client.model.base.ValidationMessage;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.jwebconsole.client.service.ServiceFactory;

import java.util.List;

public class ConnectionWindowPresenterFacade {

    private Messages messages;
    private InfoHolder infoHolder;
    private ConnectionWindowViewValidator validator;
    private ConnectionControllerState state;

    @Inject
    public ConnectionWindowPresenterFacade(Messages messages, InfoHolder infoHolder,
                                           ConnectionWindowViewValidator validator, ConnectionControllerState state) {
        this.messages = messages;
        this.infoHolder = infoHolder;
        this.validator = validator;
        this.state = state;
    }

    public void displayError(String error) {
        infoHolder.printInfo(messages.errorInfoTitle(), error);
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
}
