package org.jwebconsole.client.place;

public enum ContentTabs {

    MEMORY_TAB(NameTokens.memory),
    THREAD_TAB(NameTokens.thread);

    private String nameToken;

    private ContentTabs(String nameToken) {
        this.nameToken = nameToken;
    }

    public String getNameToken() {
        return nameToken;
    }
}
