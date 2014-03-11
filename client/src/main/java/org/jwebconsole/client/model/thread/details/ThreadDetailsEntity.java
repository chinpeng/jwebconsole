package org.jwebconsole.client.model.thread.details;

public class ThreadDetailsEntity {

    private String hostId;
    private Long threadId;
    private String stackTraceElement;

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public String getStackTraceElement() {
        return stackTraceElement;
    }

    public void setStackTraceElement(String stackTraceElement) {
        this.stackTraceElement = stackTraceElement;
    }
}
