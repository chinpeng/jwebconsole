package org.jwebconsole.client.bundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface AppResources extends ClientBundle {

    AppResources INSTANCE =  GWT.create(AppResources.class);

    @Source("sampleresource.css")
    SampleResource sampleResource();

}
