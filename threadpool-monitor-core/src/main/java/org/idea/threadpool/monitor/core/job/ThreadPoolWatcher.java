package org.idea.threadpool.monitor.core.job;

import org.idea.threadpool.monitor.core.config.CommonCache;
import org.idea.threadpool.monitor.core.executor.IExecutor;
import org.idea.threadpool.monitor.core.executor.IExecutorPeekRecordHolder;
import org.idea.threadpool.monitor.core.util.CommonUtil;
import org.idea.threadpool.monitor.report.IReporter;
import org.idea.threadpool.monitor.report.ThreadPoolDetailInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 线程池后台监视者
 *
 * @Author linhao
 * @Date created in 12:29 下午 2022/8/4
 */
public class ThreadPoolWatcher implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolWatcher.class);

    private Map<String, IExecutor> executorMap;

    private IReporter reporter;

    private String applicationName;

    private Integer port;

    public ThreadPoolWatcher setExecutorMap(Map<String, IExecutor> executorMap) {
        this.executorMap = executorMap;
        return this;
    }

    public ThreadPoolWatcher setReporter(IReporter reporter) {
        this.reporter = reporter;
        return this;
    }

    public ThreadPoolWatcher setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    public ThreadPoolWatcher setPort(Integer port) {
        this.port = port;
        return this;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (String poolName : executorMap.keySet()) {
                    IExecutorPeekRecordHolder executorPeekRecordHolder = CommonCache.getPeekRecordHolderMap().get(poolName);
                    IExecutor executor = executorMap.get(poolName);
                    ThreadPoolDetailInfo threadPoolDetailInfo = new ThreadPoolDetailInfo();
                    threadPoolDetailInfo.setPort(port);
                    threadPoolDetailInfo.setApplicationName(applicationName);
                    threadPoolDetailInfo.setPoolName(poolName);
                    threadPoolDetailInfo.setIp(CommonCache.getIp());
                    threadPoolDetailInfo.setActivePoolSize(executorPeekRecordHolder.getActiveThreadCountPeek());
                    threadPoolDetailInfo.setComplexTaskCount(executor.getCompletedTaskCount());
                    threadPoolDetailInfo.setMaximumPoolSize(executor.getMaximumPoolSize());
                    threadPoolDetailInfo.setErrorTaskNum(executor.getErrorNum());
                    threadPoolDetailInfo.setQueueSize(executorPeekRecordHolder.getTaskQueueLengthPeek());
                    threadPoolDetailInfo.setRecordTime(CommonUtil.getCurrentTimeStamp());
                    reporter.doReportThreadPoolInfo(threadPoolDetailInfo);
                    executorPeekRecordHolder.clearActiveCountAndQueueSIze();
                }
                Thread.sleep(15000);
            } catch (Exception e) {
                LOGGER.error("[ThreadPoolWatcher] error is ", e);
            }
        }
    }
}
