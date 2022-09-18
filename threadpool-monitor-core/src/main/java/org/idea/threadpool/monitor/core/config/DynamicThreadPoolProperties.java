package org.idea.threadpool.monitor.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author linhao
 * @Date created in 5:44 下午 2022/8/4
 */
@ConfigurationProperties(prefix = "dynamic.threadpools")
public class DynamicThreadPoolProperties   {

    private String alarmEmails;

    private Map<String,IExecutorProperties> executors = new HashMap<>();

    public Map<String, IExecutorProperties> getExecutors() {
        return executors;
    }

    public void setExecutors(Map<String, IExecutorProperties> executors) {
        this.executors = executors;
    }

    public String getAlarmEmails() {
        return alarmEmails;
    }

    public void setAlarmEmails(String alarmEmails) {
        this.alarmEmails = alarmEmails;
    }
}
