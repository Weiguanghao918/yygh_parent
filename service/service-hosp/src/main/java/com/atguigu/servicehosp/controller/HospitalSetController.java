package com.atguigu.servicehosp.controller;

import com.atguigu.servicehosp.service.HospitalSetService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Guanghao Wei
 * @create 2023-03-06 16:01
 */
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
//@CrossOrigin
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    //查询医院设置表的所有信息
    @ApiOperation("查询医院设置表得所有信息")
    @GetMapping("/getAll")
    public Result getAll() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    //逻辑删除医院
    @ApiOperation("逻辑删除医院")
    @DeleteMapping("/deleteHospitalById/{id}")
    public Result deleteHospitalById(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //多条件分页查询
    @ApiOperation("多条件分页查询医院")
    @PostMapping("/findPageCondition/{page}/{limit}")
    public Result findPageCondition(@PathVariable long page,
                                    @PathVariable long limit,
                                    @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        Page<HospitalSet> page1 = new Page<>(page, limit);
        IPage<HospitalSet> iPage = hospitalSetService.selectPage(page1, hospitalSetQueryVo);
        Map<String,Object> map=new HashMap<>();
        map.put("records",iPage.getRecords());
        map.put("total",iPage.getTotal());
        return Result.ok(map);
    }

    //添加医院设置
    @ApiOperation("添加医院设置")
    @PostMapping("addHospitalSet")
    public Result addHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //设置状态： 1使用 ， 0不能使用
        hospitalSet.setStatus(1);
        //设置签名秘钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));

        boolean flag = hospitalSetService.save(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //根据id获取医院设置
    @ApiOperation("根据id获取医院设置")
    @GetMapping("getHospitalSetById/{id}")
    public Result getHospitalSetById(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //修改医院设置
    @ApiOperation("修改医院设置")
    @PostMapping("/updateHospital")
    public Result updateHospital(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除医院设置
    @ApiOperation("多逻辑删除医院设置（批量）")
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Long> Ids) {
        boolean flag = hospitalSetService.removeByIds(Ids);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 修改医院状态
     *
     * @param id
     * @param status
     * @return
     */
    @ApiOperation("修改医院状态")
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable("id") Long id,
                                  @PathVariable("status") Integer status) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 发送签名密钥
     *
     * @param id
     * @return
     */
    @ApiOperation("发送签名密钥")
    @PutMapping("/sendKey/{id}")
    public Result sendKey(@PathVariable("id") Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String hoscode = hospitalSet.getHoscode();
        String signKey = hospitalSet.getSignKey();
        return Result.ok();
    }

}
