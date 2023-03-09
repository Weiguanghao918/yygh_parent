package com.atguigu.servicehosp.controller;

import com.atguigu.servicehosp.service.DepartmentService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Guanghao Wei
 * @create 2023-03-09 16:08
 */
@Api(tags = "科室信息控制器")
@RestController
@CrossOrigin
@RequestMapping("/admin/hosp/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @ApiOperation("根据医院编号，查询所有科室列表")
    @GetMapping("/getDeptList/{hoscode}")
    public Result getDeptList(@PathVariable("hoscode") String hoscode) {
        List<DepartmentVo> departmentList = departmentService.findDeptTree(hoscode);
        return Result.ok(departmentList);
    }

}
