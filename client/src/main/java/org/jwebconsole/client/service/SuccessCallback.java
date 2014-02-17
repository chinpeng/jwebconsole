package org.jwebconsole.client.service;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.event.GlobalEventBusHolder;
import org.jwebconsole.client.event.info.PrintInfoEvent;
import org.jwebconsole.client.model.base.BaseResponse;

public abstract class SuccessCallback<T extends BaseResponse<?>> implements MethodCallback<T> {


    public void beforeResponse() {

    }

    @Override
    public void onFailure(Method method, Throwable throwable) {
        beforeResponse();
        fireInfoEvent("Error", throwable.getMessage());
    }

    private void fireInfoEvent(String title, String message) {
        GlobalEventBusHolder.getEventBus().fireEvent(new PrintInfoEvent(title, message));
    }

    @Override
    public void onSuccess(Method method, T response) {
        beforeResponse();
        if (response.isError()) {
            fireInfoEvent("Error", response.getError());
        } else {
            onSuccess(response);
        }
    }

    public abstract void onSuccess(T response);

}
