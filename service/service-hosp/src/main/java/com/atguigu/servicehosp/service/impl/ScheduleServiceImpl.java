package com.atguigu.servicehosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.servicehosp.repository.ScheduleRepository;
import com.atguigu.servicehosp.service.DepartmentService;
import com.atguigu.servicehosp.service.HospitalService;
import com.atguigu.servicehosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.BookingScheduleRuleVo;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-08 18:12
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

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

    /**
     * 根据医生编号，科室编号，分页查询排班信息
     * <p>
     * 题外话：这个排版信息的封装和科室信息封装不同之处在于：
     * 科室信息是以一种树形的形式进行封装，需要遍历出所有的科室，根据大科室号进行分组后，再依次遍历每个组里面具体的小科室，将小科室封装好存在集合中，最终设置到大科室的孩子集合中
     * 排版信息只需要根据日期进行分组后，将统计的信息一并封装为对象存入列表，最后返回即可
     *
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     * @return
     */
    @Override
    public Map<String, Object> getScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        //根据医院编号，科室编号查询对应数据，封装查询条件
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        //根据工作日workDate进行分组
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),//条件匹配
                Aggregation.group("workDate")//分组字段
                        .first("workDate").as("workDate")//设置分组后的别名
                        .count().as("docCount")//统计分组内的数量
                        .sum("reservedNumber").as("reservedNumber")//将分组内的该字段求和
                        .sum("availableNumber").as("availableNumber"),
                Aggregation.sort(Sort.Direction.ASC, "workDate"),//每一组升序排列，按照日期
                Aggregation.skip((page - 1) * limit),//分页条件
                Aggregation.limit(limit)
        );
        //获取分组查询后的总记录条数
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        //获取总记录数，第一个参数时聚合函数，第二个是查询的返回类型，第三个是需要封装的类型
        AggregationResults<BookingScheduleRuleVo> toalAggResult = mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        int total = toalAggResult.getMappedResults().size();

        AggregationResults<BookingScheduleRuleVo> results = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        //获取最终的集合
        List<BookingScheduleRuleVo> ruleVoList = results.getMappedResults();

        //取出每个实体类对应的日期，转换为对应的星期进行封装返回
        for (BookingScheduleRuleVo bookingScheduleRuleVo : ruleVoList) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String week = this.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(week);
        }

        //将列表数据和总条数进行返回
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", ruleVoList);
        resultMap.put("total", total);
        //这里使用hoscode来获取医院名称，将数据封装在一个集合中一并加入结果集合中（为了可用性）
        String hospName = hospitalService.getHospNameByHoscode(hoscode);
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hospName", hospName);
        resultMap.put("baseMap", baseMap);

        return resultMap;
    }

    /**
     * 根据医院编号、部分编号、排班日期获取所有记录
     *
     * @param hoscode
     * @param depcode
     * @param workDate
     * @return
     */
    @Override
    public List<Schedule> getScheduleDetails(String hoscode, String depcode, String workDate)  {
//        List<Schedule> scheduleList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDate());
        List<Schedule> scheduleList = null;
        try {
            scheduleList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode,new SimpleDateFormat("yyyy-MM-dd").parse(workDate) );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(Schedule schedule:scheduleList){
            this.packageSchedule(schedule);
        }
        return scheduleList;
    }

    /**
     * 封装每个Schedule里面的参数信息（医院名称，科室名称，日期对应的星期）
     * @param schedule
     */
    private void packageSchedule(Schedule schedule) {
        schedule.getParam().put("hoscode",hospitalService.getHospNameByHoscode(schedule.getHoscode()));
        schedule.getParam().put("depname",departmentService.getDeptmentName(schedule.getHoscode(),schedule.getDepcode()));
        schedule.getParam().put("dayOfWeek",this.getDayOfWeek(new DateTime(schedule.getWorkDate())));

    }

    /**
     * 根据日期获取周几数据
     *
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }

}
