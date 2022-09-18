package org.idea.threadpool.monitor.console.dao;

import org.idea.threadpool.monitor.console.bo.ThreadPoolListBO;
import org.idea.threadpool.monitor.console.vo.req.ThreadPoolReqVO;
import org.idea.threadpool.monitor.console.vo.resp.*;
import org.idea.threadpool.monitor.report.ThreadPoolRealTimeInfo;

import java.util.List;

/**
 * @Author linhao
 * @Date created in 10:58 下午 2022/9/7
 */
public interface IThreadPoolStoreDao {

    List<ThreadPoolListBO> getThreadPoolList(String applicationName);

    List<PoolDetailRecordRespVO> getPoolDetailRecordList(String applicationName,String poolName,String ip,Integer port,String queryDate);

    List<TaskCountPerMinRespVO> getTaskCountPer15s(String applicationName,String poolName,String ip,Integer port,String queryDate);

    List<TaskCountPerMinRespVO> getErrorCountPer15s(String applicationName,String poolName,String ip,Integer port,String queryDate);

    List<TaskCountPerMinRespVO> getTagCountPer15s(String applicationName,String poolName,String ip,Integer port,String queryDate);

    PoolNameAndAddressRespVO getAddress(String applicationName);

    ThreadPoolRealTimeInfo getRealTimeInfo(String applicationName, String poolName, String ip, Integer port);

    List<JobItemRespVO> getJobRecord(ThreadPoolReqVO threadPoolReqVO);

    Long countJobRecordSize(ThreadPoolReqVO threadPoolReqVO);

    List<JobRecordQueueRespVO> getJobRecordQueueList(ThreadPoolReqVO threadPoolReqVO);

    List<String> getJobTagList(ThreadPoolReqVO threadPoolReqVO);

    List<AlarmInfo> getAlarmInfos(ThreadPoolReqVO threadPoolReqVO);

    TotalData getTotalData(List<String> applicationNameList);
}
