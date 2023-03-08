package com.atguigu.servicehosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.servicehosp.repository.ScheduleRepository;
import com.atguigu.servicehosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-08 18:12
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    /**
     * 上传排班接口
     *
     * @param map
     */
    @Override
    public void save(Map<String, Object> map) {
        String jsonString = JSONObject.toJSONString(map);
        Schedule schedule = JSONObject.parseObject(jsonString, Schedule.class);
        Schedule targetSchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if (targetSchedule != null) {
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);
        } else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);
        }
    }

    /**
     * 分页查询排班信息
     *
     * @param pageNum
     * @param pageSize
     * @param scheduleOrderVo
     * @return
     */
    @Override
    public Page<Schedule> getSchedulePageList(Integer pageNum, Integer pageSize, ScheduleOrderVo scheduleOrderVo) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleOrderVo, schedule);
        Example<Schedule> example = Example.of(schedule, matcher);
        Page<Schedule> schedulePage = scheduleRepository.findAll(example, pageable);
        return schedulePage;
    }

    /**
     * 删除排班接口
     *
     * @param hoscode
     * @param hosScheduleId
     */
    @Override
    public void deleteSchedule(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (schedule != null) {
            schedule.setIsDeleted(1);
            scheduleRepository.save(schedule);
        }
    }
}
