package com.atguigu.servicehosp.repository;

import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Guanghao Wei
 * @create 2023-03-08 14:24
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {

    Hospital getHospitalByHoscode(String hoscode);
}
