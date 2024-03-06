package com.heu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heu.domin.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDao extends BaseMapper<Orders> {

}
