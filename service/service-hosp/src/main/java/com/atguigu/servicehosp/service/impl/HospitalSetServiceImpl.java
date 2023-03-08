package com.atguigu.servicehosp.service.impl;

import com.atguigu.servicehosp.mapper.HospitalSetMapper;
import com.atguigu.servicehosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Guanghao Wei
 * @create 2023-03-06 16:00
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    @Autowired
    private HospitalSetMapper hospitalSetMapper;

    /**
     * 分页查询医院
     *
     * @param page1
     * @param hospitalSetQueryVo
     * @return
     */
    @Override
    public IPage<HospitalSet> selectPage(Page<HospitalSet> page1, HospitalSetQueryVo hospitalSetQueryVo) {
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(hospitalSetQueryVo.getHoscode())) {
            queryWrapper.eq("hoscode", hospitalSetQueryVo.getHoscode());
        }
        if (!StringUtils.isEmpty(hospitalSetQueryVo.getHosname())) {
            queryWrapper.like("hosname", hospitalSetQueryVo.getHosname());
        }
        IPage<HospitalSet> iPage = hospitalSetMapper.selectPage(page1, queryWrapper);
        return iPage;
    }

    /**
     * 根据医院hoscode查询signKey
     *
     * @param hoscode
     * @return
     */
    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hoscode", hoscode);
        HospitalSet hospitalSet = hospitalSetMapper.selectOne(queryWrapper);
        return hospitalSet.getSignKey();
    }
}
