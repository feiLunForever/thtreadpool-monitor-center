package org.idea.threadpool.monitor.report;


import com.alibaba.fastjson.JSON;

/**
 * @Author linhao
 * @Date created in 5:30 下午 2022/8/4
 */
public class ThreadPoolDetailInfo {

    private Integer port;
    private String applicationName;
    private String ip;
    private String poolName;
    private Integer maximumPoolSize;
    private Integer corePoolSize;
    private Integer activePoolSize;
    private Long keepAliveTime;
    private Integer queueCapacity;
    private Integer queueSize;
    private boolean preStartAllCoreThreads;
    private boolean preStartCoreThread;
    private long complexTaskCount;
    private long errorTaskNum;
    private String rejectedExecutionType;
    private String taskCountScoreThreshold;
    private String recordTime;

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

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getActivePoolSize() {
        return activePoolSize;
    }

    public void setActivePoolSize(Integer activePoolSize) {
        this.activePoolSize = activePoolSize;
    }

    public Long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Integer getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public boolean isPreStartAllCoreThreads() {
        return preStartAllCoreThreads;
    }

    public void setPreStartAllCoreThreads(boolean preStartAllCoreThreads) {
        this.preStartAllCoreThreads = preStartAllCoreThreads;
    }

    public boolean isPreStartCoreThread() {
        return preStartCoreThread;
    }

    public void setPreStartCoreThread(boolean preStartCoreThread) {
        this.preStartCoreThread = preStartCoreThread;
    }

    public String getRejectedExecutionType() {
        return rejectedExecutionType;
    }

    public void setRejectedExecutionType(String rejectedExecutionType) {
        this.rejectedExecutionType = rejectedExecutionType;
    }

    public String getTaskCountScoreThreshold() {
        return taskCountScoreThreshold;
    }

    public void setTaskCountScoreThreshold(String taskCountScoreThreshold) {
        this.taskCountScoreThreshold = taskCountScoreThreshold;
    }

    public void setComplexTaskCount(long complexTaskCount) {
        this.complexTaskCount = complexTaskCount;
    }

    public long getComplexTaskCount() {
        return complexTaskCount;
    }


    public void setErrorTaskNum(long errorTaskNum) {
        this.errorTaskNum = errorTaskNum;
    }

    public long getErrorTaskNum() {
        return errorTaskNum;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public String toJson(){
        return JSON.toJSONString(this);
    }
}
