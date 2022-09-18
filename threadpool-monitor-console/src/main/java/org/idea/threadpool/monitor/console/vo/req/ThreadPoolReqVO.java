package org.idea.threadpool.monitor.console.vo.req;

/**
 * @Author linhao
 * @Date created in 8:27 上午 2022/9/9
 */
public class ThreadPoolReqVO {

    private String applicationName;
    private String poolName;
    private String ip;
    private Integer port;
    private String tag;
    private String ipAndPort;
    private String queryDate;
    private String jobId;
    private Integer page;
    private Integer pageSize;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }

    public String getIpAndPort() {
        return ipAndPort;
    }

    public void setIpAndPort(String ipAndPort) {
        this.ipAndPort = ipAndPort;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "ThreadPoolReqVO{" +
                "applicationName='" + applicationName + '\'' +
                ", tag='" + tag + '\'' +
                ", jobId='" + jobId + '\'' +
                ", poolName='" + poolName + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", ipAndPort='" + ipAndPort + '\'' +
                '}';
    }
}
