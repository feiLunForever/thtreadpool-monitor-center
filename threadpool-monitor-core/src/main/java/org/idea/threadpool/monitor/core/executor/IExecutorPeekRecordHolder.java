package org.idea.threadpool.monitor.core.executor;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author linhao
 * @Date created in 10:29 上午 2022/9/15
 */
public class IExecutorPeekRecordHolder {

    /**
     * 记录最近15秒内的活跃线程最高峰
     */
    private int activeThreadCountPeek = 0;

    /**
     * 记录最近15秒内的任务队列最大值
     */
    private int taskQueueLengthPeek = 0;

    private List<Long> executeTaskTimes = new LinkedList<>();


    public void recordActiveThreadCountPeek(int activeThreadCount) {
        if (activeThreadCountPeek < activeThreadCount) {
            activeThreadCountPeek = activeThreadCount;
        }
    }

    public void recordTaskQueueLengthPeek(int taskQueueLength) {
        if (taskQueueLengthPeek < taskQueueLength) {
            taskQueueLengthPeek = taskQueueLength;
        }
    }

    public void recordExecuteTaskTimes(long timeCount) {
        executeTaskTimes.add(timeCount);
    }

    public List<Long> getExecuteTaskTimes() {
        return executeTaskTimes;
    }

    public int getActiveThreadCountPeek() {
        return activeThreadCountPeek;
    }

    public int getTaskQueueLengthPeek() {
        return taskQueueLengthPeek;
    }

    public void setActiveThreadCountPeek(int activeThreadCountPeek) {
        this.activeThreadCountPeek = activeThreadCountPeek;
    }

    public void setTaskQueueLengthPeek(int taskQueueLengthPeek) {
        this.taskQueueLengthPeek = taskQueueLengthPeek;
    }

    public void setExecuteTaskTimes(List<Long> executeTaskTimes) {
        this.executeTaskTimes = executeTaskTimes;
    }

    /**
     * 清理队列长度和线程池活跃数值
     */
    public void clearActiveCountAndQueueSIze() {
        this.setActiveThreadCountPeek(0);
        this.setTaskQueueLengthPeek(0);
    }

}
