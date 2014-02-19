package org.jwebconsole.client.application.content.thread;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.application.main.ApplicationPresenter;
import org.jwebconsole.client.application.main.event.HideContentMaskEvent;
import org.jwebconsole.client.application.main.event.ShowContentMaskEvent;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.jwebconsole.client.place.AppParams;
import org.jwebconsole.client.place.NameTokens;

public class ThreadContentPresenter extends Presenter<ThreadContentView, ThreadContentPresenter.ThreadContentProxy>
        implements ThreadContentUiHandlers {

    private final ThreadContentPresenterFacade facade;

    public static final Object THREAD_CHART_WIDGET_SLOT = new Object();

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
            processInvalidHostId();
        } else {
            makeHostRequest(hostId);
        }
    }

    private void makeHostRequest(String hostId) {
        getEventBus().fireEvent(new ShowContentMaskEvent());
        facade.makeHostRequest(hostId, new MethodCallback<HostConnectionResponse>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                processFailure(throwable);
            }

            @Override
            public void onSuccess(Method method, HostConnectionResponse hostConnectionResponse) {
                processSuccess(hostConnectionResponse);
            }
        });
    }

    private void processSuccess(HostConnectionResponse hostConnectionResponse) {
        getEventBus().fireEvent(new HideContentMaskEvent());
        if (hostConnectionResponse.isError()) {
            processInvalidHostId();
        } else {
            processResponse(hostConnectionResponse.getBody());
        }
    }

    private void processResponse(HostConnection body) {
        facade.revealThreadCountChartPresenter(this, body);
    }

    private void processFailure(Throwable throwable) {
        getEventBus().fireEvent(new HideContentMaskEvent());
        facade.printError(throwable);
    }

    private void processInvalidHostId() {
        facade.printHosNotExistsMessage();
        facade.redirectToErrorPlace();
    }

}
