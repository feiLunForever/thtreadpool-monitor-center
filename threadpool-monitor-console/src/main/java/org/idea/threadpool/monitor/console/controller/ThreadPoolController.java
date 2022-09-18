package org.idea.threadpool.monitor.console.controller;

import org.idea.threadpool.monitor.console.config.IResponse;
import org.idea.threadpool.monitor.console.service.ThreadPoolService;
import org.idea.threadpool.monitor.console.vo.req.ThreadPoolReqVO;
import org.idea.threadpool.monitor.console.vo.resp.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 线程池监控基础接口
 *
 * @Author linhao
 * @Date created in 10:04 下午 2022/9/7
 */
@RestController
@RequestMapping("threadPool")
public class ThreadPoolController {

    @Resource
    private ThreadPoolService threadPoolService;

    @PostMapping("getApplicationNames")
    public IResponse<List<String>> getApplicationNames() {
        return IResponse.success(threadPoolService.getApplicationNames());
    }

    @PostMapping("getThreadPoolList")
    public IResponse<List<ThreadPoolListRespVO>> getThreadPoolList(@RequestBody ThreadPoolReqVO threadPoolReqVO) {
        List<ThreadPoolListRespVO> resultList = threadPoolService.getThreadPoolList(threadPoolReqVO.getApplicationName());
        return IResponse.success(resultList);
    }

    @PostMapping("getDetailRecord")
    public IResponse<PoolRecordRespVO> getDetailRecord(@RequestBody ThreadPoolReqVO threadPoolReqVO) {
        String[] addressArr = threadPoolReqVO.getIpAndPort().split(":");
        threadPoolReqVO.setIp(addressArr[0]);
        threadPoolReqVO.setPort(Integer.valueOf(addressArr[1]));
        return IResponse.success(threadPoolService.getDetailRecord(threadPoolReqVO));
    }

    @PostMapping("getAddress")
    public IResponse<PoolNameAndAddressRespVO> getAddressAndPoolName(@RequestBody ThreadPoolReqVO threadPoolReqVO) {
        return IResponse.success(threadPoolService.getAddressAndPoolName(threadPoolReqVO));
    }

    @PostMapping("getJobRecord")
    public IResponse<JobListRespVO> getJobRecord(@RequestBody ThreadPoolReqVO threadPoolReqVO) {
        String[] addressArr = threadPoolReqVO.getIpAndPort().split(":");
        threadPoolReqVO.setIp(addressArr[0]);
        threadPoolReqVO.setPort(Integer.valueOf(addressArr[1]));
        return IResponse.success(threadPoolService.getJobRecord(threadPoolReqVO));
    }

    @PostMapping("getJobTagList")
    public IResponse<List<String>> getJobTagList(@RequestBody ThreadPoolReqVO threadPoolReqVO) {
        return IResponse.success(threadPoolService.getJobTagList(threadPoolReqVO));
    }

    @PostMapping("getAlarmInfo")
    public IResponse<List<AlarmInfo>> getAlarmInfos(@RequestBody ThreadPoolReqVO threadPoolReqVO) {
        return IResponse.success(threadPoolService.getAlarmInfos(threadPoolReqVO));
    }

    @GetMapping("getTotalData")
    public IResponse<TotalData> getTotalData(){
        return IResponse.success(threadPoolService.getTotalData());
    }

}
