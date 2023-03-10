package com.atguigu.servicehosp.controller;

import com.atguigu.servicehosp.service.DepartmentService;
import com.atguigu.servicehosp.service.HospitalService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-10 11:59
 */
@Api(tags = "前端系统医院管理接口")
@RequestMapping("/api/hosp/hospital")
@RestController
public class HospitalApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation("获取医院分页列表")
    @GetMapping("/getHospListPage/{page}/{limit}")
    public Result index(@PathVariable("page") Integer page,
                        @PathVariable("limit") Integer limit,
                        HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitalPage = hospitalService.getPage(page, limit, hospitalQueryVo);
        return Result.ok(hospitalPage);
    }

    @ApiOperation("根据医院名称关键字搜索医院列表")
    @GetMapping("/findByHospName/{hospName}")
    public Result findbyHospName(@PathVariable("hospName") String hospName) {
        List<Hospital> hospitalList = hospitalService.findByHospName(hospName);
        return Result.ok(hospitalList);
    }

    @ApiOperation("获取医院科室信息")
    @GetMapping("/department/{hoscode}")
    public Result getDepartmentInfoByHoscode(@PathVariable("hoscode") String hoscode) {
        return Result.ok(departmentService.findDeptTree(hoscode));
    }

    @ApiOperation("根据医院编号获取医院详情，封装数据以map形式返回")
    @GetMapping("/showHospitalDetailByHoscode/{hoscode}")
    public Result showHospitalDetail(@PathVariable("hoscode")String hoscode){
        Map<String, Object> map=hospitalService.showHospitalDetailByHoscode(hoscode);
        return Result.ok(map);
    }
}
