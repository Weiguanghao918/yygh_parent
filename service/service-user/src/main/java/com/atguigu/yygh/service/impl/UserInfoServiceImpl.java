package com.atguigu.yygh.service.impl;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.JwtHelper;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.mapper.UserInfoMapper;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-10 14:30
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 登录功能
     *
     * @param loginVo
     * @return
     */
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
        //获取手机号和密码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //校验验证码
        String mobleCode = redisTemplate.opsForValue().get(phone);
        if (!code.equals(mobleCode)) {
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        //手机号已经被使用
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setStatus(1);
            userInfo.setPhone(phone);
            userInfoMapper.insert(userInfo);
        }
        //校验是否被禁用
        if (userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //记录登录

        //返回页面显示名称
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("name", name);
        map.put("token", token);
        return map;
    }
}
