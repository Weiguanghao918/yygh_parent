package com.atguigu.servicehosp.service;

import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Guanghao Wei
 * @create 2023-03-06 16:00
 */
public interface HospitalSetService extends IService<HospitalSet> {
    IPage<HospitalSet> selectPage(Page<HospitalSet> page1, HospitalSetQueryVo hospitalSetQueryVo);

    String getSignKey(String hoscode);
}
