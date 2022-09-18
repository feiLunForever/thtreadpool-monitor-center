package org.idea.threadpool.monitor.core.job;

import org.idea.threadpool.monitor.common.CommonConstants;
import org.idea.threadpool.monitor.core.config.CommonCache;
import org.idea.threadpool.monitor.core.config.DynamicThreadPoolProperties;
import org.idea.threadpool.monitor.core.config.IExecutorProperties;
import org.idea.threadpool.monitor.core.executor.IExecutor;
import org.idea.threadpool.monitor.core.executor.ResizableCapacityLinkedBlockingQueue;
import org.idea.threadpool.monitor.core.util.CommonUtil;
import org.idea.threadpool.monitor.core.util.EmailUtil;
import org.idea.threadpool.monitor.report.IReporter;
import org.idea.threadpool.monitor.report.ThreadPoolRealTimeInfo;
import org.idea.threadpool.monitor.report.TotalDataInfo;
import org.idea.threadpool.monitor.report.redis.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author linhao
 * @Date created in 9:35 上午 2022/9/11
 */
public class RealTimeInfoWatcher implements Runnable {

    private static Logger LOGGER = LoggerFactory.getLogger(RealTimeInfoWatcher.class);

    private Map<String, IExecutor> executorMap;
    private IReporter reporter;
    private DynamicThreadPoolProperties dynamicThreadPoolProperties;
    private ApplicationContext applicationContext;
    private IRedisService redisService;
    private static long lastSendTimeMills = -1;

