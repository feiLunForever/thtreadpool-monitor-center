package org.idea.threadpool.monitor.core.config;

import org.idea.threadpool.monitor.core.executor.IExecutor;
import org.idea.threadpool.monitor.core.executor.IExecutorPeekRecordHolder;
import org.idea.threadpool.monitor.core.job.*;
import org.idea.threadpool.monitor.core.util.CommonUtil;
import org.idea.threadpool.monitor.report.IReporter;
import org.idea.threadpool.monitor.report.redis.RedisAsyncReporter;
import org.idea.threadpool.monitor.report.TotalDataInfo;
import org.idea.threadpool.monitor.report.redis.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author linhao
 * @Date created in 5:41 下午 2022/8/4
 */
@Configuration
@Import({DynamicThreadPoolConfigInitHandler.class})
@EnableConfigurationProperties(value = {DynamicThreadPoolProperties.class, RedisProperties.class})
public class DynamicThreadPoolConfig implements InitializingBean {

    @Resource
    private DynamicThreadPoolProperties dynamicThreadPoolProperties;
    @Resource
    private ApplicationContext applicationContext;
    @Value("${spring.application.name:defaultName:noBodyKnowApplication}")
    private String applicationName;
    @Value("${server.port:8080}")
    private Integer port;

    private IReporter reporter;
    private Map<String, IExecutor> executorMap;
    private IRedisService redisService;

    private ExecutorService jobPool = Executors.newFixedThreadPool(10);

    @Bean
    public IRedisFactory redisFactory(RedisProperties redisProperties) {
        return new RedisFactoryImpl(redisProperties);
    }

    @Bean
    @ConditionalOnBean(IRedisFactory.class)
    public IRedisService redisService(IRedisFactory redisFactory) {
        this.redisService = new RedisServiceImpl(redisFactory);
        return redisService;
    }

    @Override
    public void afterPropertiesSet() {
        IRedisService redisService = applicationContext.getBean(IRedisService.class);
        this.executorMap = applicationContext.getBeansOfType(IExecutor.class);
        this.reporter = new RedisAsyncReporter(redisService);
        this.initReporter();
        this.initCommonCache();
        this.initPeekRecordHolder();
        //这里有四个线程任务要处理
        this.initThreadPoolInfoWatcher(); //负责采集线程池的每15秒的activeCount和queueSize数据
        this.initTaskTimesWatcher(); //负责采集线程池的每15秒请求量
        this.initThreadPoolRealTimeWatcher(); //负责采集线程池每秒的数据信息，实时变化
        this.initAsyncConsumer();
        this.initRefreshProperties();
    }

    private void initPeekRecordHolder() {
        Map<String, IExecutorPeekRecordHolder> executorPeekRecordHolderMap = CommonCache.getPeekRecordHolderMap();
        for (String poolName : executorMap.keySet()) {
            executorPeekRecordHolderMap.put(poolName,new IExecutorPeekRecordHolder());
        }
    }

    private void initCommonCache() {
        CommonCache.setApplicationName(applicationName);
        CommonCache.setPort(port);
        CommonCache.setIp(CommonUtil.getIpAddress());
        CommonCache.setExecutorMap(executorMap);
        CommonCache.initTaskTimesPerMinuter();
    }

    //初始化线程池的数据上报器
    private void initReporter() {
        Map<String, IExecutorProperties> executorPropertiesMap = dynamicThreadPoolProperties.getExecutors();
        for (String executorName : executorMap.keySet()) {
            IExecutorProperties executorProperties = executorPropertiesMap.get(executorName);
            IExecutor executor = executorMap.get(executorName);
            executor.setReporter(reporter);
        }
    }

    //初始化线程池上报任务
    private void initThreadPoolInfoWatcher() {
        Map<String, IExecutorProperties> executorPropertiesMap = dynamicThreadPoolProperties.getExecutors();
        for (String executorsPropertiesName : executorPropertiesMap.keySet()) {
            IExecutorProperties executorProperties = executorPropertiesMap.get(executorsPropertiesName);
            if (!Boolean.valueOf(executorProperties.getWatcher())) {
                executorMap.remove(executorsPropertiesName);
            }
        }
        ThreadPoolWatcher watcher = new ThreadPoolWatcher();
        watcher.setApplicationName(applicationName);
        watcher.setExecutorMap(executorMap);
        watcher.setPort(port);
        watcher.setReporter(reporter);
        jobPool.execute(watcher);
    }

    //初始化线程池每15秒的请求量监控任务
    private void initTaskTimesWatcher() {
        jobPool.execute(new TaskTimesWatcher(reporter));
    }

    //初始化线程池实时数据监控
    private void initThreadPoolRealTimeWatcher() {
        jobPool.execute(new RealTimeInfoWatcher(redisService,executorMap,applicationContext, reporter,dynamicThreadPoolProperties));
    }

    private void initRefreshProperties(){
        jobPool.execute(new DynamicThreadPoolRefreshWatcher(applicationContext));
    }
    private void initAsyncConsumer() {
        jobPool.execute(new AsyncConsumer(reporter, redisService));
    }
}
