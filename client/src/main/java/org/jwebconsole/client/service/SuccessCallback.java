package org.jwebconsole.client.service;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.bundle.AppErrorId;
import org.jwebconsole.client.event.GlobalEventBusHolder;
import org.jwebconsole.client.event.info.PrintInfoEvent;
import org.jwebconsole.client.model.base.BaseResponse;
import org.jwebconsole.client.model.base.ErrorMessage;

public abstract class SuccessCallback<T extends BaseResponse<?>> implements MethodCallback<T> {


    public void beforeResponse() {

    }

    @Override
    public void onFailure(Method method, Throwable throwable) {
        beforeResponse();
        fireInfoEvent(new ErrorMessage(AppErrorId.UNKNOWN_ERROR.getId(), ""));
    }

    private void fireInfoEvent(ErrorMessage errorMessage) {
        if (GlobalEventBusHolder.getEventBus() != null) {
            GlobalEventBusHolder.getEventBus().fireEvent(new PrintInfoEvent(errorMessage));
        }
    }

    @Override
    public void onSuccess(Method method, T response) {
        beforeResponse();
        if (response.isError()) {
            fireInfoEvent(response.getError());
        } else {
            onSuccess(response);
        }
    }

    public abstract void onSuccess(T response);

}
