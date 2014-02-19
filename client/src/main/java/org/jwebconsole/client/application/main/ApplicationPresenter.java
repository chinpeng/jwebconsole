package org.jwebconsole.client.application.main;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import org.jwebconsole.client.application.main.event.HideContentMaskEvent;
import org.jwebconsole.client.application.main.event.HideContentMaskEventHandler;
import org.jwebconsole.client.application.main.event.ShowContentMaskEvent;
import org.jwebconsole.client.application.main.event.ShowContentMaskEventHandler;
import org.jwebconsole.client.event.RevealOnStartEvent;
import org.jwebconsole.client.place.NameTokens;

public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.ApplicationProxy>
        implements ShowContentMaskEventHandler,
        HideContentMaskEventHandler {

    public interface MyView extends View {

        void showContentMask();

        void hideContentMask();

    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_TOOLBAR = new Type<RevealContentHandler<?>>();

    @ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_LEFT_PANEL = new Type<RevealContentHandler<?>>();

    @ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_CONTENT_PANEL = new Type<RevealContentHandler<?>>();


    @ProxyStandard
    @NameToken(NameTokens.home)
    public interface ApplicationProxy extends ProxyPlace<ApplicationPresenter> {
    }

    @Inject
    public ApplicationPresenter(EventBus eventBus, MyView view, ApplicationProxy proxy, ApplicationInitializer initializer) {
        super(eventBus, view, proxy, RevealType.Root);
        initializer.init();
        register();
    }

    private void register() {
        getEventBus().addHandler(ShowContentMaskEvent.TYPE, this);
        getEventBus().addHandler(HideContentMaskEvent.TYPE, this);
    }


    @Override
    protected void onReveal() {
        super.onReveal();
        getEventBus().fireEvent(new RevealOnStartEvent());
    }

    @Override
    public void onHideContentMask(HideContentMaskEvent event) {
        getView().hideContentMask();
    }

    @Override
    public void onShowContentMask(ShowContentMaskEvent event) {
        getView().showContentMask();
    }



}
