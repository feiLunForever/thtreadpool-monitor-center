package org.idea.threadpool.monitor.core.config;

import org.idea.threadpool.monitor.core.executor.IExecutor;
import org.idea.threadpool.monitor.core.executor.IExecutorPeekRecordHolder;
import org.idea.threadpool.monitor.report.ReportInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author linhao
 * @Date created in 9:03 下午 2022/9/7
 */
public class CommonCache {

    private static String applicationName;
    private static String ip;
    private static Integer port;
    private static Map<String, IExecutor> executorMap;
    private static Map<String, AtomicInteger> taskTimesPer15Seconds = new HashMap<>();
    private static Map<String, AtomicInteger> errorTimesPer15Seconds = new HashMap<>();
    private static Map<String, AtomicInteger> tagTimesPer15Seconds = new HashMap<>();
    private static ArrayBlockingQueue<ReportInfo> arrayBlockingQueue = new ArrayBlockingQueue(10000);
    private static AtomicInteger topActiveCount = new AtomicInteger(0);
    private static AtomicInteger topQueueSize = new AtomicInteger(0);
    private static Map<String, IExecutorPeekRecordHolder> peekRecordHolderMap = new HashMap<>(0);


    public static Map<String, AtomicInteger> getTagTimesPer15Seconds() {
        return tagTimesPer15Seconds;
    }

    public static Map<String, AtomicInteger> getErrorTimesPer15Seconds() {
        return errorTimesPer15Seconds;
    }

    public static Map<String, IExecutorPeekRecordHolder> getPeekRecordHolderMap() {
        return peekRecordHolderMap;
    }

    public static AtomicInteger getTopActiveCount() {
        return topActiveCount;
    }

    public static AtomicInteger getTopQueueSize() {
        return topQueueSize;
    }

    public static ArrayBlockingQueue<ReportInfo> getArrayBlockingQueue() {
        return arrayBlockingQueue;
    }

    public static void setArrayBlockingQueue(ArrayBlockingQueue<ReportInfo> arrayBlockingQueue) {
        CommonCache.arrayBlockingQueue = arrayBlockingQueue;
    }

    public static String getApplicationName() {
        return applicationName;
    }

    public static void setApplicationName(String applicationName) {
        CommonCache.applicationName = applicationName;
    }

    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
        CommonCache.port = port;
    }

    public static Map<String, AtomicInteger> getTaskTimesPer15Seconds() {
        return taskTimesPer15Seconds;
    }

    public static void initTaskTimesPerMinuter() {
        for (String poolName : executorMap.keySet()) {
            CommonCache.taskTimesPer15Seconds.put(poolName, new AtomicInteger(0));
            CommonCache.errorTimesPer15Seconds.put(poolName, new AtomicInteger(0));
            CommonCache.tagTimesPer15Seconds.put(poolName, new AtomicInteger(0));
        }
    }

    public static void setTaskTimesPer15Seconds(Map<String, AtomicInteger> taskTimesPer15Seconds) {
        CommonCache.taskTimesPer15Seconds = taskTimesPer15Seconds;
    }

    public static Map<String, IExecutor> getExecutorMap() {
        return executorMap;
    }

    public static void setExecutorMap(Map<String, IExecutor> em) {
        executorMap = em;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String inputIp) {
        ip = inputIp;
    }
}
