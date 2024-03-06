package com.heu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heu.common.R;
import com.heu.domin.Employee;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {

    R<Employee> login(HttpServletRequest request, Employee employee);

    R<String> logout(HttpServletRequest request);

    R<Page<Employee>> getAll(Integer page, Integer pageSize,String name);

    R<Employee> saveEmp(HttpServletRequest request,Employee employee);

    Employee updateEmp(HttpServletRequest request,Employee employee);

    Employee getById(Long id);
}
