package org.idea.threadpool.monitor.report;

/**
 * @Author linhao
 * @Date created in 4:28 下午 2022/9/11
 */
public class ThreadPoolRealTimeInfo {

    private String poolName;
    private Integer maximumPoolSize;
    private Integer corePoolSize;
    private Integer activePoolSize;
    private Long keepAliveTime;
    private Integer queueCapacity;
    private Integer queueSize;
    private long errorTaskCount;
    private long completedTaskCount;
    private boolean preStartAllCoreThreads;
    private boolean preStartCoreThread;
    private String rejectedExecutionType;
    private String taskCountScoreThreshold;


    public long getErrorTaskCount() {
        return errorTaskCount;
    }

    public void setErrorTaskCount(long errorTaskCount) {
        this.errorTaskCount = errorTaskCount;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
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

    public Integer getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
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
}
