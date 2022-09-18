package org.idea.threadpool.monitor.console.vo.resp;

/**
 * @Author linhao
 * @Date created in 9:52 上午 2022/9/12
 */
public class JobItemRespVO {


    private String tag;
    private String jobId;
    private int status;
    private int executeTime;
    private String createTime;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(int executeTime) {
        this.executeTime = executeTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
