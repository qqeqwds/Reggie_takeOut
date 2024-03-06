package com.heu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heu.domin.Dish;
import com.heu.domin.Setmeal;
import com.heu.domin.SetmealDish;
import com.heu.dto.DishDto;
import com.heu.dto.SetmealDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    @Transactional
    public boolean saveCombo(SetmealDto dto);

    public SetmealDto getBycId(Long cId);

    @Transactional
    public boolean updateCombo(SetmealDto dto);

    public boolean updateState(Integer status,List<Long>ids);

    @Transactional
    public boolean deleteCombo(List<Long> ids);

    @Transactional
    public List<SetmealDto> getByCategoryId(Long id,Integer status);

    public List<SetmealDish> getDishBySetmealId(Long Id);
}
