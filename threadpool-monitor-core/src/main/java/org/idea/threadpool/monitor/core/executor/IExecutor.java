package org.idea.threadpool.monitor.core.executor;

import org.idea.threadpool.monitor.core.config.CommonCache;
import org.idea.threadpool.monitor.core.util.CommonUtil;
import org.idea.threadpool.monitor.report.IReporter;
import org.idea.threadpool.monitor.report.ReportInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author linhao
 * @Date created in 8:31 下午 2022/8/3
 */
public class IExecutor extends ThreadPoolExecutor {

    private static Logger logger = LoggerFactory.getLogger(IExecutor.class);

    private String poolName;
    private AtomicLong errorTaskNum = new AtomicLong();

    private IReporter reporter;
    private Boolean preStartCoreThread;
    private Boolean preStartAllCoreThreads;
    private Double taskCountScoreThreshold;
    private ResizableCapacityLinkedBlockingQueue workQueue;

    private final ThreadLocal<Map<String, Object>> taskContent = new ThreadLocal<>();

    public IExecutor(int corePoolSize,
                     int maximumPoolSize,
                     long keepAliveTime,
                     TimeUnit unit,
                     String poolName,
                     ResizableCapacityLinkedBlockingQueue<Runnable> workQueue,
                     RejectedExecutionHandler handler,
                     Boolean allowCoreThreadTimeOut,
                     Boolean preStartCoreThread,
                     Boolean preStartAllCoreThreads,
                     Double taskCountScoreThreshold) {

        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new IThreadFactory(poolName), handler);
        this.poolName = poolName;
        this.taskCountScoreThreshold = taskCountScoreThreshold;
        super.allowCoreThreadTimeOut(allowCoreThreadTimeOut);
        this.preStartCoreThread = preStartCoreThread;
        this.preStartAllCoreThreads = preStartAllCoreThreads;
        this.workQueue = workQueue;
        if (preStartCoreThread) {
            super.prestartCoreThread();
        }
        if (preStartAllCoreThreads) {
            super.prestartAllCoreThreads();
        }
    }

    public int getQueueCapacity() {
        return workQueue.getCapacity();
    }

    /**
     * 获取队列体积
     *
     * @return
     */
    public int getQueueSize() {
        if (super.getQueue() == null) {
            return 0;
        }
        return super.getQueue().size();
    }

    @Override
    public void execute(Runnable command) {
        IExecutorJob executorJob = new IExecutorJob("", command);
        super.execute(executorJob);
    }

    public void execute(Runnable command, String tag) {
        IExecutorJob executorJob = new IExecutorJob(tag, command);
        super.execute(executorJob);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        String jobId = UUID.randomUUID().toString();
        Map<String, Object> threadInfoMap = new HashMap<>(4);
        if (r instanceof IExecutorJob) {
            IExecutorJob iExecutorJob = (IExecutorJob) r;
            threadInfoMap.put("tag", iExecutorJob.getTag());
        }
        threadInfoMap.put("jobId", jobId);
        threadInfoMap.put("beginTime", System.currentTimeMillis());
        taskContent.set(threadInfoMap);
        CommonCache.getPeekRecordHolderMap().get(poolName).recordActiveThreadCountPeek(getActiveCount());
        CommonCache.getPeekRecordHolderMap().get(poolName).recordTaskQueueLengthPeek(getQueueSize());
        logger.info("[beforeExecute] threadInfoMap is {}", threadInfoMap);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        Map<String, Object> threadInfoMap = taskContent.get();
        try {
            long beginTime = (long) threadInfoMap.get("beginTime");
            long executeTime = System.currentTimeMillis() - beginTime;
            String tag = evalString(threadInfoMap.get("tag"));
            ReportInfo reportInfo = new ReportInfo();
            reportInfo.setExecuteTime(executeTime);
            reportInfo.setPoolName(this.getPoolName());
            reportInfo.setJobId(String.valueOf(threadInfoMap.get("jobId")));
            reportInfo.setTag(tag);
            reportInfo.setCreateTime(CommonUtil.getCurrentTimeStamp());
            reportInfo.setStatus(0);
            if (t != null) {
                reportInfo.setStatus(1);
                errorTaskNum.getAndIncrement();
                CommonCache.getErrorTimesPer15Seconds().get(poolName).incrementAndGet();
            }
            if (!StringUtils.isEmpty(tag)) {
                CommonCache.getTagTimesPer15Seconds().get(poolName).incrementAndGet();
                CommonCache.getArrayBlockingQueue().put(reportInfo);
            }
            CommonCache.getTaskTimesPer15Seconds().get(poolName).addAndGet(1);
        } catch (Exception e) {
            logger.error("[afterExecute] error is ", e);
        } finally {
            taskContent.remove();
            logger.info("[afterExecute] threadInfoMap is {}", threadInfoMap);
        }
    }


    public static String evalString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public long getErrorNum() {
        return errorTaskNum.get();
    }

    @Override
    public int getCorePoolSize() {
        return super.getCorePoolSize();
    }

    @Override
    public long getTaskCount() {
        return super.getTaskCount();
    }

    @Override
    public long getCompletedTaskCount() {
        return super.getCompletedTaskCount();
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public IReporter getReporter() {
        return reporter;
    }

    public void setReporter(IReporter reporter) {
        this.reporter = reporter;
    }

    public AtomicLong getErrorTaskNum() {
        return errorTaskNum;
    }

    public void setErrorTaskNum(AtomicLong errorTaskNum) {
        this.errorTaskNum = errorTaskNum;
    }

    public Boolean getPreStartCoreThread() {
        return preStartCoreThread;
    }

    public void setPreStartCoreThread(Boolean preStartCoreThread) {
        this.preStartCoreThread = preStartCoreThread;
    }

    public Boolean getPreStartAllCoreThreads() {
        return preStartAllCoreThreads;
    }

    public void setPreStartAllCoreThreads(Boolean preStartAllCoreThreads) {
        this.preStartAllCoreThreads = preStartAllCoreThreads;
    }

    public ThreadLocal<Map<String, Object>> getTaskContent() {
        return taskContent;
    }

    public Double getTaskCountScoreThreshold() {
        return taskCountScoreThreshold;
    }

    public void setTaskCountScoreThreshold(Double taskCountScoreThreshold) {
        this.taskCountScoreThreshold = taskCountScoreThreshold;
    }
}
