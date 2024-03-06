package com.heu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heu.domin.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishDao extends BaseMapper<Dish> {
}
