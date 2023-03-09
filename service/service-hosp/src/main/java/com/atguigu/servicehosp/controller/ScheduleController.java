package com.atguigu.servicehosp.controller;

import com.atguigu.servicehosp.service.ScheduleService;
import com.atguigu.yygh.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-09 17:52
 */
@Api(tags = "排班控制器")
@RestController
@CrossOrigin
@RequestMapping("/admin/hosp/Schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("根据医生编号，科室编号，分页查询排班信息")
    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable("page") Integer page,
                                  @PathVariable("limit") Integer limit,
                                  @PathVariable("hoscode") String hoscode,
                                  @PathVariable("depcode") String depcode) {
        Map<String, Object> scheduleRuleMap = scheduleService.getScheduleRule(page, limit, hoscode, depcode);
        return Result.ok(scheduleRuleMap);
    }
}
