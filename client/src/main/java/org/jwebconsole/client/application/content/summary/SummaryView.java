package org.jwebconsole.client.application.content.summary;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;

/**
 * Created by DrFaust
 * Date: 05.03.14
 * Time: 22:21
 */
public interface SummaryView extends View, HasUiHandlers<SummaryUiHandlers> {
    public void setName(String name);
    public void setVersion(String version);
    public void setArchitecture(String architecture);
    public void setSystemLoadAverage(Double systemLoadAverage);
    public void setAvailableProcessors(Integer availableProcessors);
    public void setProcessCPUTime(Long processCPUTime);
    public void setCommittedVirtualMemorySize(Long committedVirtualMemorySize);
    public void setTotalPhysicalMemorySize(Long totalPhysicalMemorySize);
    public void setFreePhysicalMemorySize(Long freePhysicalMemorySize);
    public void setTotalSwapSpaceSize(Long totalSwapSpaceSize);
    public void setFreeSwapSpaceSize(Long freeSwapSpaceSize);
}
