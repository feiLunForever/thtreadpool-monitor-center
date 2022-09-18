package org.idea.threadpool.monitor.console.vo.resp;

/**
 * @Author linhao
 * @Date created in 4:25 下午 2022/9/11
 */
public class ThreadPoolRealTimeRespVO {

    private String poolName;
    private Integer maximumPoolSize;
    private Integer corePoolSize;
    private Integer activePoolSize;
    private Long keepAliveTime;
    private Integer queueCapacity;
    private Integer queueSize;
    private String rejectedExecutionType;
    private String taskCountScoreThreshold;
    private Integer corePoolLoad;
    private Integer queueSizeLoad;

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

    public Integer getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
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

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Integer getCorePoolLoad() {
        return corePoolLoad;
    }

    public void setCorePoolLoad(Integer corePoolLoad) {
        this.corePoolLoad = corePoolLoad;
    }

    public Integer getQueueSizeLoad() {
        return queueSizeLoad;
    }

    public void setQueueSizeLoad(Integer queueSizeLoad) {
        this.queueSizeLoad = queueSizeLoad;
    }
}
