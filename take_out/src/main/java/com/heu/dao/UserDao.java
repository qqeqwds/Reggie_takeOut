package com.heu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heu.domin.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
