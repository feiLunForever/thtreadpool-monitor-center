package org.idea.threadpool.monitor.console.vo.resp;


import org.idea.threadpool.monitor.report.ThreadPoolDetailInfo;

import java.util.Arrays;

/**
 * @Author linhao
 * @Date created in 6:55 下午 2022/9/9
 */
public class PoolRecordRespVO {

    private Object[] activePoolSizeArr;

    private Object[] queueSizeArr;

    private Object[] recordTimeArr;

    private Object[] taskCountArr;

    private Object[] taskCountTimeArr;

    private Object[] errorTaskTimeArr;

    private Object[] errorTaskCountArr;

    private Object[] tagRecordTimeArr;

    private Object[] tagRecordCountArr;

    private ThreadPoolRealTimeRespVO threadPoolRealTimeRespVO;


    public ThreadPoolRealTimeRespVO getThreadPoolRealTimeRespVO() {
        return threadPoolRealTimeRespVO;
    }

    public void setThreadPoolRealTimeRespVO(ThreadPoolRealTimeRespVO threadPoolRealTimeRespVO) {
        this.threadPoolRealTimeRespVO = threadPoolRealTimeRespVO;
    }

    public Object[] getActivePoolSizeArr() {
        return activePoolSizeArr;
    }

    public void setActivePoolSizeArr(Object[] activePoolSizeArr) {
        this.activePoolSizeArr = activePoolSizeArr;
    }

    public Object[] getQueueSizeArr() {
        return queueSizeArr;
    }

    public void setQueueSizeArr(Object[] queueSizeArr) {
        this.queueSizeArr = queueSizeArr;
    }

    public Object[] getRecordTimeArr() {
        return recordTimeArr;
    }

    public void setRecordTimeArr(Object[] recordTimeArr) {
        this.recordTimeArr = recordTimeArr;
    }

    public Object[] getTaskCountArr() {
        return taskCountArr;
    }

    public void setTaskCountArr(Object[] taskCountArr) {
        this.taskCountArr = taskCountArr;
    }

    public Object[] getTaskCountTimeArr() {
        return taskCountTimeArr;
    }

    public void setTaskCountTimeArr(Object[] taskCountTimeArr) {
        this.taskCountTimeArr = taskCountTimeArr;
    }

    public Object[] getErrorTaskTimeArr() {
        return errorTaskTimeArr;
    }

    public void setErrorTaskTimeArr(Object[] errorTaskTimeArr) {
        this.errorTaskTimeArr = errorTaskTimeArr;
    }

    public Object[] getErrorTaskCountArr() {
        return errorTaskCountArr;
    }

    public void setErrorTaskCountArr(Object[] errorTaskCountArr) {
        this.errorTaskCountArr = errorTaskCountArr;
    }

    public Object[] getTagRecordTimeArr() {
        return tagRecordTimeArr;
    }

    public void setTagRecordTimeArr(Object[] tagRecordTimeArr) {
        this.tagRecordTimeArr = tagRecordTimeArr;
    }

    public Object[] getTagRecordCountArr() {
        return tagRecordCountArr;
    }

    public void setTagRecordCountArr(Object[] tagRecordCountArr) {
        this.tagRecordCountArr = tagRecordCountArr;
    }

    @Override
    public String toString() {
        return "PoolRecordRespVO{" +
                "activePoolSizeArr=" + Arrays.toString(activePoolSizeArr) +
                ", queueSizeArr=" + Arrays.toString(queueSizeArr) +
                ", recordTimeArr=" + Arrays.toString(recordTimeArr) +
                ", taskCountArr=" + Arrays.toString(taskCountArr) +
                ", taskCountTimeArr=" + Arrays.toString(taskCountTimeArr) +
                ", errorTaskTimeArr=" + Arrays.toString(errorTaskTimeArr) +
                ", errorTaskCountArr=" + Arrays.toString(errorTaskCountArr) +
                ", tagRecordTimeArr=" + Arrays.toString(tagRecordTimeArr) +
                ", tagRecordCountArr=" + Arrays.toString(tagRecordCountArr) +
                '}';
    }
}
