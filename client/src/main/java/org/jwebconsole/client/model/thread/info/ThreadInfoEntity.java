package org.jwebconsole.client.model.thread.info;

public class ThreadInfoEntity {

    private Integer id;
    private Long threadId;
    private String hostId;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ThreadInfoEntity{" +
                "id=" + id +
                ", threadId=" + threadId +
                ", hostId='" + hostId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
