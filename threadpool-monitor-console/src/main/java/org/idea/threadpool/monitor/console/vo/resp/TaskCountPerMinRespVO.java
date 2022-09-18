package org.idea.threadpool.monitor.console.vo.resp;

/**
 * @Author linhao
 * @Date created in 2:13 下午 2022/9/10
 */
public class TaskCountPerMinRespVO {

    private String recordTime;

    private Integer times;

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
