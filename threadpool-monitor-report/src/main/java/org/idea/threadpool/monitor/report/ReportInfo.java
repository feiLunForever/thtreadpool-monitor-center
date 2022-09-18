package org.idea.threadpool.monitor.report;

import com.alibaba.fastjson.JSON;

/**
 * @Author linhao
 * @Date created in 8:58 上午 2022/8/4
 */
public class ReportInfo {

    /**
     * 任务id
     */
    private String jobId;

    /**
     * 任务标签
     */
    private String tag;

    /**
     * 任务执行结果 0 成功 1 失败
     */
    private Integer status;

    /**
     * 执行时间 毫秒
     */
    private long executeTime;

    private String poolName;

    private String createTime;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    @Override
    public String toString() {
        return "ReportInfo{" +
                "jobId='" + jobId + '\'' +
                ", tag='" + tag + '\'' +
                ", executeTime=" + executeTime +
                '}';
    }

    public String toJson(){
        return JSON.toJSONString(this);
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
