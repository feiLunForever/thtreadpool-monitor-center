package org.idea.threadpool.monitor.console.service;

import org.idea.threadpool.monitor.console.vo.req.LoginReqVO;
import org.idea.threadpool.monitor.console.vo.resp.LoginRespVO;

/**
 * @Author linhao
 * @Date created in 7:17 下午 2022/9/8
 */
public interface AdminService {

    /**
     * 登录请求
     *
     * @param loginReqVO
     * @return
     */
    LoginRespVO login(LoginReqVO loginReqVO);

    /**
     * 注销操作
     *
     * @param usiToken
     * @return
     */
    boolean logout(String usiToken);
}
