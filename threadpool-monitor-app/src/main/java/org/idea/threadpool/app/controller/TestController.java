package org.idea.threadpool.app.controller;

import org.apache.commons.lang3.RandomUtils;
import org.idea.threadpool.monitor.core.executor.IExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author linhao
 * @Date created in 12:02 下午 2022/8/16
 */
@RestController
public class TestController {

    @Resource
    private IExecutor testExecutor;
    @Resource
    private IExecutor commonExecutor;

    @GetMapping(value = "/common/add-job")
    public void doCommon() {
        commonExecutor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("测试数据！！");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @GetMapping(value = "/do-pay")
    public void doPay() {
        for(int i=0;i<100;i++) {
            testExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("随机执行任务");
                    try {
                        Thread.sleep(100 + RandomUtils.nextInt(0, 10) * 100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },"订单支付");
        }
    }


    @GetMapping(value = "/do-job")
    public void doJob() {
        for(int i=0;i<100;i++) {
            testExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("随机执行任务");
                    try {
                        Thread.sleep(1000 + RandomUtils.nextInt(0, 10) * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/test/add-job")
    public void doTest(int i) {
        testExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int j = 10 / i;
                System.out.println("测试数据！！");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "测试任务-01");
    }

}
