package com.heu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heu.dao.SetmealDao;
import com.heu.domin.Dish;
import com.heu.domin.Setmeal;
import com.heu.domin.SetmealDish;
import com.heu.dto.DishDto;
import com.heu.dto.SetmealDto;
import com.heu.service.DishService;
import com.heu.service.SetmeaDishlService;
import com.heu.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealDao, Setmeal> implements SetmealService {

    @Autowired
    private SetmeaDishlService setmeaDishlService;

    @Autowired
    private DishService dishService;

    @Override
    public boolean saveCombo(SetmealDto dto) {
        // 保存套餐
        boolean res1 = this.save(dto);
        // 提取套餐中绑定的菜品
        List<SetmealDish> dishes = dto.getSetmealDishes().stream().map(setmealDish -> {
            setmealDish.setSetmealId(dto.getId());
            return setmealDish;
        }).collect(Collectors.toList());
        boolean res2 = setmeaDishlService.saveBatch(dishes);
        return res1 & res2;
    }

    // 修改套餐完成数据回显
    @Override
    public SetmealDto getBycId(Long cId) {
        SetmealDto dto = new SetmealDto();
        Setmeal setmeal = this.getById(cId);
        // 属性copy
        BeanUtils.copyProperties(setmeal,dto);

        // 根据cId查询套餐中关联的菜品
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId,cId);
        List<SetmealDish> dishes = setmeaDishlService.list(lqw);
        dto.setSetmealDishes(dishes);
        return dto;
    }

    @Override
    public boolean updateCombo(SetmealDto dto) {
        // 修改套餐
        boolean res1 = this.updateById(dto);

        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId,dto.getId());
        // 删除套餐关联的旧菜品
        boolean res2 = setmeaDishlService.remove(lqw);

        // 添加新菜品
        List<SetmealDish> dishes = dto.getSetmealDishes();
        dishes = dishes.stream().map(setmealDish -> {
            setmealDish.setSetmealId(dto.getId());
            return setmealDish;
        }).collect(Collectors.toList());
        boolean res3 = setmeaDishlService.saveBatch(dishes);
        return res1 && res2 && res3;
    }

    @Override
    public boolean updateState(Integer status,List<Long> ids) {
        List<Setmeal> setmeals = this.listByIds(ids).stream().map(setmeal -> {
            setmeal.setStatus(status);
            return setmeal;
        }).collect(Collectors.toList());

        boolean result = this.updateBatchById(setmeals);
        return result;
    }

    @Override
    public boolean deleteCombo(List<Long> ids) {
        // 判断选中的套餐中是否正包含正在销售的套餐
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId,ids).eq(Setmeal::getStatus,1);
        List<Setmeal> setmeals = this.list(lqw);

        if(setmeals != null && setmeals.size() > 0){
            // 包含正在销售的商品，禁止删除
            return false;
        }
        // 不包含正在销售的产品允许删除
        // 删除套餐
        boolean result1 = this.removeByIds(ids);
        // 删除套餐中关联的菜品
        LambdaQueryWrapper<SetmealDish> sLqw = new LambdaQueryWrapper<>();
        sLqw.in(SetmealDish::getSetmealId,ids);
        boolean result2 = setmeaDishlService.remove(sLqw);
        return result1 && result2;
    }

    @Override
    public List<SetmealDto> getByCategoryId(Long id,Integer status) {
        // 根据套餐分类查询套餐
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Setmeal::getCategoryId,id).eq(Setmeal::getStatus,status);
        List<Setmeal> combos = this.list(lqw);
        List<SetmealDto> dtos = combos.stream().map(combo -> {
            SetmealDto dto = new SetmealDto();
            // 拷贝属性
            BeanUtils.copyProperties(combo, dto);
            // 关联套餐内的菜品
            LambdaQueryWrapper<SetmealDish> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(SetmealDish::getSetmealId, combo.getId());
            List<SetmealDish> dishes = setmeaDishlService.list(lqw1);
            dto.setSetmealDishes(dishes);
            return dto;
        }).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public List<SetmealDish> getDishBySetmealId(Long Id) {
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId,Id);
        List<SetmealDish> dishes = setmeaDishlService.list(lqw);
        dishes = dishes.stream().map(setmealDish ->{
            LambdaQueryWrapper<Dish> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(Dish::getId,setmealDish.getDishId());
            Dish one = dishService.getOne(lqw1);
            String image = one.getImage();
            setmealDish.setImage(image);
            return setmealDish;
        }).collect(Collectors.toList());
        return dishes;
    }


}
