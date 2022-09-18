package org.idea.threadpool.monitor.console.vo.resp;

import java.util.Set;

/**
 * @Author linhao
 * @Date created in 4:56 下午 2022/9/10
 */
public class PoolNameAndAddressRespVO {

    private Set<String> poolNameSet;

    private Set<String> addressSet;

    public Set<String> getPoolNameSet() {
        return poolNameSet;
    }

    public void setPoolNameSet(Set<String> poolNameSet) {
        this.poolNameSet = poolNameSet;
    }

    public Set<String> getAddressSet() {
        return addressSet;
    }

    public void setAddressSet(Set<String> addressSet) {
        this.addressSet = addressSet;
    }
}
