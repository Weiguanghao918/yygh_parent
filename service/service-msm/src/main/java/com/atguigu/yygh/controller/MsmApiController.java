package com.atguigu.yygh.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.service.MsmService;
import com.atguigu.yygh.utils.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Guanghao Wei
 * @create 2023-03-10 15:19
 */
@Api(tags = "短信服务控制器")
@RestController
@RequestMapping("/api/msm")
public class MsmApiController {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation("发送短信")
    @GetMapping("/send/{phone}")
    public Result sendCode(@PathVariable("phone") String phone) {
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return Result.ok();
        }
        code = RandomUtil.getSixBitRandom();
        Boolean isSend = msmService.send(phone, code);
        if (isSend) {
            redisTemplate.opsForValue().set(phone, code, 2, TimeUnit.MINUTES);
            return Result.ok();
        } else {
            return Result.fail().message("短信发送失败");
        }
    }
}
