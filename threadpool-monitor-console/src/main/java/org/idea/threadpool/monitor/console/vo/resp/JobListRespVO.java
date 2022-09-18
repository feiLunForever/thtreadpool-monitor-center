package org.idea.threadpool.monitor.console.vo.resp;

import java.util.List;

/**
 * @Author linhao
 * @Date created in 9:52 上午 2022/9/12
 */
public class JobListRespVO {

    private List<JobItemRespVO> jobItemRespVOList;
    private Long totalSize;
    private List<JobRecordQueueRespVO> jobRecordQueueRespVOS;

    public List<JobRecordQueueRespVO> getJobRecordQueueRespVOS() {
        return jobRecordQueueRespVOS;
    }

    public void setJobRecordQueueRespVOS(List<JobRecordQueueRespVO> jobRecordQueueRespVOS) {
        this.jobRecordQueueRespVOS = jobRecordQueueRespVOS;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public List<JobItemRespVO> getJobItemRespVOList() {
        return jobItemRespVOList;
    }

    public void setJobItemRespVOList(List<JobItemRespVO> jobItemRespVOList) {
        this.jobItemRespVOList = jobItemRespVOList;
    }
}
