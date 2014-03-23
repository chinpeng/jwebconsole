package org.jwebconsole.client.common;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.sencha.gxt.widget.core.client.info.Info;
import org.jwebconsole.client.bundle.AppErrorId;
import org.jwebconsole.client.bundle.ErrorMessageConverter;
import org.jwebconsole.client.bundle.messages.ErrorMessages;
import org.jwebconsole.client.event.info.PrintInfoEvent;
import org.jwebconsole.client.event.info.PrintInfoEventHandler;
import org.jwebconsole.client.model.base.ErrorMessage;

public class InfoHolder implements PrintInfoEventHandler {

    private EventBus eventBus;
    private ErrorMessageConverter errorMessageConverter;
    private ErrorMessages errorMessages;

    @Inject
    public InfoHolder(EventBus eventBus, ErrorMessageConverter errorMessageConverter, ErrorMessages errorMessages) {
        this.eventBus = eventBus;
        this.errorMessageConverter = errorMessageConverter;
        this.errorMessages = errorMessages;
        eventBus.addHandler(PrintInfoEvent.TYPE, this);
    }

    public void printInfo(String title, String text) {
        Info.display(title, text);
    }

    public void printInfo(String text) {
        Info.display("", text);
    }

    @Override
    public void onEventReceived(PrintInfoEvent event) {
        if (event.getErrorMessage() != null) {
            printError(event.getErrorMessage());
        } else {
            printInfo(event.getTitle(), event.getText());
        }
    }

    public void printUnknownError() {
        printInfo(errorMessages.unknownError());
    }

    public void printError(ErrorMessage errorMessage) {
        Info.display(errorMessages.errorMessageTitle(), errorMessageConverter.getMessage(AppErrorId.fromId(errorMessage.getId())));
    }

}
