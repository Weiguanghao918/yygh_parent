package com.atguigu.servicehosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-08 18:12
 */
public interface ScheduleService {
    void save(Map<String, Object> map);

    Page<Schedule> getSchedulePageList(Integer pageNum, Integer pageSize, ScheduleOrderVo scheduleOrderVo);

    void deleteSchedule(String hoscode, String hosScheduleId);

    Map<String, Object> getScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    List<Schedule> getScheduleDetails(String hoscode, String depcode, String workDate);
}
