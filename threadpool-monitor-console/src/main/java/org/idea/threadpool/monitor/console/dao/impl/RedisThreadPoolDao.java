package org.idea.threadpool.monitor.console.dao.impl;

import com.alibaba.fastjson.JSON;
import org.idea.threadpool.monitor.common.CommonConstants;
import org.idea.threadpool.monitor.console.bo.ThreadPoolListBO;
import org.idea.threadpool.monitor.console.dao.IThreadPoolStoreDao;
import org.idea.threadpool.monitor.console.vo.req.ThreadPoolReqVO;
import org.idea.threadpool.monitor.console.vo.resp.*;
import org.idea.threadpool.monitor.report.ThreadPoolRealTimeInfo;
import org.idea.threadpool.monitor.report.redis.IRedisService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author linhao
 * @Date created in 10:58 下午 2022/9/7
 */
@Component
public class RedisThreadPoolDao implements IThreadPoolStoreDao {

    @Resource
    private IRedisService redisService;

    @Override
    public List<ThreadPoolListBO> getThreadPoolList(String applicationName) {
        Set<String> poolList = redisService.sMembers(CommonConstants.buildTotalDataKey(applicationName));
        List<ThreadPoolListBO> threadPoolListBOS = new ArrayList<>(poolList.size());
        for (String item : poolList) {
            ThreadPoolListBO threadPoolListBO = new ThreadPoolListBO();
            String[] items = item.split("\\$");
            threadPoolListBO.setIp(items[0]);
            threadPoolListBO.setPort(Integer.valueOf(items[1]));
            threadPoolListBO.setPoolName(items[2]);
            ThreadPoolRealTimeInfo realTimeInfo = this.getRealTimeInfo(applicationName, threadPoolListBO.getPoolName(), threadPoolListBO.getIp(), threadPoolListBO.getPort());
            threadPoolListBO.setThreadPoolDetail(realTimeInfo);
            threadPoolListBOS.add(threadPoolListBO);
        }
        return threadPoolListBOS;
    }

    @Override
    public List<PoolDetailRecordRespVO> getPoolDetailRecordList(String applicationName, String poolName, String ip, Integer port, String queryDate) {
        String poolKey = CommonConstants.buildThreadPoolDetailInfoKey(ip, port, applicationName, poolName, queryDate);
        List<String> jsonList = redisService.lrange(poolKey, 0, -1);
        List<PoolDetailRecordRespVO> poolDetailRecordRespVOList = new ArrayList<>();
        for (int i = jsonList.size() - 1; i >= 0; i--) {
            String jsonItem = jsonList.get(i);
            PoolDetailRecordRespVO item = JSON.parseObject(jsonItem, PoolDetailRecordRespVO.class);
            item.setRecordTime(item.getRecordTime().replaceAll(":", "").replaceAll("-", "").replaceAll(" ", ""));
            poolDetailRecordRespVOList.add(item);
        }
        return poolDetailRecordRespVOList;
    }

    @Override
    public List<TaskCountPerMinRespVO> getTaskCountPer15s(String applicationName, String poolName, String ip, Integer port, String queryDate) {
        String redisKey = CommonConstants.buildThreadPoolTaskTimesKey(ip, port, applicationName, poolName, queryDate);
        List<String> jsonList = redisService.lrange(redisKey, 0, -1);
        List<TaskCountPerMinRespVO> resultList = new ArrayList<>();
        for (int i = jsonList.size() - 1; i >= 0; i--) {
            String jsonItem = jsonList.get(i);
            TaskCountPerMinRespVO item = new TaskCountPerMinRespVO();
            item.setTimes(Integer.valueOf(jsonItem.split(":")[0]));
            item.setRecordTime(jsonItem.split(":")[1]);
            resultList.add(item);
        }
        return resultList;
    }

    @Override
    public List<TaskCountPerMinRespVO> getErrorCountPer15s(String applicationName, String poolName, String ip, Integer port, String queryDate) {
        String redisKey = CommonConstants.buildErrorTaskKey(ip, port, applicationName, poolName, queryDate);
        List<String> jsonList = redisService.lrange(redisKey, 0, -1);
        List<TaskCountPerMinRespVO> resultList = new ArrayList<>();
        for (int i = jsonList.size() - 1; i >= 0; i--) {
            String jsonItem = jsonList.get(i);
            TaskCountPerMinRespVO item = new TaskCountPerMinRespVO();
            item.setTimes(Integer.valueOf(jsonItem.split(":")[0]));
            item.setRecordTime(jsonItem.split(":")[1]);
            resultList.add(item);
        }
        return resultList;
    }

