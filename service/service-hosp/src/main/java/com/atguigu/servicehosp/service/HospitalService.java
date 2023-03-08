package com.atguigu.servicehosp.service;

import com.atguigu.yygh.model.hosp.Hospital;

import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-08 14:24
 */
public interface HospitalService {
    void save(Map<String, Object> map);

    Hospital getByHoscode(String hoscode);
}
