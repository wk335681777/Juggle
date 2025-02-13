package net.somta.juggle.core.model;

public class ServerInfo {
    private String ip;
    private String port;
    private long lastHeartBeatTime;
    private String protocol;
    public ServerInfo(String protocol, String ip, String port, long lastHeartBeatTime) {
        this.protocol = protocol;
        this.ip = ip;
        this.port = port;
        this.lastHeartBeatTime = lastHeartBeatTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public long getLastHeartBeatTime() {
        return lastHeartBeatTime;
    }

    public void setLastHeartBeatTime(long lastHeartBeatTime) {
        this.lastHeartBeatTime = lastHeartBeatTime;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
