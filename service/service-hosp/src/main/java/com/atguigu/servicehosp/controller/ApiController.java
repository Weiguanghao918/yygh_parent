package com.atguigu.servicehosp.controller;

import com.atguigu.servicehosp.service.DepartmentService;
import com.atguigu.servicehosp.service.HospitalService;
import com.atguigu.servicehosp.service.HospitalSetService;
import com.atguigu.servicehosp.service.ScheduleService;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-08 14:25
 */
@Api(tags = "医院管理API接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("上传医院接口")
    @PostMapping("/saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);

        //获取医院上传的签名
        String hospSign = (String) map.get("sign");
        //根据医院编号查询数据库的签名
        String hoscode = (String) map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        String logoData = (String) map.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        map.put("logoData", logoData);
        hospitalService.save(map);

        return Result.ok();

    }

    @ApiOperation("查询医院接口")
    @PostMapping("/hospital/show")
    public Result showHospital(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        //获取医院上传的签名
        String hospSign = (String) map.get("sign");
        //根据医院编号查询数据库的签名
        String hoscode = (String) map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    @ApiOperation("上传科室接口")
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String) map.get("hoscode");
        //获取医院上传的签名
        String hospSign = (String) map.get("sign");
        String signKey = hospitalSetService.getSignKey(hoscode);

        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.save(map);

        return Result.ok();
    }

    @ApiOperation("查询科室接口")
    @PostMapping("/department/list")
    public Result departmentList(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        //获取医院上传的签名
        String hospSign = (String) map.get("sign");
        //根据医院编号查询数据库的签名
        String hoscode = (String) map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        int pageNum = StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String) map.get("page"));
        int pageSize = StringUtils.isEmpty(map.get("limit")) ? 10 : Integer.parseInt((String) map.get("limit"));

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

        Page<Department> pageModel = departmentService.getPageDepartment(pageNum, pageSize, departmentQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("删除科室")
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        //获取医院上传的签名
        String hospSign = (String) map.get("sign");
        //根据医院编号查询数据库的签名
        String hoscode = (String) map.get("hoscode");
        String depcode = (String) map.get("depcode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.deleteDepartment(hoscode, depcode);
        return Result.ok();

    }

    @ApiOperation("上传排班接口")
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        //获取医院上传的签名
        String hospSign = (String) map.get("sign");
        //根据医院编号查询数据库的签名
        String hoscode = (String) map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        scheduleService.save(map);
        return Result.ok();
    }

    @ApiOperation("查询排班接口")
    @PostMapping("/schedule/list")
    public Result scheduleList(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        //获取医院上传的签名
        String hospSign = (String) map.get("sign");
        //根据医院编号查询数据库的签名
        String hoscode = (String) map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        int pageNum = StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String) map.get("page"));
        int pageSize = StringUtils.isEmpty(map.get("limit")) ? 10 : Integer.parseInt((String) map.get("limit"));

        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        scheduleOrderVo.setHoscode(hoscode);
        Page<Schedule> schedulePage = scheduleService.getSchedulePageList(pageNum, pageSize, scheduleOrderVo);
        return Result.ok(schedulePage);
    }

    @ApiOperation("删除排班接口")
    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        //获取医院上传的签名
        String hospSign = (String) map.get("sign");
        //根据医院编号查询数据库的签名
        String hoscode = (String) map.get("hoscode");
        String hosScheduleId = (String) map.get("hosScheduleId");
        String signKey = hospitalSetService.getSignKey(hoscode);

        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        scheduleService.deleteSchedule(hoscode,hosScheduleId);

        return Result.ok();
    }

}
