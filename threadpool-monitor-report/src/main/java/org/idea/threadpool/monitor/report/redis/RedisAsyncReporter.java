package org.idea.threadpool.monitor.report.redis;


import com.alibaba.fastjson.JSON;
import org.idea.threadpool.monitor.common.CommonConstants;
import org.idea.threadpool.monitor.common.CommonUtil;
import org.idea.threadpool.monitor.report.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 往redis异步上报数据信息
 *
 * @Author linhao
 * @Date created in 9:04 上午 2022/8/4
 */
public class RedisAsyncReporter implements IReporter {

    private IRedisService redisService;

    public RedisAsyncReporter(IRedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void doReportTask(String ip, Integer port, String applicationName, String tagId, ReportInfo reportInfo) {
        String redisKey = CommonConstants.buildJobInfoListKey(
                ip,
                port,
                applicationName,
                CommonUtil.getTodayStr(),
                reportInfo.getPoolName(),
                tagId);
        //只记录耗时最高的5k个
        redisService.zAdd(redisKey, reportInfo.getExecuteTime(),
                reportInfo.toJson());
    }

    @Override
    public void doReportRealTime(String ip, Integer port, String applicationName, ThreadPoolRealTimeInfo realTimeInfo) {
        String keyName = CommonConstants.buildThreadPoolRealTimeKey(
                ip,
                port,
                applicationName,
                realTimeInfo.getPoolName());
        redisService.setStr(keyName, JSON.toJSONString(realTimeInfo), 7, TimeUnit.DAYS);
    }

    @Override
    public void doReportAlarmInfo(String applicationName, String alarmEmails) {
        String redisKey = CommonConstants.buildAlarmKey(applicationName);
        redisService.setMapItem(redisKey, applicationName, alarmEmails);
        redisService.expire(redisKey, 7, TimeUnit.DAYS);
    }

    @Override
    public void doReportTaskTimes(Integer taskTimes, ThreadPoolDetailInfo param) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayKey = dateFormat.format(new Date());
        String keyName = CommonConstants.buildThreadPoolTaskTimesKey(
                param.getIp(),
                param.getPort(),
                param.getApplicationName(),
                param.getPoolName(),
                dayKey);
        redisService.lpush(keyName, taskTimes + ":" + CommonUtil.getCurrentMin());
        redisService.expire(keyName, 7, TimeUnit.DAYS);
    }

    @Override
    public void doReportTotalData(TotalDataInfo totalDataInfo) {
        String mapKeyName = CommonConstants.buildTotalDataKey(totalDataInfo.getApplicationName());
        redisService.sAdd(mapKeyName, CommonUtil.getIpAddress() + "$" + totalDataInfo.getPort() + "$" + totalDataInfo.getPoolName());
        redisService.expire(mapKeyName, 7, TimeUnit.DAYS);
    }

    @Override
    public void doReportThreadPoolInfo(ThreadPoolDetailInfo param) {
        String dateKey = CommonUtil.getTodayStr();
        String keyName = CommonConstants.buildThreadPoolDetailInfoKey(
                param.getIp(),
                param.getPort(),
                param.getApplicationName(),
                param.getPoolName(),
                dateKey);
        redisService.lpush(keyName, param.toJson());
        redisService.expire(keyName, 7, TimeUnit.DAYS);
    }

    @Override
    public void doReportErrorTaskTimes(int errorCount, ThreadPoolDetailInfo param) {
        String dateKey = CommonUtil.getTodayStr();
        String keyName = CommonConstants.buildErrorTaskKey(
                param.getIp(),
                param.getPort(),
                param.getApplicationName(),
                param.getPoolName(),
                dateKey);
        redisService.lpush(keyName, errorCount + ":" + CommonUtil.getCurrentMin());
        redisService.expire(keyName, 7, TimeUnit.DAYS);
    }

    @Override
    public void doReportTagTimes(int tagCount, ThreadPoolDetailInfo param) {
        String dateKey = CommonUtil.getTodayStr();
        String keyName = CommonConstants.buildTagCountKey(
                param.getIp(),
                param.getPort(),
                param.getApplicationName(),
                param.getPoolName(),
                dateKey);
        redisService.lpush(keyName, tagCount + ":" + CommonUtil.getCurrentMin());
        redisService.expire(keyName, 7, TimeUnit.DAYS);
    }
}
