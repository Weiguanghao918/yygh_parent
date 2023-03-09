package com.atguigu.servicehosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-08 14:24
 */
public interface HospitalService {
    void save(Map<String, Object> map);

    Hospital getByHoscode(String hoscode);

    Page<Hospital> getPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateHospStatus(String id, int status);

    Map<String, Object> showHospitalDetail(String id);

    String getHospNameByHoscode(String hoscode);
}
