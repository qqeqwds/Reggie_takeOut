package com.heu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heu.domin.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeDao extends BaseMapper<Employee> {
}
