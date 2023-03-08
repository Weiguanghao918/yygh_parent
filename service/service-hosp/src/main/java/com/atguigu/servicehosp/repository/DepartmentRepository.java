package com.atguigu.servicehosp.repository;

import com.atguigu.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Guanghao Wei
 * @create 2023-03-08 17:19
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {
    /**
     * 根据医院编号和科室编号查询科室
     * @param hoscode
     * @param depcode
     * @return
     */
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
