package org.idea.threadpool.monitor.core.job;

import org.idea.threadpool.monitor.core.config.DynamicThreadPoolProperties;
import org.idea.threadpool.monitor.core.config.IExecutorProperties;
import org.idea.threadpool.monitor.core.executor.IExecutor;
import org.idea.threadpool.monitor.core.executor.ResizableCapacityLinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 动态刷新线程池配置
 *
 * @Author idea
 * @Date created in 12:09 下午 2021/11/23
 */
public class DynamicThreadPoolRefreshWatcher implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicThreadPoolRefreshWatcher.class);

    private ApplicationContext applicationContext;

    public DynamicThreadPoolRefreshWatcher(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
                if (isThreadPoolPropertiesIllegal()) {
                    LOGGER.error("[DynamicThreadPoolRefreshConfiguration] dynamic threadPool's properties is illegal!");
                    continue;
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
                        LOGGER.info("[DynamicThreadPoolRefreshConfiguration] refresh success! executorProperties is {}", executorProperties);
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.error("[DynamicThreadPoolRefreshConfiguration] refresh error! e is ", e);
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


}
