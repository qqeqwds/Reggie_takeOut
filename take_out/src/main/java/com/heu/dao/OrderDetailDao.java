package com.heu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heu.domin.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailDao extends BaseMapper<OrderDetail> {
}
