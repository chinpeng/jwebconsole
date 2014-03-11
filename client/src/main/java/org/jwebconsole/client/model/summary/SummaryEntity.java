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
    private String architecture;
    private Double systemLoadAverage;
    private Integer availableProcessors;

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

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public Double getSystemLoadAverage() {
        return systemLoadAverage;
    }

    public void setSystemLoadAverage(Double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }

    public Integer getAvailableProcessors() {
        return availableProcessors;
    }

    public void setAvailableProcessors(Integer availableProcessors) {
        this.availableProcessors = availableProcessors;
    }

    @Override
    public String toString() {
        return "SummaryEntity{" +
                "hostId='" + hostId + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", architecture='" + architecture + '\'' +
                ", systemLoadAverage=" + systemLoadAverage +
                ", availableProcessors=" + availableProcessors +
                '}';
    }
}
