package com.atguigu.servicehosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.servicehosp.client.DictFeignClient;
import com.atguigu.servicehosp.repository.HospitalRepository;
import com.atguigu.servicehosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-08 14:25
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private DictFeignClient dictFeignClient;

    /**
     * 医院平台上传医院接口
     *
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
        if (hospitalIsExist != null) {
            hospital.setStatus(hospitalIsExist.getStatus());
            hospital.setCreateTime(hospitalIsExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }


    }

    /**
     * 根据hoscode查询医院信息并进行返回
     *
     * @param hoscode
     * @return
     */
    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }

    @Override
    public Page<Hospital> getPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> hospitalPage = hospitalRepository.findAll(example, pageable);

        List<Hospital> content = hospitalPage.getContent();
        for (Hospital hosp : content) {
            this.setHospitalHostType(hosp);
        }

        return hospitalPage;
    }

    /**
     * 修改医院状态信息
     *
     * @param id
     * @param status
     */
    @Override
    public void updateHospStatus(String id, int status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);

    }

    /**
     * 根据医院id获取医院详情，封装数据以map形式返回
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> showHospitalDetail(String id) {
        Map<String, Object> hospMape = new HashMap<>();
        Hospital hospital = hospitalRepository.findById(id).get();
        hospMape.put("hospital", hospital);
        this.setHospitalHostType(hospital);
        hospMape.put("bookingRule", hospital.getBookingRule());
        hospital.setBookingRule(null);
        return hospMape;
    }

    /**
     * 根据医院编号返回医院名称
     *
     * @param hoscode
     * @return
     */
    @Override
    public String getHospNameByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospital != null) {
            return hospital.getHosname();
        }
        return null;
    }

    /**
     * 根据医院名称关键字搜索医院列表
     * @param hospName
     * @return
     */
    @Override
    public List<Hospital> findByHospName(String hospName) {
        List<Hospital> hospitalList =hospitalRepository.findHospitalByHosnameLike(hospName);
        return hospitalList;
    }

    /**
     * 根据hoscode来获取医院详情
     * @param hoscode
     * @return
     */
    @Override
    public Map<String, Object> showHospitalDetailByHoscode(String hoscode) {
        Map<String,Object> result=new HashMap<>();
        Hospital hospital = this.getByHoscode(hoscode);
        this.setHospitalHostType(hospital);
        result.put("hospital",hospital);
        result.put("bookingRule",hospital.getBookingRule());
        hospital.setBookingRule(null);
        return result;
    }

    /**
     * 根据省市区 医院等级编号远程调用Dict控制器方法获取对应的中文名称
     *
     * @param hospital
     * @return
     */
    private void setHospitalHostType(Hospital hospital) {
        String hospitalRankName = dictFeignClient.getName("Hostype", hospital.getHostype());

        String provinceName = dictFeignClient.getName(hospital.getProvinceCode());
        String districtName = dictFeignClient.getName(hospital.getDistrictCode());
        String cityName = dictFeignClient.getName(hospital.getCityCode());

        hospital.getParam().put("provinceName", provinceName);
        hospital.getParam().put("districtName", districtName);
        hospital.getParam().put("cityName", cityName);
        hospital.getParam().put("hospitalRankName", hospitalRankName);
        hospital.getParam().put("fullAddress", provinceName + cityName + districtName);

    }


}
