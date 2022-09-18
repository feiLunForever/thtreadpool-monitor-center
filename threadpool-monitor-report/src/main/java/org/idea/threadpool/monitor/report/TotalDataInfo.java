package org.idea.threadpool.monitor.report;

/**
 * @Author linhao
 * @Date created in 9:46 下午 2022/9/7
 */
public class TotalDataInfo {

    private String ip;
    private String poolName;
    private String applicationName;
    private Integer port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
