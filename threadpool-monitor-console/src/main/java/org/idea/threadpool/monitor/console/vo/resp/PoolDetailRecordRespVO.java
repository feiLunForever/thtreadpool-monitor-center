package org.idea.threadpool.monitor.console.vo.resp;

/**
 * @Author linhao
 * @Date created in 6:55 下午 2022/9/9
 */
public class PoolDetailRecordRespVO {

    private Integer activePoolSize;

    private Integer queueSize;

    private String recordTime;

    public Integer getActivePoolSize() {
        return activePoolSize;
    }

    public void setActivePoolSize(Integer activePoolSize) {
        this.activePoolSize = activePoolSize;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }
}
