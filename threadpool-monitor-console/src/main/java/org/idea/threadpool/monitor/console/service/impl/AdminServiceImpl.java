package org.idea.threadpool.monitor.console.service.impl;

import org.idea.threadpool.monitor.console.service.AdminService;
import org.idea.threadpool.monitor.console.vo.req.LoginReqVO;
import org.idea.threadpool.monitor.console.vo.resp.LoginRespVO;
import org.idea.threadpool.monitor.report.redis.IRedisService;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author linhao
 * @Date created in 7:18 下午 2022/9/8
 */
public class AdminServiceImpl implements AdminService {

    @Value("${console.admin.username}")
    private String adminName;
    @Value("${console.admin.password}")
    private String adminPwd;
    @Resource
    private IRedisService redisService;

    private static String CONSOLE_ADMIN_PREFIX = "threadpool:console:admin:";

    @Override
    public LoginRespVO login(LoginReqVO loginReqVO) {
        LoginRespVO result = new LoginRespVO();
        if (adminName.equals(loginReqVO.getSid()) && adminPwd.equals(loginReqVO.getPwd())) {
            String token = UUID.randomUUID().toString();
            result.setToken(token);
            redisService.setStr(CONSOLE_ADMIN_PREFIX + token, "1", 6, TimeUnit.HOURS);
            return result;
        }
        return result;
    }

    @Override
    public boolean logout(String usiToken) {
        if (redisService.exists(CONSOLE_ADMIN_PREFIX + usiToken)) {
            redisService.del(CONSOLE_ADMIN_PREFIX + usiToken);
        }
        return true;
    }
}
