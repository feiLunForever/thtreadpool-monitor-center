package org.idea.threadpool.monitor.report.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author linhao
 * @Date created in 3:58 下午 2021/5/28
 */
@ConfigurationProperties(prefix = "threadpool.store.redis")
@Configuration
public class RedisProperties {

    /**对应的数据库编号**/
    private Integer index;
    private String password;
    private Integer port;
    private String des;
    private Integer maxIdle;
    private Integer minIdle;
    private String host;
    private Integer timeOut;
    private Long maxWaitMillis;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public Long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(Long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    @Override
    public String toString() {
        return "RedisProperties{" +
                "index=" + index +
                ", password='" + password + '\'' +
                ", port=" + port +
                ", des='" + des + '\'' +
                ", maxIdle=" + maxIdle +
                ", minIdle=" + minIdle +
                ", host='" + host + '\'' +
                ", timeOut=" + timeOut +
                ", maxWaitMillis=" + maxWaitMillis +
                '}';
    }
}
