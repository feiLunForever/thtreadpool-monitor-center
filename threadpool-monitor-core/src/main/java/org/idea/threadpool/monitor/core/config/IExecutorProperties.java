package org.idea.threadpool.monitor.core.config;

/**
 * @Author linhao
 * @Date created in 5:42 下午 2022/8/4
 */
public class IExecutorProperties {

    private String corePoolSize;

    private String maximumPoolSize;

    private String keepAliveTime;

    private String queueCapacity;

    private String rejectedExecutionType;

    /**
     * 队列在执行任务数/队列总长度 阈值
     */
    private String taskCountScoreThreshold;

    /**
     * 预先开启全部核心线程
     */
    private String preStartAllCoreThreads;

    /**
     * 预先启动所有核心线程
     */
    private String preStartCoreThread;

    /**
     * 核心线程空闲超时之后被回收
     */
    private String allowsCoreThreadTimeOut;

    /**
     * 如果开启则对该线程池开启告警功能
     */
    private String alarmStatus;

    /**
     * 任务标签最大存储容积
     */
    private String maxTagRecordSize;

    private String watcher;


    public String getWatcher() {
        return watcher;
    }

    public void setWatcher(String watcher) {
        this.watcher = watcher;
    }

    public String getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(String corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public String getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(String maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public String getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(String keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public String getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(String queueCapacity) {
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

    public String getPreStartAllCoreThreads() {
        return preStartAllCoreThreads;
    }

    public void setPreStartAllCoreThreads(String preStartAllCoreThreads) {
        this.preStartAllCoreThreads = preStartAllCoreThreads;
    }

    public String getPreStartCoreThread() {
        return preStartCoreThread;
    }

    public void setPreStartCoreThread(String preStartCoreThread) {
        this.preStartCoreThread = preStartCoreThread;
    }

    public String getAllowsCoreThreadTimeOut() {
        return allowsCoreThreadTimeOut;
    }

    public void setAllowsCoreThreadTimeOut(String allowsCoreThreadTimeOut) {
        this.allowsCoreThreadTimeOut = allowsCoreThreadTimeOut;
    }

    public String getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getMaxTagRecordSize() {
        return maxTagRecordSize;
    }

    public void setMaxTagRecordSize(String maxTagRecordSize) {
        this.maxTagRecordSize = maxTagRecordSize;
    }

    @Override
    public String toString() {
        return "IExecutorProperties{" +
                "corePoolSize='" + corePoolSize + '\'' +
                ", maximumPoolSize='" + maximumPoolSize + '\'' +
                ", keepAliveTime='" + keepAliveTime + '\'' +
                ", queueCapacity='" + queueCapacity + '\'' +
                ", rejectedExecutionType='" + rejectedExecutionType + '\'' +
                ", taskCountScoreThreshold='" + taskCountScoreThreshold + '\'' +
                ", preStartAllCoreThreads='" + preStartAllCoreThreads + '\'' +
                ", preStartCoreThread='" + preStartCoreThread + '\'' +
                ", allowsCoreThreadTimeOut='" + allowsCoreThreadTimeOut + '\'' +
                ", alarmStatus='" + alarmStatus + '\'' +
                ", maxTagRecordSize='" + maxTagRecordSize + '\'' +
                '}';
    }
}
