package org.idea.threadpool.monitor.console.vo.resp;

import java.util.List;

/**
 * @Author linhao
 * @Date created in 11:37 上午 2022/9/16
 */
public class TotalData {

    private long totalTaskCount;
    private long errorTaskCount;
    private int applicationCount;
    private List<TagQueueInfoRespVO> tagQueueInfoRespVOList;

    public long getTotalTaskCount() {
        return totalTaskCount;
    }

    public void setTotalTaskCount(long totalTaskCount) {
        this.totalTaskCount = totalTaskCount;
    }

    public long getErrorTaskCount() {
        return errorTaskCount;
    }

    public void setErrorTaskCount(long errorTaskCount) {
        this.errorTaskCount = errorTaskCount;
    }

    public int getApplicationCount() {
        return applicationCount;
    }

    public void setApplicationCount(int applicationCount) {
        this.applicationCount = applicationCount;
    }

    public List<TagQueueInfoRespVO> getTagQueueInfoRespVOList() {
        return tagQueueInfoRespVOList;
    }

    public void setTagQueueInfoRespVOList(List<TagQueueInfoRespVO> tagQueueInfoRespVOList) {
        this.tagQueueInfoRespVOList = tagQueueInfoRespVOList;
    }
}
