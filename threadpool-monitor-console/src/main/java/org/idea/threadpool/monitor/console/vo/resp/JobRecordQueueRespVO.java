package org.idea.threadpool.monitor.console.vo.resp;

/**
 * @Author linhao
 * @Date created in 10:34 下午 2022/9/17
 */
public class JobRecordQueueRespVO {

    private String poolName;
    private String ipAndPort;
    private String applicationName;
    private Integer length;
    private String tagName;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getIpAndPort() {
        return ipAndPort;
    }

    public void setIpAndPort(String ipAndPort) {
        this.ipAndPort = ipAndPort;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
