package org.jwebconsole.client.util;

import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class PlaceRequestUtils {

    public static PlaceRequest getPlaceRequestWithReplacedToken(PlaceRequest source, String newNameToken) {
        PlaceRequest.Builder builder = new PlaceRequest.Builder();
        builder.nameToken(newNameToken);
        for (String parameter: source.getParameterNames()) {
            String paramValue = source.getParameter(parameter, "");
            builder.with(parameter, paramValue);
        }
        return builder.build();
    }

}
