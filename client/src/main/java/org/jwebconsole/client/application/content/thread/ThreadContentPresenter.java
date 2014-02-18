package org.jwebconsole.client.application.content.thread;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import org.jwebconsole.client.application.ApplicationPresenter;
import org.jwebconsole.client.model.thread.ThreadCountEntity;
import org.jwebconsole.client.model.thread.ThreadCountListResponse;
import org.jwebconsole.client.place.AppParams;
import org.jwebconsole.client.place.NameTokens;
import org.jwebconsole.client.service.SuccessCallback;

import java.util.List;

public class ThreadContentPresenter extends Presenter<ThreadContentView, ThreadContentPresenter.ThreadContentProxy>
        implements ThreadContentUiHandlers {

    private final ThreadContentPresenterFacade facade;

    @Inject
    public ThreadContentPresenter(EventBus eventBus, ThreadContentView view, ThreadContentProxy proxy, ThreadContentPresenterFacade facade) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_CONTENT_PANEL);
        this.facade = facade;
        view.setUiHandlers(this);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.thread)
    public interface ThreadContentProxy extends ProxyPlace<ThreadContentPresenter> {

    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String hostId = request.getParameter(AppParams.HOST_ID, "").trim();
        if (hostId.equals("")) {
            facade.printEmptyHostIdMessage();
            facade.redirectToErrorPlace();
        } else {
            makeThreadCountInfoRequest(hostId);
        }
    }

    private void makeThreadCountInfoRequest(String hostId) {
        facade.makeThreadCountRequest(hostId, new SuccessCallback<ThreadCountListResponse>() {
            @Override
            public void onSuccess(ThreadCountListResponse response) {
                processThreadCountInfoResponse(response.getBody());
            }
        });
    }

    private void processThreadCountInfoResponse(List<ThreadCountEntity> entities) {
        getView().populateChart(entities);
    }
}
