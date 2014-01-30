package org.jwebconsole.client.application.popup.connection;

import com.google.inject.Inject;
import com.sencha.gxt.widget.core.client.info.Info;
import org.fusesource.restygwt.client.MethodCallback;

import org.jwebconsole.client.bundle.AppValidationId;
import org.jwebconsole.client.bundle.ValidationMessageConverter;
import org.jwebconsole.client.bundle.messages.Messages;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.jwebconsole.client.service.ServiceFactory;

public class ConnectionWindowPresenterFacade {

    private Messages messages;
    private ServiceFactory serviceFactory;
    private ValidationMessageConverter converter;

    @Inject
    public ConnectionWindowPresenterFacade(Messages messages, ServiceFactory serviceFactory, ValidationMessageConverter converter) {
        this.messages = messages;
        this.serviceFactory = serviceFactory;
        this.converter = converter;
    }

    public String getMaskMessage() {
        return messages.loadingMaskText();
    }

    public void connect(HostConnection connection, MethodCallback<HostConnectionResponse> callback) {
        serviceFactory.getHostService().addNewHost(connection, callback);
    }


    public void displayError(String error) {
        Info.display(messages.errorInfoTitle(), error);
    }

    public String getMessage(AppValidationId appValidationId) {
        return converter.getMessage(appValidationId);
    }

}