package org.jwebconsole.client.model.thread;

import java.util.Date;

public class ThreadCountEntity {

    private Integer id;
    private String hostId;
    private Date time;
    private Integer threadCount;
    private Integer peakThreadCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    public Integer getPeakThreadCount() {
        return peakThreadCount;
    }

    public void setPeakThreadCount(Integer peakThreadCount) {
        this.peakThreadCount = peakThreadCount;
    }
}
