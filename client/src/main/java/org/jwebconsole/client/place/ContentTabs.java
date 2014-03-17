package org.jwebconsole.client.place;

import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.TabDataBasic;

public enum ContentTabs {

    OVERVIEW_TAB(1),
    MEMORY_TAB(2),
    SUMMARY_TAB(3),
    THREAD_TAB(4);


    private Integer priority;

    ContentTabs(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }

    public TabData toTabData(String label) {
        return new TabDataBasic(label, priority);
    }


}
