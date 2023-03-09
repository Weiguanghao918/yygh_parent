package com.atguigu.servicehosp.controller;

import com.atguigu.servicehosp.service.HospitalService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-09 13:14
 */
@Api(tags = "医院管理控制器")
@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @ApiOperation("多条件分页展示医院数据")
    @PostMapping("/pageList/{page}/{limit}")
    public Result getPageList(@PathVariable("page") Integer page,
                              @PathVariable("limit") Integer limit,
                              @RequestBody(required = false) HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitalPage = hospitalService.getPage(page, limit, hospitalQueryVo);
        return Result.ok(hospitalPage);
    }

    @ApiOperation("改变医院状态信息")
    @GetMapping("/updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable("id") String id,
                                   @PathVariable("status") int status) {
        hospitalService.updateHospStatus(id, status);
        return Result.ok();

    }

    @ApiOperation("根据医院id获取医院详细信息")
    @GetMapping("/showHospDetail/{id}")
    public Result showHospitalDetail(@PathVariable("id") String id) {
        Map<String, Object> map = hospitalService.showHospitalDetail(id);
        return Result.ok(map);
    }

}
