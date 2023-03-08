package com.atguigu.servicehosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.servicehosp.repository.DepartmentRepository;
import com.atguigu.servicehosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-08 17:20
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 上传科室接口
     *
     * @param map
     */
    @Override
    public void save(Map<String, Object> map) {
        String jsonString = JSONObject.toJSONString(map);
        Department department = JSONObject.parseObject(jsonString, Department.class);
        String hoscode = department.getHoscode();
        String depcode = department.getDepcode();
        Department targetDepartment = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (targetDepartment != null) {
            department.setIsDeleted(0);
            department.setUpdateTime(new Date());
            departmentRepository.save(department);
        } else {
            department.setUpdateTime(new Date());
            department.setCreateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    /**
     * 查询科室接口
     *
     * @param pageNum
     * @param pageSize
     * @param departmentQueryVo
     * @return
     */
    @Override
    public Page<Department> getPageDepartment(int pageNum, int pageSize, DepartmentQueryVo departmentQueryVo) {
        ExampleMatcher matcher=ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        Department department=new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);
        Example<Department> example = Example.of(department,matcher);
        Pageable page = PageRequest.of(pageNum-1, pageSize);
        Page<Department> departmentPage = departmentRepository.findAll(example, page);
        return departmentPage;
    }

    /**
     * 删除科室 根据医院编号和科室编号
     * @param hoscode
     * @param depcode
     */
    @Override
    public void deleteDepartment(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department!=null){
            department.setIsDeleted(1);
            departmentRepository.save(department);
        }
    }
}
