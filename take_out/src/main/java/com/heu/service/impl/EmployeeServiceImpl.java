package com.heu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heu.common.R;
import com.heu.dao.EmployeeDao;
import com.heu.domin.Employee;
import com.heu.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements EmployeeService{

    @Autowired
    private EmployeeDao employeeDao;

    // 登录
    @Override
    public R<Employee> login(HttpServletRequest request, Employee employee) {
        // 读取登录信息
        String username = employee.getUsername();
        // 将登陆密码加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 创建查询条件
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,username);
        Employee emp = employeeDao.selectOne(lqw);

        // 判断用户是否存在
        if(emp == null){
            return R.error("查无此人,登陆失败");
        }
        // 判断登录信息中的密码是否匹配
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误,登陆失败");
        }
        // 判断用户帐号状态
        if(emp.getStatus() == 0){
            return R.error("用户账号被冻结,无法登录");
        }
        // 三项检查完将登录用户的id存储进session域中
        request.getSession().setAttribute("emp",emp.getId());
        return R.success(emp);
    }

    // 退登
    @Override
    public R<String> logout(HttpServletRequest request){
        // 移除session域中的id
        request.getSession().removeAttribute("emp");
        return R.success("退登成功");
    }

    // 查询
    @Override
    public R<Page<Employee>> getAll(Integer page, Integer pageSize,String name) {
        // 构建分页器
        Page<Employee> employeePage = new Page<>(page,pageSize);
        // 创建分页条件
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotEmpty(name),Employee::getUsername,name)
           .orderByDesc(Employee::getUpdateTime);
        // 分页查询
        employeeDao.selectPage(employeePage,lqw);
        return R.success(employeePage);
    }

    // 添加员工
    @Override
    public R<Employee> saveEmp(HttpServletRequest request,Employee employee) {
        // 对新增对象再次加工
        LocalDateTime time = LocalDateTime.now();
        // 添加新增、修改的时间和id
        /*employee.setCreateTime(time);
        employee.setCreateUser((Long) request.getSession().getAttribute("emp"));
        employee.setUpdateTime(time);
        employee.setUpdateUser((Long) request.getSession().getAttribute("emp"));*/
        // 设置初始对密码并进行加密
        String password = DigestUtils.md5DigestAsHex(DigestUtils.md5Digest("123456".getBytes()));
        employee.setPassword(password);
        employeeDao.insert(employee);
        return R.success(employee);
    }

    // 修改员工信息
    @Override
    public Employee updateEmp(HttpServletRequest request,Employee employee) {
        // 根据id修改状态
        /*employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((Long) request.getSession().getAttribute("emp"));*/
        employeeDao.updateById(employee);
        return employee;
    }

    // 数据回显
    @Override
    public Employee getById(Long id) {
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getId,id);
        Employee employee = employeeDao.selectOne(lqw);
        return employee;
    }

}
