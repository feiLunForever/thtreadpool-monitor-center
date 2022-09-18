package org.idea.threadpool.monitor.core.job;

import org.idea.threadpool.monitor.common.CommonConstants;
import org.idea.threadpool.monitor.core.config.CommonCache;
import org.idea.threadpool.monitor.report.IReporter;
import org.idea.threadpool.monitor.report.ReportInfo;
import org.idea.threadpool.monitor.report.redis.IRedisService;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author linhao
 * @Date created in 9:17 上午 2022/9/12
 */
public class AsyncConsumer implements Runnable {

    private IReporter reporter;
    private IRedisService redisService;

    public AsyncConsumer(IReporter reporter, IRedisService redisService) {
        this.reporter = reporter;
        this.redisService = redisService;
    }

    @Override
    public void run() {
        while (true) {
            ReportInfo reportInfo = null;
            try {
                reportInfo = CommonCache.getArrayBlockingQueue().take();
                String tagKey = CommonConstants.buildTagKey(CommonCache.getApplicationName(), reportInfo.getPoolName());
                String tagUUid = (String) redisService.getMapField(tagKey, reportInfo.getTag(), String.class);
                if (StringUtils.isEmpty(tagUUid)) {
                    tagUUid = UUID.randomUUID().toString();
                    redisService.setMapItem(tagKey, reportInfo.getTag(), tagUUid);
                }
                reporter.doReportTask(CommonCache.getIp(), CommonCache.getPort(), CommonCache.getApplicationName(), tagUUid, reportInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
