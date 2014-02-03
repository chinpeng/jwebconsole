package org.jwebconsole.client.service;

import com.sencha.gxt.widget.core.client.info.Info;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.model.base.BaseResponse;

public abstract class SuccessCallback<T extends BaseResponse<?>> implements MethodCallback<T> {

    public void beforeResponse() {

    }

    @Override
    public void onFailure(Method method, Throwable throwable) {
        beforeResponse();
        Info.display("Error", throwable.getMessage());
    }

    @Override
    public void onSuccess(Method method, T response) {
        beforeResponse();
        if (response.isError()) {
            Info.display("Error", response.getError());
        } else {
            onSuccess(response);
        }
    }

    public abstract void onSuccess(T response);

}
