package org.jwebconsole.client.service;

public interface SuccessCallback<T> {

    void onSuccess(T body);

}
