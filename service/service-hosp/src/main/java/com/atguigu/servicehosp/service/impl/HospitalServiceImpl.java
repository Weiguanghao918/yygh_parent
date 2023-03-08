package com.atguigu.servicehosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.servicehosp.repository.HospitalRepository;
import com.atguigu.servicehosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-08 14:25
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    /**
     * 上传医院接口
     * @param map
     */
    @Override
    public void save(Map<String, Object> map) {
        //转换map集合为对象
        String mapString = JSONObject.toJSONString(map);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);
        //判断是否存在相同数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalIsExist = hospitalRepository.getHospitalByHoscode(hoscode);
        //如果存在，进行修改
        if(hospitalIsExist!=null){
            hospital.setStatus(hospitalIsExist.getStatus());
            hospital.setCreateTime(hospitalIsExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }



    }

    /**
     * 根据hoscode查询医院信息并进行返回
     * @param hoscode
     * @return
     */
    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }




}
