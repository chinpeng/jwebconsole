package org.jwebconsole.client.application.content.memory;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class MemoryModule extends AbstractPresenterModule{

    @Override
    protected void configure() {
        bindPresenter(MemoryPresenter.class, MemoryView.class, MemoryViewImpl.class, MemoryPresenter.MemoryProxy.class);
    }
}
