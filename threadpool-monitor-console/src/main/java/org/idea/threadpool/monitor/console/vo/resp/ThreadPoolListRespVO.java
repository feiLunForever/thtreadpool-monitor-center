package org.idea.threadpool.monitor.console.vo.resp;

/**
 * @Author linhao
 * @Date created in 10:41 下午 2022/9/7
 */
public class ThreadPoolListRespVO {

    private String poolName;

    private String ip;

    private Integer port;

    private Object threadPoolDetail;

    public Object getThreadPoolDetail() {
        return threadPoolDetail;
    }

    public void setThreadPoolDetail(Object threadPoolDetail) {
        this.threadPoolDetail = threadPoolDetail;
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

    @Override
    public String toString() {
        return "ThreadPoolListBO{" +
                "poolName='" + poolName + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
