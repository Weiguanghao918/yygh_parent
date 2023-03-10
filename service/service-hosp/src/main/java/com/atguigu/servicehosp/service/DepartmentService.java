package com.atguigu.servicehosp.service;

import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-08 17:20
 */
public interface DepartmentService {
    void save(Map<String, Object> map);

    Page<Department> getPageDepartment(int pageNum, int pageSize, DepartmentQueryVo departmentQueryVo);

    void deleteDepartment(String hoscode, String depcode);

    List<DepartmentVo> findDeptTree(String hoscode);

    String getDeptmentName(String hoscode, String depcode);
}
