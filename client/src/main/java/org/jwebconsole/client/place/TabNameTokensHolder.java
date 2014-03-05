package org.jwebconsole.client.place;

import java.util.ArrayList;
import java.util.List;

public class TabNameTokensHolder {

    private List<String> tabNameTokens = new ArrayList<String>();

    public TabNameTokensHolder() {
        tabNameTokens.add(NameTokens.getThread());
        tabNameTokens.add(NameTokens.getMemory());
    }


    public boolean isTabNameToken(String token) {
        return token != null && tabNameTokens.contains(token);
    }

}
