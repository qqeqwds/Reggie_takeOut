package com.heu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heu.common.R;
import com.heu.domin.Employee;
import com.heu.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        R<Employee> login = employeeService.login(request, employee);
        return login;
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        R<String> logout = employeeService.logout(request);
        return logout;
    }

    @GetMapping("/page")
    public R<Page<Employee>> getAll(Integer page, Integer pageSize,String name){
        log.info("获取分页条件{},{},{}",page,pageSize,name);
        R<Page<Employee>> pageInfo = employeeService.getAll(page, pageSize, name);
        return pageInfo;
    }

    @PostMapping
    public R<Employee> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工：{}",employee);
        R<Employee> emp = employeeService.saveEmp(request, employee);
        return emp;
    }

    @PutMapping
    public R<Employee> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        Employee emp = employeeService.updateEmp(request,employee);
        return R.success(emp);
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("获取修改对象id：{}",id);
        Employee emp = employeeService.getById(id);
        return R.success(emp);
    }
}
