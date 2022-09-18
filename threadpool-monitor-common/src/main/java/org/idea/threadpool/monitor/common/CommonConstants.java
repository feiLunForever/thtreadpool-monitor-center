package org.idea.threadpool.monitor.common;

import java.util.Arrays;
import java.util.List;

/**
 * @Author linhao
 * @Date created in 11:02 下午 2022/9/7
 */
public class CommonConstants {

    public static final String TASK_INFO_LIST = "thread_pool:job_info_list";
    public static final String THREAD_POOL_DETAIL_INFO_LIST = "thread_pool:detail_info_list";
    public static final String THREAD_POOL_TOTAL_DATA = "thread_pool:total:data";
    public static final String THREAD_POOL_TASK_TIMES = "thread_pool:task_times";
    public static final String THREAD_POOL_REAL_TIMES = "thread_pool:real_times";
    public static final String TAG_MAP = "thread_pool:tag_map";
    public static final String ALARM_MAP = "thread_pool:alarm_map";
    public static final String ERROR_TASK_TIMES = "thread_pool:error_task_times";
    public static final String TAG_TIMES = "thread_pool:tag_times";

    private static String ERROR_ALARM_TITLE = "动态线程池告警[%s]";
    private static String ERROR_ALARM_CONTENT = "线程池出现负载异常，applicationName:%s, address:%s, poolName:%s";

    public static String buildErrorAlarmTitle(String env) {
        return String.format(ERROR_ALARM_TITLE, env);
    }

    public static String buildErrorAlarmContent(String applicationName, String address, String poolName) {
        return String.format(ERROR_ALARM_CONTENT, applicationName, address, poolName);
    }

    public static String buildJobInfoListKey(String ip, Integer port, String applicationName, String queryDate, String poolName, String tag) {
        List<String> keyNameList = Arrays.asList(CommonConstants.TASK_INFO_LIST,
                ip,
                String.valueOf(port),
                applicationName,
                queryDate,
                poolName,
                tag);
        return String.join(":", keyNameList);
    }

    public static String buildTagKey(String applicationName, String poolName) {
        List<String> keyNameList = Arrays.asList(CommonConstants.TAG_MAP,
                applicationName,
                poolName);
        return String.join(":", keyNameList);
    }

    public static String buildAlarmKey(String applicationName) {
        List<String> keyNameList = Arrays.asList(CommonConstants.ALARM_MAP,
                applicationName);
        return String.join(":", keyNameList);
    }

    public static String buildThreadPoolRealTimeKey(String ip, Integer port, String applicationName, String poolName) {
        List<String> keyNameList = Arrays.asList(CommonConstants.THREAD_POOL_REAL_TIMES,
                ip,
                String.valueOf(port),
                applicationName,
                poolName);
        return String.join(":", keyNameList);
    }


    public static String buildThreadPoolTaskTimesKey(String ip, Integer port, String applicationName, String poolName, String dayKey) {
        List<String> keyNameList = Arrays.asList(CommonConstants.THREAD_POOL_TASK_TIMES,
                dayKey,
                ip,
                String.valueOf(port),
                applicationName,
                poolName);
        return String.join(":", keyNameList);
    }

    public static String buildTotalDataKey(String applicationName) {
        List<String> keyNameList = Arrays.asList(CommonConstants.THREAD_POOL_TOTAL_DATA, applicationName);
        return String.join(":", keyNameList);
    }

    public static String buildThreadPoolDetailInfoKey(String ip, Integer port, String applicationName, String poolName, String dayKey) {
        List<String> keyNameList = Arrays.asList(CommonConstants.THREAD_POOL_DETAIL_INFO_LIST,
                dayKey,
                ip,
                String.valueOf(port),
                applicationName,
                poolName);
        return String.join(":", keyNameList);
    }

    public static String buildErrorTaskKey(String ip, Integer port, String applicationName, String poolName, String dayKey) {
        List<String> keyNameList = Arrays.asList(CommonConstants.ERROR_TASK_TIMES,
                dayKey,
                ip,
                String.valueOf(port),
                applicationName,
                poolName);
        return String.join(":", keyNameList);
    }

    public static String buildTagCountKey(String ip, Integer port, String applicationName, String poolName, String dayKey) {
        List<String> keyNameList = Arrays.asList(CommonConstants.TAG_TIMES,
                dayKey,
                ip,
                String.valueOf(port),
                applicationName,
                poolName);
        return String.join(":", keyNameList);
    }
}
