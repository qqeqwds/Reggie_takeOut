package com.heu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heu.domin.Dish;
import com.heu.dto.DishDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DishService extends IService<Dish> {

    @Transactional
    public boolean saveDish(DishDto dishDto);

    public Page<Dish> getAll(Integer page,Integer pageSize,String name);

    public boolean updateStatus(Integer Status, List<Long> ids);

    @Transactional
    public boolean deleteById(List<Long> ids);

    public DishDto getOne(Long id);

    @Transactional
    public boolean updateDish(DishDto dto);

    public List<DishDto> getByCategoryId(Dish dish);
}
