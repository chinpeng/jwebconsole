package org.jwebconsole.client.place;

import java.util.ArrayList;
import java.util.List;

public class TabNameTokensHolder {

    private List<String> tabNameTokens = new ArrayList<String>();

    public TabNameTokensHolder() {
        for (ContentTabs tab : ContentTabs.values()) {
            tabNameTokens.add(tab.getNameToken());
        }
    }


    public boolean isTabNameToken(String token) {
        return token != null && tabNameTokens.contains(token);
    }

}
