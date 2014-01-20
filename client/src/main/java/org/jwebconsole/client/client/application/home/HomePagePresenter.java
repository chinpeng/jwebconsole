package org.jwebconsole.client.client.application.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.client.application.ApplicationPresenter;
import org.jwebconsole.client.client.place.NameTokens;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import org.jwebconsole.client.client.sandbox.HostService;
import org.jwebconsole.client.client.sandbox.HostStatus;

public class HomePagePresenter extends Presenter<HomePagePresenter.MyView, HomePagePresenter.MyProxy> {
    public interface MyView extends View {
        HasClickHandlers getButton();
    }

    @ProxyStandard
    @NameToken(NameTokens.home)
    public interface MyProxy extends ProxyPlace<HomePagePresenter> {
    }

    @Inject
    public HomePagePresenter(EventBus eventBus, MyView view, MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetCenter);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        getView().getButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                HostService hostService = GWT.create(HostService.class);
                hostService.getHostStatus("localhost", 8080, new MethodCallback<HostStatus>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {
                        Window.alert(throwable.toString());
                    }

                    @Override
                    public void onSuccess(Method method, HostStatus hostStatus) {
                        Window.alert("" + hostStatus.getAvailable());
                    }
                });
            }
        });
    }
}
