package org.idea.threadpool.monitor.core.job;

import org.idea.threadpool.monitor.core.config.CommonCache;
import org.idea.threadpool.monitor.report.IReporter;
import org.idea.threadpool.monitor.report.ThreadPoolDetailInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 统计每分钟执行了多少任务
 *
 * @Author linhao
 * @Date created in 9:00 上午 2022/9/10
 */
public class TaskTimesWatcher implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(TaskTimesWatcher.class);

    private IReporter reporter;

    public TaskTimesWatcher(IReporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (String poolName : CommonCache.getTaskTimesPer15Seconds().keySet()) {

                    AtomicInteger taskCount = CommonCache.getTaskTimesPer15Seconds().get(poolName);
                    AtomicInteger errorCount = CommonCache.getErrorTimesPer15Seconds().get(poolName);
                    AtomicInteger tagCount = CommonCache.getTagTimesPer15Seconds().get(poolName);
                    ThreadPoolDetailInfo threadPoolDetailInfo = new ThreadPoolDetailInfo();
                    threadPoolDetailInfo.setApplicationName(CommonCache.getApplicationName());
                    threadPoolDetailInfo.setPoolName(poolName);
                    threadPoolDetailInfo.setPort(CommonCache.getPort());
                    threadPoolDetailInfo.setIp(CommonCache.getIp());
                    reporter.doReportTaskTimes(taskCount.get(), threadPoolDetailInfo);
                    reporter.doReportErrorTaskTimes(errorCount.get(), threadPoolDetailInfo);
                    reporter.doReportTagTimes(tagCount.get(), threadPoolDetailInfo);
                    logger.info("[TaskTimesWatcher] do report,poolName is {},taskCount is {},tagCount is {},errorCount is {}", poolName, taskCount.get(), tagCount.get(), errorCount.get());
                    //减去之前15秒内已经计算过的次数
                    CommonCache.getTaskTimesPer15Seconds().get(poolName).addAndGet(-1 * taskCount.get());
                    CommonCache.getErrorTimesPer15Seconds().get(poolName).addAndGet(-1 * errorCount.get());
                    CommonCache.getTagTimesPer15Seconds().get(poolName).addAndGet(-1 * tagCount.get());
                }
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
