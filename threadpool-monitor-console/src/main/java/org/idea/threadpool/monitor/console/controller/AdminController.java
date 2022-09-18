package org.idea.threadpool.monitor.console.controller;

import org.idea.threadpool.monitor.console.config.IResponse;
import org.idea.threadpool.monitor.console.service.AdminService;
import org.idea.threadpool.monitor.console.vo.req.LoginReqVO;
import org.idea.threadpool.monitor.console.vo.resp.LoginRespVO;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author linhao
 * @Date created in 7:11 下午 2022/9/8
 */
@RestController
@RequestMapping("admin")
public class AdminController {

    private AdminService adminService;

    @PostMapping("login")
    public IResponse<LoginRespVO> login(LoginReqVO loginReqVO) {
        if (StringUtils.isEmpty(loginReqVO.getPwd()) || StringUtils.isEmpty(loginReqVO.getSid())) {
            return IResponse.error(null, "登录账号异常");
        }
        LoginRespVO loginResult = adminService.login(loginReqVO);
        if (StringUtils.isEmpty(loginResult.getToken())) {
            return IResponse.error(null,"登录账号异常");
        }
        return IResponse.success(loginResult);
    }

    @PostMapping("logout")
    public IResponse logout(HttpServletRequest request){
        String usiToken = request.getHeader("usi");
        adminService.logout(usiToken);
        return IResponse.success();
    }
}
