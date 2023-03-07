package com.atguigu.servicecmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.servicecmn.listener.DictListener;
import com.atguigu.servicecmn.mapper.DictMapper;
import com.atguigu.servicecmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guanghao Wei
 * @create 2023-03-07 15:49
 */
@Service
public class DicetServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private DictMapper dictMapper;

    /**
     * 根据id判断每个子节点是否还有子节点，并且给属性赋值
     *
     * @param id
     * @return
     */
    @Cacheable(value = "dict")
    @Override
    public List<Dict> findByParentId(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        List<Dict> dictList = dictMapper.selectList(queryWrapper);
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            boolean hasChildren = this.hasChildren(dictId);
            dict.setHasChildren(hasChildren);
        }
        return dictList;
    }

    /**
     * 导出数据字典
     *
     * @param response
     */
    @Override
    public void exportData(HttpServletResponse response) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.ms-excel");
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            List<Dict> dictList = dictMapper.selectList(null);
            List<DictEeVo> dictEeVoList = new ArrayList<>();
            for(Dict dict:dictList){
                DictEeVo dictEeVo=new DictEeVo();
                BeanUtils.copyProperties(dict,dictEeVo,DictEeVo.class);
                dictEeVoList.add(dictEeVo);
            }

            EasyExcel.write(response.getOutputStream(),DictEeVo.class).sheet("数据字典").doWrite(dictEeVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @CacheEvict(value = "dict",allEntries = true)
    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(dictMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasChildren(Long dictId) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", dictId);
        Integer count = Math.toIntExact(dictMapper.selectCount(queryWrapper));
        return count > 0;
    }
}
