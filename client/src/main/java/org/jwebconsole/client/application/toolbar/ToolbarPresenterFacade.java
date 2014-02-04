package org.jwebconsole.client.application.toolbar;

import com.google.inject.Inject;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.bundle.AppValidationId;
import org.jwebconsole.client.bundle.ValidationMessageConverter;
import org.jwebconsole.client.common.InfoHolder;
import org.jwebconsole.client.model.base.SimpleResponse;
import org.jwebconsole.client.model.base.ValidationMessage;
import org.jwebconsole.client.service.ServiceFactory;

import java.util.List;

public class ToolbarPresenterFacade {

    private ServiceFactory serviceFactory;
    private ValidationMessageConverter converter;
    private InfoHolder infoHolder;

    @Inject
    public ToolbarPresenterFacade(ServiceFactory serviceFactory, ValidationMessageConverter converter, InfoHolder infoHolder) {
        this.serviceFactory = serviceFactory;
        this.converter = converter;
        this.infoHolder = infoHolder;
    }

    public void deleteHost(String hostId, MethodCallback<SimpleResponse> callback) {
        serviceFactory.getHostService().delete(hostId, callback);
    }

    public void printValidationMessages(List<ValidationMessage> messages) {
        for (ValidationMessage validationMessage : messages) {
            AppValidationId id = AppValidationId.fromId(validationMessage.getId());
            String message = converter.getMessage(id);
            infoHolder.printInfo(message);
        }
    }

}
