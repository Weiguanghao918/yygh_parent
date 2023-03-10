package com.atguigu.servicecmn.controller;

import com.atguigu.servicecmn.service.DictService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Guanghao Wei
 * @create 2023-03-07 15:50
 */
@Api(tags = {"数据字典控制器"})
@RestController
//@CrossOrigin
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    @ApiOperation("根据id查询子节点数据列表")
    @GetMapping("/findByParentId/{id}")
    public Result findByParentId(@PathVariable("id") Long id) {
        List<Dict> list = dictService.findByParentId(id);
        return Result.ok(list);
    }

    @ApiOperation("导出数据字典")
    @GetMapping("/exportData")
    public Result exportData(HttpServletResponse response){
        dictService.exportData(response);
        return Result.ok();
    }

    @ApiOperation("导入数据字典")
    @PostMapping("/importData")
    public Result importData(MultipartFile file){
        dictService.importData(file);
        return Result.ok();
    }

    @ApiOperation("根据dictcode和value值查询数据字典名称")
    @GetMapping("/getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode")String dictCord,
                          @PathVariable("value")String value){
        return dictService.getDictName(dictCord,value);
    }

    @ApiOperation("根据value值查询数据字典名称")
    @GetMapping("/getName/{value}")
    public String getName(@PathVariable("value")String value){
        return dictService.getDictName("",value);
    }

    @ApiOperation("根据dict_code获取下属节点信息")
    @GetMapping("/findByDictCode/{dictCode}")
    public  Result findByDictCode(@PathVariable("dictCode")String dictCord){
        List<Dict> dictList=dictService.findByDictCode(dictCord);
        return Result.ok(dictList);
    }

}