    @Override
    public List<TaskCountPerMinRespVO> getTagCountPer15s(String applicationName, String poolName, String ip, Integer port, String queryDate) {
        String redisKey = CommonConstants.buildTagCountKey(ip, port, applicationName, poolName, queryDate);
        List<String> jsonList = redisService.lrange(redisKey, 0, -1);
        List<TaskCountPerMinRespVO> resultList = new ArrayList<>();
        for (int i = jsonList.size() - 1; i >= 0; i--) {
            String jsonItem = jsonList.get(i);
            TaskCountPerMinRespVO item = new TaskCountPerMinRespVO();
            item.setTimes(Integer.valueOf(jsonItem.split(":")[0]));
            item.setRecordTime(jsonItem.split(":")[1]);
            resultList.add(item);
        }
        return resultList;
    }

    @Override
    public PoolNameAndAddressRespVO getAddress(String applicationName) {
        PoolNameAndAddressRespVO result = new PoolNameAndAddressRespVO();
        String redisKey = CommonConstants.buildTotalDataKey(applicationName);
        Set<String> set = redisService.sMembers(redisKey);
        Set<String> addressSet = new HashSet<>();
        Set<String> poolNameSet = new HashSet<>();
        for (String item : set) {
            String detail[] = item.split("\\$");
            addressSet.add(detail[0] + ":" + detail[1]);
            poolNameSet.add(detail[2]);
        }
        result.setPoolNameSet(poolNameSet);
        result.setAddressSet(addressSet);
        return result;
    }

    @Override
    public ThreadPoolRealTimeInfo getRealTimeInfo(String applicationName, String poolName, String ip, Integer port) {
        String key = CommonConstants.buildThreadPoolRealTimeKey(ip, port, applicationName, poolName);
        String json = redisService.get(key);
        ThreadPoolRealTimeInfo realTimeInfo = JSON.parseObject(json, ThreadPoolRealTimeInfo.class);
        return realTimeInfo;
    }

    @Override
    public List<JobItemRespVO> getJobRecord(ThreadPoolReqVO reqVO) {
        String tagKey = CommonConstants.buildTagKey(reqVO.getApplicationName(), reqVO.getPoolName());
        String tagUUid = (String) redisService.getMapField(tagKey, reqVO.getTag(), String.class);
        if (StringUtils.isEmpty(tagUUid)) {
            return Collections.emptyList();
        }
        int start = (reqVO.getPage() - 1) * reqVO.getPageSize();
        int end = start + reqVO.getPageSize();
        String key = CommonConstants.buildJobInfoListKey(reqVO.getIp(), reqVO.getPort(), reqVO.getApplicationName(), reqVO.getQueryDate(), reqVO.getPoolName(), tagUUid);
        Set<Tuple> jobInfo = redisService.zRangeWithScores(key, 0, -1);
        List<JobItemRespVO> jobItemRespVOS = new ArrayList<>();
        for (Tuple job : jobInfo) {
            String jobDetail = job.getElement();
            JobItemRespVO item = JSON.parseObject(jobDetail, JobItemRespVO.class);
            if(!StringUtils.isEmpty(reqVO.getJobId()) && !reqVO.getJobId().equals(item.getJobId())){
                continue;
            }
            jobItemRespVOS.add(item);
            start++;
            if (start > end) {
                break;
            }
        }
        return jobItemRespVOS;
    }

//    private long getJobQueueSize(String applicationName, String poolName, Integer port, String ip, String queryDate) {
//        ThreadPoolReqVO threadPoolReqVO = new ThreadPoolReqVO();
//        threadPoolReqVO.setApplicationName(applicationName);
//        threadPoolReqVO.setPoolName(poolName);
//        List<String> jobTagList = this.getJobTagList(threadPoolReqVO);
//        String tagKey = CommonConstants.buildTagKey(applicationName, poolName);
//        long count = 0;
//        for (String tagName : jobTagList) {
//            String tagUUid = (String) redisService.getMapField(tagKey, tagName, String.class);
//            if (StringUtils.isEmpty(tagUUid)) {
//                continue;
//            }
//            String key = CommonConstants.buildJobInfoListKey(ip, port, applicationName, queryDate, poolName, tagUUid);
//            long size = redisService.zCard(key);
//
//        }
//        return;
//    }


