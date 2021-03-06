package org.jwebconsole.client.bundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface AppIcons extends ClientBundle {

    AppIcons INSTANCE = GWT.create(AppIcons.class);

    @Source("img/icons/icon-add.png")
    ImageResource iconAdd();

    @Source("img/icons/icon-edit.png")
    ImageResource iconEdit();

    @Source("img/icons/icon-delete.png")
    ImageResource iconDelete();

    @Source("img/icons/icon-host-on.png")
    ImageResource iconHostOn();

    @Source("img/icons/icon-host-off.png")
    ImageResource iconHostOff();

}
