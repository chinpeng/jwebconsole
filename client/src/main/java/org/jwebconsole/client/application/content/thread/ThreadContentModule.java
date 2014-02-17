package org.jwebconsole.client.application.content.thread;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ThreadContentModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ThreadContentPresenter.class, ThreadContentView.class, ThreadContentViewImpl.class, ThreadContentPresenter.ThreadContentProxy.class);
    }
}
