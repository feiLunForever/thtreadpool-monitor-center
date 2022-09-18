package org.idea.threadpool.monitor.core.executor;

/**
 * @Author linhao
 * @Date created in 8:35 下午 2022/8/3
 */
public class IExecutorJob implements Runnable{

    private String tag;
    private Runnable runnable;

    public IExecutorJob(String tag, Runnable runnable) {
        this.tag = tag;
        this.runnable = runnable;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        runnable.run();
    }
}
