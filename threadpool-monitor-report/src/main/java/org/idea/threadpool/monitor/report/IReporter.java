package org.idea.threadpool.monitor.report;


/**
 * @Author linhao
 * @Date created in 8:58 上午 2022/8/4
 */
public interface IReporter {

    /**
     * 上报线程池数据
     *
     * @param reportInfo
     */
    void doReportTask(String ip,Integer port,String applicationName,String tagId,ReportInfo reportInfo);

    /**
     * 上报线程池实时数据信息
     *
     * @param ip
     * @param port
     * @param applicationName
     * @param realTimeInfo
     */
    void doReportRealTime(String ip,Integer port,String applicationName,ThreadPoolRealTimeInfo realTimeInfo);

    /**
     * 上报告警邮件地址信息
     *
     * @param applicationName
     * @param alarmEmails
     */
    void doReportAlarmInfo(String applicationName,String alarmEmails);

    /**
     * 上报执行了多少个任务
     *
     * @param taskTimes
     * @param param
     */
    void doReportTaskTimes(Integer taskTimes, ThreadPoolDetailInfo param);

    /**
     * 上报这个应用的所有线程池属性
     *
     * @param totalDataInfo
     */
    void doReportTotalData(TotalDataInfo totalDataInfo);

    /**
     * 上报线程池的信息
     *
     * @param threadPoolDetailInfo
     */
    void doReportThreadPoolInfo(ThreadPoolDetailInfo threadPoolDetailInfo);

    /**
     * 记录任务异常发生时间
     *
     * @param i
     * @param threadPoolDetailInfo
     */
    void doReportErrorTaskTimes(int i, ThreadPoolDetailInfo threadPoolDetailInfo);

    /**
     * 记录写入标签的发生时间
     *
     * @param i
     * @param threadPoolDetailInfo
     */
    void doReportTagTimes(int i, ThreadPoolDetailInfo threadPoolDetailInfo);

}
