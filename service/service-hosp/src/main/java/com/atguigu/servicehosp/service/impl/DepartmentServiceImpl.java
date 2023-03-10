package com.atguigu.servicehosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.servicehosp.repository.DepartmentRepository;
import com.atguigu.servicehosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Guanghao Wei
 * @create 2023-03-08 17:20
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 医院平台上传科室接口
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
     * 医院平台查询科室接口
     *
     * @param pageNum
     * @param pageSize
     * @param departmentQueryVo
     * @return
     */
    @Override
    public Page<Department> getPageDepartment(int pageNum, int pageSize, DepartmentQueryVo departmentQueryVo) {
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);
        Example<Department> example = Example.of(department, matcher);
        Pageable page = PageRequest.of(pageNum - 1, pageSize);
        Page<Department> departmentPage = departmentRepository.findAll(example, page);
        return departmentPage;
    }

    /**
     * 医院平台删除科室 根据医院编号和科室编号
     *
     * @param hoscode
     * @param depcode
     */
    @Override
    public void deleteDepartment(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            department.setIsDeleted(1);
            departmentRepository.save(department);
        }
    }

    /**
     * 根据医院id获取医院下属所有的科室信息
     * 将科室按照科室号划分，划分后，在封装每一个小科室进去，最后返回大科室
     *
     * @param hoscode
     * @return
     */
    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        List<DepartmentVo> result = new ArrayList<>();
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> example = Example.of(department);
        List<Department> departmentList = departmentRepository.findAll(example);

        //将该医院的所有科室信息按照科室号进行分类，用map集合存储，键为科室号，值为科室列表
        Map<String, List<Department>> departmentMap = departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));

        for(Map.Entry<String,List<Department>> entry:departmentMap.entrySet()){
            //遍历每一个Entry，每一个大科室封装为一个DepartmentVo对象
            String bigcode = entry.getKey();
            List<Department> departmentList1 = entry.getValue();
            DepartmentVo departmentVo=new DepartmentVo();
            departmentVo.setDepcode(bigcode);
            departmentVo.setDepname(departmentList1.get(0).getBigname());

            //遍历每个大科室下面的科室列表，将每一个小科室封装为DepartmentVo对象，并且封装在列表中
            List<DepartmentVo> childrenList=new ArrayList<>();
            for(Department dept:departmentList1){
                DepartmentVo child=new DepartmentVo();
                child.setDepcode(dept.getDepcode());
                child.setDepname(dept.getDepname());
                childrenList.add(child);
            }
            //将小科室列表封装到大科室对象的下属集合里面
            departmentVo.setChildren(childrenList);
            //遍历每个大科室，都做相同处理，每个大科室都封装在最后的集合里，最终遍历完成后进行返回
            result.add(departmentVo);
        }

        return result;
    }

    /**
     * 根据医院编号和部门编号返回部门名称
     * @param hoscode
     * @param depcode
     * @return
     */
    @Override
    public String getDeptmentName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department!=null){
            return department.getDepname();
        }
        return null;
    }


}