    @Override
    public List<JobRecordQueueRespVO> getJobRecordQueueList(ThreadPoolReqVO reqVO) {
        String tagKey = CommonConstants.buildTagKey(reqVO.getApplicationName(), reqVO.getPoolName());
        Map<String,String> tagMap = redisService.getMap(tagKey);
        List<JobRecordQueueRespVO> jobRecordQueueRespVOS = new ArrayList<>();
        for (String tagName : tagMap.keySet()) {
            JobRecordQueueRespVO item = new JobRecordQueueRespVO();
            String tagUUid = tagMap.get(tagName);
            String key = CommonConstants.buildJobInfoListKey(reqVO.getIp(),reqVO.getPort(),
                    reqVO.getApplicationName(),reqVO.getQueryDate(),reqVO.getPoolName(),tagUUid);
            item.setTagName(tagName);
            item.setIpAndPort(reqVO.getIpAndPort());
            item.setLength(Math.toIntExact(redisService.zCard(key)));
            item.setPoolName(reqVO.getPoolName());
            item.setApplicationName(reqVO.getApplicationName());
            jobRecordQueueRespVOS.add(item);
        }
        return jobRecordQueueRespVOS;
    }

    @Override
    public Long countJobRecordSize(ThreadPoolReqVO reqVO) {
        String tagKey = CommonConstants.buildTagKey(reqVO.getApplicationName(), reqVO.getPoolName());
        String tagUUid = (String) redisService.getMapField(tagKey, reqVO.getTag(), String.class);
        if (StringUtils.isEmpty(tagUUid)) {
            return 0L;
        }
        String key = CommonConstants.buildJobInfoListKey(reqVO.getIp(), reqVO.getPort(), reqVO.getApplicationName(), reqVO.getQueryDate(), reqVO.getPoolName(), tagUUid);
        return redisService.zCard(key);
    }

    @Override
    public List<String> getJobTagList(ThreadPoolReqVO threadPoolReqVO) {
        String tagKey = CommonConstants.buildTagKey(threadPoolReqVO.getApplicationName(), threadPoolReqVO.getPoolName());
        Map<String, String> tagMap = redisService.getMap(tagKey);
        if (tagMap.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(tagMap.keySet());
    }

    @Override
    public List<AlarmInfo> getAlarmInfos(ThreadPoolReqVO threadPoolReqVO) {
        String redisKey = CommonConstants.buildAlarmKey(threadPoolReqVO.getApplicationName());
        Map<String, String> alarmMap = redisService.getMap(redisKey);
        List<AlarmInfo> result = new ArrayList<>(alarmMap.size());
        for (String poolName : alarmMap.keySet()) {
            AlarmInfo alarmInfo = new AlarmInfo();
            alarmInfo.setApplication(threadPoolReqVO.getApplicationName());
            alarmInfo.setEmail(alarmMap.get(poolName));
            result.add(alarmInfo);
        }
        return result;
    }

    @Override
    public TotalData getTotalData(List<String> applicationNameList) {
        TotalData totalData = new TotalData();
        long totalTaskCount = 0L;
        long errorTaskCount = 0L;
        int applicationCount = 0;
        for (String applicationName : applicationNameList) {
            String totalDataKey = CommonConstants.buildTotalDataKey(applicationName);
            List<String> setValues = redisService.sScanAll(totalDataKey, "0", "*", -1);
            for (String poolNameAndIp : setValues) {
                String arr[] = poolNameAndIp.split("\\$");
                String ip = arr[0];
                String port = arr[1];
                String poolName = arr[2];
                ThreadPoolRealTimeInfo threadPoolRealTimeInfo = this.getRealTimeInfo(applicationName, poolName, ip, Integer.valueOf(port));
                long currentCount = threadPoolRealTimeInfo.getCompletedTaskCount();
                totalTaskCount = totalTaskCount + currentCount;
                errorTaskCount = errorTaskCount + threadPoolRealTimeInfo.getErrorTaskCount();
                applicationCount++;
                ThreadPoolReqVO reqVO = new ThreadPoolReqVO();
                reqVO.setApplicationName(applicationName);
                reqVO.setPoolName(poolName);
                getJobTagList(reqVO);
            }
        }
        totalData.setApplicationCount(applicationCount);
        totalData.setTotalTaskCount(totalTaskCount);
        totalData.setErrorTaskCount(errorTaskCount);
        return totalData;
    }
}
