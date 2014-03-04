package org.jwebconsole.client.model.summary;

/**
 * Created by amednikov
 * Date: 04.03.14
 * Time: 19:23
 */
public class SummaryEntity {
    private String hostId;
    private String name;
    private String version;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "SummaryEntity{" +
                "hostId='" + hostId + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
