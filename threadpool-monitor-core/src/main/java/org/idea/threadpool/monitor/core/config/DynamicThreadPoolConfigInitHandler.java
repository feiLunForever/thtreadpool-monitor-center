package org.idea.threadpool.monitor.core.config;

import org.idea.threadpool.monitor.core.executor.IExecutor;
import org.idea.threadpool.monitor.core.executor.ResizableCapacityLinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 需要在Spring的ApplicationContext初始化之前就先加载动态线程池的参数配置
 *
 * @Author idea
 * @Date created in 7:16 下午 2021/11/19
 */
public class DynamicThreadPoolConfigInitHandler implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    public static final String DYNAMIC_THREAD_POOL_PREFIX = "dynamic.threadpools.executors";

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicThreadPoolConfigInitHandler.class);

    private DynamicThreadPoolProperties dynamicThreadPoolProperties = new DynamicThreadPoolProperties();

    private Environment environment;

    /**
     * 预留给子类扩展，例如自定义拒绝策略
     * todo
     */
    public void beforeInitDynamicThreadPoolPropertiesBeforeContextRefresh() {
    }

    /**
     * 预留给子类扩展
     * todo
     */
    public void afterInitDynamicThreadPoolPropertiesBeforeContextRefresh() {
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beforeInitDynamicThreadPoolPropertiesBeforeContextRefresh();
        this.initDynamicThreadPoolPropertiesBeforeContextRefresh();
        try {
            this.registryThreadPoolExecutor(dynamicThreadPoolProperties, beanDefinitionRegistry);
        } catch (Exception e) {
            LOGGER.error("[registerBeanDefinitions] error is ", e);
        }
        this.afterInitDynamicThreadPoolPropertiesBeforeContextRefresh();
    }

    /**
     * SpringBoot 应用中的EnvironmentUtils 实现源代码
     *
     * @param environment
     * @param keyPrefix
     * @return
     */
    private static Map<String, String> getSubProperties(Environment environment, String keyPrefix) {
        return Binder.get(environment)
                .bind(keyPrefix, Bindable.mapOf(String.class, String.class))
                .orElseGet(Collections::emptyMap);
    }

    /**
     * 动态刷新线程池属性
     */
    private void initDynamicThreadPoolPropertiesBeforeContextRefresh() {
        Map<String, String> dynamicThreadPoolConfig = getSubProperties(environment, DYNAMIC_THREAD_POOL_PREFIX);
        Map<String, Map<String, String>> executorMap = new HashMap<>();
        for (String dynamicThreadPoolConfigKey : dynamicThreadPoolConfig.keySet()) {
            if("alarmEmails".equals(dynamicThreadPoolConfigKey)) {
                dynamicThreadPoolProperties.setAlarmEmails(dynamicThreadPoolConfig.get(dynamicThreadPoolConfigKey));
                continue;
            }
            String executorsName = dynamicThreadPoolConfigKey.substring(0, dynamicThreadPoolConfigKey.indexOf("."));
            String field = dynamicThreadPoolConfigKey.replaceAll(executorsName + ".", "");
            Map<String, String> itemMaps = executorMap.get(executorsName);
            if (itemMaps == null) {
                itemMaps = new HashMap<>();
            }
            itemMaps.put(field, dynamicThreadPoolConfig.get(dynamicThreadPoolConfigKey));
            executorMap.put(executorsName, itemMaps);
        }

        for (String executorName : executorMap.keySet()) {
            Map<String, String> propertiesMap = executorMap.get(executorName);
            IExecutorProperties executorProperties = new IExecutorProperties();
            for (String field : propertiesMap.keySet()) {
                try {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field, IExecutorProperties.class);
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    writeMethod.invoke(executorProperties, propertiesMap.get(field));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dynamicThreadPoolProperties.getExecutors().put(executorName, executorProperties);
        }
    }

    private void registryThreadPoolExecutor(DynamicThreadPoolProperties dynamicThreadPoolProperties, BeanDefinitionRegistry beanDefinitionRegistry) {
        for (String poolName : dynamicThreadPoolProperties.getExecutors().keySet()) {
            IExecutorProperties properties = dynamicThreadPoolProperties.getExecutors().get(poolName);
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(IExecutor.class);
            beanDefinitionBuilder.addConstructorArgValue(Integer.valueOf(properties.getCorePoolSize()));
            beanDefinitionBuilder.addConstructorArgValue(Integer.valueOf(properties.getMaximumPoolSize()));
            beanDefinitionBuilder.addConstructorArgValue(Integer.valueOf(properties.getKeepAliveTime()));
            beanDefinitionBuilder.addConstructorArgValue(TimeUnit.MILLISECONDS);
            beanDefinitionBuilder.addConstructorArgValue(poolName);
            beanDefinitionBuilder.addConstructorArgValue(new ResizableCapacityLinkedBlockingQueue<Runnable>(Integer.parseInt(properties.getQueueCapacity())));
            beanDefinitionBuilder.addConstructorArgValue(new ThreadPoolExecutor.AbortPolicy());
            beanDefinitionBuilder.addConstructorArgValue(Boolean.valueOf(properties.getAllowsCoreThreadTimeOut()));
            beanDefinitionBuilder.addConstructorArgValue(Boolean.valueOf(properties.getPreStartCoreThread()));
            beanDefinitionBuilder.addConstructorArgValue(Boolean.valueOf(properties.getPreStartAllCoreThreads()));
            beanDefinitionBuilder.addConstructorArgValue(Double.valueOf(properties.getTaskCountScoreThreshold()));
            BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
            beanDefinitionRegistry.registerBeanDefinition(poolName, beanDefinition);
            LOGGER.info(" ===================== [registryThreadPoolExecutor] poolName:{} registry success! ===================== ", poolName);
        }
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
