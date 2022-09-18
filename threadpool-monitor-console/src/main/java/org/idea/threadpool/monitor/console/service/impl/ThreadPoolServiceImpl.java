package org.idea.threadpool.monitor.console.service.impl;

import org.idea.threadpool.monitor.console.dao.IThreadPoolStoreDao;
import org.idea.threadpool.monitor.console.service.ThreadPoolService;
import org.idea.threadpool.monitor.console.utils.BeanConvertUtils;
import org.idea.threadpool.monitor.console.vo.req.ThreadPoolReqVO;
import org.idea.threadpool.monitor.console.vo.resp.*;
import org.idea.threadpool.monitor.report.ThreadPoolDetailInfo;
import org.idea.threadpool.monitor.report.ThreadPoolRealTimeInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @Author linhao
 * @Date created in 10:42 下午 2022/9/7
 */
@Service
public class ThreadPoolServiceImpl implements ThreadPoolService {

    @Resource
    private IThreadPoolStoreDao redisThreadPoolDao;

    @Value("${threadpool.application.name.list}")
    private String applicationNameStr;

    @Override
    public List<ThreadPoolListRespVO> getThreadPoolList(String applicationName) {
        return BeanConvertUtils.convertList(redisThreadPoolDao.getThreadPoolList(applicationName), ThreadPoolListRespVO.class);
    }

    @Override
    public List<String> getApplicationNames() {
        String applicationNameArr[] = applicationNameStr.split(",");
        return Arrays.asList(applicationNameArr);
    }

    @Override
    public PoolRecordRespVO getDetailRecord(ThreadPoolReqVO threadPoolReqVO) {
        List<PoolDetailRecordRespVO> poolList = redisThreadPoolDao.getPoolDetailRecordList(
                threadPoolReqVO.getApplicationName(),
                threadPoolReqVO.getPoolName(),
                threadPoolReqVO.getIp(),
                threadPoolReqVO.getPort(),
                threadPoolReqVO.getQueryDate());

        List<TaskCountPerMinRespVO> taskCountList = redisThreadPoolDao.getTaskCountPer15s(threadPoolReqVO.getApplicationName(),
                threadPoolReqVO.getPoolName(),
                threadPoolReqVO.getIp(),
                threadPoolReqVO.getPort(),
                threadPoolReqVO.getQueryDate());

        List<TaskCountPerMinRespVO> errorCountList = redisThreadPoolDao.getErrorCountPer15s(threadPoolReqVO.getApplicationName(),
                threadPoolReqVO.getPoolName(),
                threadPoolReqVO.getIp(),
                threadPoolReqVO.getPort(),
                threadPoolReqVO.getQueryDate());

        List<TaskCountPerMinRespVO> tagCountList = redisThreadPoolDao.getTagCountPer15s(threadPoolReqVO.getApplicationName(),
                threadPoolReqVO.getPoolName(),
                threadPoolReqVO.getIp(),
                threadPoolReqVO.getPort(),
                threadPoolReqVO.getQueryDate());


        ThreadPoolRealTimeInfo realTimeInfo = redisThreadPoolDao.getRealTimeInfo(threadPoolReqVO.getApplicationName(),
                threadPoolReqVO.getPoolName(),
                threadPoolReqVO.getIp(),
                threadPoolReqVO.getPort());

        PoolRecordRespVO result = new PoolRecordRespVO();
        result.setTaskCountArr(taskCountList.stream().map(TaskCountPerMinRespVO::getTimes).toArray());
        result.setTaskCountTimeArr(taskCountList.stream().map(TaskCountPerMinRespVO::getRecordTime).toArray());
        result.setErrorTaskTimeArr(errorCountList.stream().map(TaskCountPerMinRespVO::getRecordTime).toArray());
        result.setErrorTaskCountArr(errorCountList.stream().map(TaskCountPerMinRespVO::getTimes).toArray());
        result.setTagRecordTimeArr(tagCountList.stream().map(TaskCountPerMinRespVO::getRecordTime).toArray());
        result.setTagRecordCountArr(tagCountList.stream().map(TaskCountPerMinRespVO::getTimes).toArray());

        result.setActivePoolSizeArr(poolList.stream().map(PoolDetailRecordRespVO::getActivePoolSize).toArray());
        result.setQueueSizeArr(poolList.stream().map(PoolDetailRecordRespVO::getQueueSize).toArray());
        result.setRecordTimeArr(poolList.stream().map(PoolDetailRecordRespVO::getRecordTime).toArray());

        Integer activePoolSize = realTimeInfo.getActivePoolSize();
        Integer queueSize = realTimeInfo.getQueueSize();
        double corePoolLoad = (double) activePoolSize / (double) realTimeInfo.getMaximumPoolSize();
        double queueSizeLoad = (double) queueSize / (double) realTimeInfo.getQueueCapacity();
        ThreadPoolRealTimeRespVO threadPoolRealTimeRespVO = BeanConvertUtils.convert(realTimeInfo, ThreadPoolRealTimeRespVO.class);
        threadPoolRealTimeRespVO.setQueueSize(queueSize);
        threadPoolRealTimeRespVO.setCorePoolLoad((int) (corePoolLoad * 100));
        threadPoolRealTimeRespVO.setQueueSizeLoad((int) (queueSizeLoad * 100));
        threadPoolRealTimeRespVO.setActivePoolSize(activePoolSize);
        result.setThreadPoolRealTimeRespVO(threadPoolRealTimeRespVO);
        return result;
    }

    @Override
    public PoolNameAndAddressRespVO getAddressAndPoolName(ThreadPoolReqVO threadPoolReqVO) {
        return redisThreadPoolDao.getAddress(threadPoolReqVO.getApplicationName());
    }

    @Override
    public JobListRespVO getJobRecord(ThreadPoolReqVO threadPoolReqVO) {
        List<JobItemRespVO> jobItemRespVOS = redisThreadPoolDao.getJobRecord(threadPoolReqVO);
        JobListRespVO result = new JobListRespVO();
        result.setJobItemRespVOList(jobItemRespVOS);
        result.setTotalSize(redisThreadPoolDao.countJobRecordSize(threadPoolReqVO));
        result.setJobRecordQueueRespVOS(redisThreadPoolDao.getJobRecordQueueList(threadPoolReqVO));
        return result;
    }

    @Override
    public List<String> getJobTagList(ThreadPoolReqVO threadPoolReqVO) {
        return redisThreadPoolDao.getJobTagList(threadPoolReqVO);
    }

    @Override
    public List<AlarmInfo> getAlarmInfos(ThreadPoolReqVO threadPoolReqVO) {
        return redisThreadPoolDao.getAlarmInfos(threadPoolReqVO);
    }

    @Override
    public TotalData getTotalData() {
        List<String> applicationNameList = this.getApplicationNames();
        return redisThreadPoolDao.getTotalData(applicationNameList);
    }
}
