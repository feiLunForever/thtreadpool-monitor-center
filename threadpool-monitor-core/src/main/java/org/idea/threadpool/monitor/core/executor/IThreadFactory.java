package org.idea.threadpool.monitor.core.executor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Author linhao
 * @Date created in 9:08 上午 2022/8/4
 */
public class IThreadFactory implements ThreadFactory {

    private LongAdder longAdder = new LongAdder();
    private final ThreadGroup group;
    private String threadPrefix;

    public IThreadFactory(String prefix) {
        this.threadPrefix = prefix;
        SecurityManager securityManager = System.getSecurityManager();
        group = ((securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup());
    }

    @Override
    public Thread newThread(Runnable r) {
        String threadPrefixSeq = threadPrefix + "-" + longAdder.longValue();
        Thread thread = new Thread(group, r, threadPrefixSeq, 0);
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