    public RealTimeInfoWatcher(IRedisService redisService, Map<String, IExecutor> executorMap, ApplicationContext applicationContext, IReporter reporter, DynamicThreadPoolProperties dynamicThreadPoolProperties) {
        this.executorMap = executorMap;
        this.reporter = reporter;
        this.redisService = redisService;
        this.dynamicThreadPoolProperties = dynamicThreadPoolProperties;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (String poolName : executorMap.keySet()) {
                    IExecutor executor = executorMap.get(poolName);
                    recordRealTimeInfo(executor);
                    realTimeAlarmInfo(executor);
                    refreshThreadPool();
                    cleanJobInfoRecord(poolName);
                    reportIpInfo();
                }
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                LOGGER.error("[RealTimeInfoWatcher] error is ", e);
            }
        }
    }

    public void reportIpInfo() {
        for (Map.Entry<String, IExecutor> item : executorMap.entrySet()) {
            TotalDataInfo totalDataInfo = new TotalDataInfo();
            totalDataInfo.setApplicationName(CommonCache.getApplicationName());
            totalDataInfo.setIp(CommonCache.getIp());
            totalDataInfo.setPoolName(item.getValue().getPoolName());
            totalDataInfo.setPort(CommonCache.getPort());
            reporter.doReportTotalData(totalDataInfo);
        }
    }


    //清理记录标签任务的记录
    private void cleanJobInfoRecord(String poolName) {
        String tagMapKey = CommonConstants.buildTagKey(CommonCache.getApplicationName(), poolName);
        Map<String, String> tagIdMap = redisService.getMap(tagMapKey);
        for (String tagName : tagIdMap.keySet()) {
            String tagUUid = tagIdMap.get(tagName);
            String jobInfoKey = CommonConstants.buildJobInfoListKey(CommonCache.getIp(), CommonCache.getPort(), CommonCache.getApplicationName(),
                    CommonUtil.getTodayStr(), poolName, tagUUid);
            long size = redisService.zCard(jobInfoKey);
            IExecutorProperties executorProperties = dynamicThreadPoolProperties.getExecutors().get(poolName);

            //如果体积过大，则需要删除部分耗时最低的数据
            if (size > Double.valueOf(executorProperties.getMaxTagRecordSize()) * 0.75) {
                int begin = (int) (size * 0.75);
                //删除耗时最低的25%的数据
                redisService.zremrangeByRank(jobInfoKey, begin, -1);
                LOGGER.info("删除队列任务数据");
            }
        }
    }

    public void refreshThreadPool() {
        if (isThreadPoolPropertiesIllegal()) {
            LOGGER.error("[RealTimeInfoWatcher] dynamic threadPool's properties is illegal!");
            return;
        }
        Map<String, IExecutor> runningExecutorMap = applicationContext.getBeansOfType(IExecutor.class);
        DynamicThreadPoolProperties dynamicThreadPoolProperties = applicationContext.getBean(DynamicThreadPoolProperties.class);
        Map<String, IExecutorProperties> iExecutorsMap = dynamicThreadPoolProperties.getExecutors();
        for (String executorName : iExecutorsMap.keySet()) {
            IExecutorProperties executorProperties = iExecutorsMap.get(executorName);
            IExecutor executor = runningExecutorMap.get(executorName);
            boolean hasChange = false;
            if (executor != null) {
                if (executor.getCorePoolSize() != Integer.valueOf(executorProperties.getCorePoolSize())) {
                    hasChange = true;
                }
                if (executor.getKeepAliveTime(TimeUnit.MILLISECONDS) != Integer.valueOf(executorProperties.getKeepAliveTime())) {
                    hasChange = true;
                }
                if (executor.getQueueCapacity() != Integer.valueOf(executorProperties.getQueueCapacity())) {
                    hasChange = true;
                }
                if (executor.getMaximumPoolSize() != Integer.valueOf(executorProperties.getMaximumPoolSize())) {
                    hasChange = true;
                }
                if (!hasChange) {
                    continue;
                }
                executor.setCorePoolSize(Integer.parseInt(executorProperties.getCorePoolSize()));
                executor.setKeepAliveTime(Long.parseLong(executorProperties.getKeepAliveTime()), TimeUnit.MILLISECONDS);
                executor.setMaximumPoolSize(Integer.parseInt(executorProperties.getMaximumPoolSize()));
                //底层默认统一使用动态扩容队列
                ResizableCapacityLinkedBlockingQueue workQueue = (ResizableCapacityLinkedBlockingQueue) executor.getQueue();
                workQueue.setCapacity(Integer.parseInt(executorProperties.getQueueCapacity()));
                LOGGER.info("[RealTimeInfoWatcher] refresh success! executorProperties is {}", executorProperties);
            }
        }
    }

    /**
     * 判断线程池配置是否合法
     *
     * @return
     */
    private boolean isThreadPoolPropertiesIllegal() {
        boolean isThreadPoolPropertiesIllegal = false;
        StringBuffer errorAlarmMsg = new StringBuffer();
        DynamicThreadPoolProperties dynamicThreadPoolProperties = applicationContext.getBean(DynamicThreadPoolProperties.class);
        String alarmEmails = dynamicThreadPoolProperties.getAlarmEmails();
        if (StringUtils.isEmpty(alarmEmails)) {
            LOGGER.error("[preCheckThreadPoolParamVerify] alarmWorkerIds could not be null!");
            return true;
        }
        Map<String, IExecutorProperties> executorsMap = dynamicThreadPoolProperties.getExecutors();
        for (String executorName : executorsMap.keySet()) {
            IExecutorProperties iExecutors = executorsMap.get(executorName);
            if (Integer.valueOf(iExecutors.getCorePoolSize()) > Integer.valueOf(iExecutors.getMaximumPoolSize())) {
                isThreadPoolPropertiesIllegal = true;
                errorAlarmMsg.append("核心线程数不得大于最大线程数! 当前corePoolSize:" + iExecutors.getCorePoolSize() + ",maximumPoolSize:" + iExecutors.getMaximumPoolSize() + "\n");
            }
            if (Integer.valueOf(iExecutors.getQueueCapacity()) > 10000) {
                isThreadPoolPropertiesIllegal = true;
                errorAlarmMsg.append("线程池队列长度范围 0～10000! 当前queueCapacity:" + iExecutors.getQueueCapacity() + "\n");
            }
            if (Double.valueOf(iExecutors.getTaskCountScoreThreshold()) > 1) {
                isThreadPoolPropertiesIllegal = true;
                errorAlarmMsg.append("线程池队列任务上限阈值（queueSize/queueCapacity）不得大于1! 当前taskCountScoreThreshold:" + iExecutors.getTaskCountScoreThreshold() + "\n");
            }
            if (Integer.valueOf(iExecutors.getMaxTagRecordSize()) > 10000) {
                isThreadPoolPropertiesIllegal = true;
                errorAlarmMsg.append("标签队列体积上限不得大于10000! 当前maxTagRecordSize:" + iExecutors.getMaxTagRecordSize() + "\n");
            }
            if (isThreadPoolPropertiesIllegal) {
                return true;
            }
        }
        return false;
    }

    /**
     * 实时记录当前服务的线程池信息
     *
     * @param executor
     */
    public void recordRealTimeInfo(IExecutor executor) {
        ThreadPoolRealTimeInfo realTimeInfo = new ThreadPoolRealTimeInfo();
        realTimeInfo.setPoolName(executor.getPoolName());
        realTimeInfo.setActivePoolSize(executor.getActiveCount());
        realTimeInfo.setMaximumPoolSize(executor.getMaximumPoolSize());
        realTimeInfo.setQueueCapacity(executor.getQueueCapacity());
        realTimeInfo.setQueueSize(executor.getQueueSize());
        realTimeInfo.setCompletedTaskCount(executor.getCompletedTaskCount());
        realTimeInfo.setErrorTaskCount(executor.getErrorNum());
        realTimeInfo.setKeepAliveTime(executor.getKeepAliveTime(TimeUnit.MILLISECONDS));
        realTimeInfo.setCorePoolSize(executor.getCorePoolSize());
        realTimeInfo.setPreStartAllCoreThreads(executor.getPreStartAllCoreThreads());
        realTimeInfo.setPreStartCoreThread(executor.getPreStartCoreThread());
        realTimeInfo.setRejectedExecutionType(executor.getRejectedExecutionHandler().getClass().getSimpleName());
        realTimeInfo.setTaskCountScoreThreshold(String.valueOf(executor.getTaskCountScoreThreshold()));
        reporter.doReportRealTime(CommonCache.getIp(), CommonCache.getPort(), CommonCache.getApplicationName(), realTimeInfo);
        reporter.doReportAlarmInfo(CommonCache.getApplicationName(), dynamicThreadPoolProperties.getAlarmEmails());
    }

    /**
     * 实时计算线程池负载情况
     *
     * @param executor
     */
    public void realTimeAlarmInfo(IExecutor executor) {
        double queueLoad = (double) executor.getQueue().size() / (double) executor.getQueueCapacity();
        if (executor.getTaskCountScoreThreshold() <= queueLoad) {
            String alarmEmails = dynamicThreadPoolProperties.getAlarmEmails();
            if (StringUtils.isEmpty(alarmEmails)) {
                return;
            }
            boolean canSend = false;
            //初始值为-1
            if (lastSendTimeMills < 0) {
                canSend = true;
            } else if (System.currentTimeMillis() - lastSendTimeMills > 5 * 60 * 1000) {
                canSend = true;
            }
            if (canSend) {
                String[] emailArr = alarmEmails.split(",");
                String title = CommonConstants.buildErrorAlarmTitle("test");
                String content = CommonConstants.buildErrorAlarmContent(CommonCache.getApplicationName(), CommonCache.getIp() + ":" + CommonCache.getPort(), executor.getPoolName());
                try {
                    LOGGER.info("【realTimeAlarmInfo】发送实时告警通知");
                    EmailUtil.send(title, content, emailArr);
                } catch (Exception e) {
                    LOGGER.error("error is ", e);
                }
                lastSendTimeMills = System.currentTimeMillis();
            }
        }
    }

}
