package org.jwebconsole.client.application.links;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class LinksView extends ViewImpl implements LinksPresenter.MyView {
  interface Binder extends UiBinder<Widget, LinksView> {
  }
  
  @Inject
  LinksView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));
  }

}
