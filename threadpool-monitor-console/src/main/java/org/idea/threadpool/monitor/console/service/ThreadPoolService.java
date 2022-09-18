package org.idea.threadpool.monitor.console.service;

import org.idea.threadpool.monitor.console.vo.req.ThreadPoolReqVO;
import org.idea.threadpool.monitor.console.vo.resp.*;

import java.util.List;

/**
 * @Author linhao
 * @Date created in 10:40 下午 2022/9/7
 */
public interface ThreadPoolService {

     List<ThreadPoolListRespVO> getThreadPoolList(String applicationName);

     List<String> getApplicationNames();

    PoolRecordRespVO getDetailRecord(ThreadPoolReqVO threadPoolReqVO);

    PoolNameAndAddressRespVO getAddressAndPoolName(ThreadPoolReqVO threadPoolReqVO);

    JobListRespVO getJobRecord(ThreadPoolReqVO threadPoolReqVO);

    List<String> getJobTagList(ThreadPoolReqVO threadPoolReqVO);

    List<AlarmInfo> getAlarmInfos(ThreadPoolReqVO threadPoolReqVO);

    TotalData getTotalData();
}
