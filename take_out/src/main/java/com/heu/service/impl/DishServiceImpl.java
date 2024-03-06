package com.heu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heu.dao.DishDao;
import com.heu.domin.Dish;
import com.heu.domin.DishFlavor;
import com.heu.dto.DishDto;
import com.heu.exception.BaseException;
import com.heu.exception.CustomException;
import com.heu.service.DishFlavorService;
import com.heu.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DishService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DishFlavorService dishFlavorService;


    @Override
    public boolean saveDish(DishDto dto) {
        // 存储菜品
        // mp会自动生成id然后保存到数据库中
        boolean res2 = this.save(dto);

        // 获取口味集合
        List<DishFlavor> flavors = dto.getFlavors();
        flavors = flavors.stream().map(flavor -> {
            // 对每个口味添加对应的菜品id
            flavor.setDishId(dto.getId());
            return flavor;
        }).collect(Collectors.toList());
        // 储存口味
        boolean res1 = dishFlavorService.saveBatch(flavors);

        // 存入了新的菜品，为了保证数据的一致性，将添加到某个分类中的菜品缓存数据进行清理
        String key = "dish" + dto.getCategoryId() + 1;
        redisTemplate.delete(key);
        log.info("redis保证了菜品分类数据{}的一致性",dto.getCategoryId());
        return res1 && res2;
    }

    @Override
    public Page<Dish> getAll(Integer page, Integer pageSize, String name) {
        // 创建分页构造器
        Page<Dish> list = new Page<>(page,pageSize);
        // 创建条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.like(name != null,Dish::getName,name)
           .orderByDesc(Dish::getSort);
        // 根据条件查询数据并进行分页
        this.page(list,lqw);
        return list;
    }

    @Override
    public boolean updateStatus(Integer status, List<Long> ids) {
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.in(Dish::getId,ids);
        List<Dish> dishes = this.list(lqw);
        dishes = dishes.stream().map(dish -> {
            dish.setStatus(status);
            return dish;
        }).collect(Collectors.toList());
        boolean result = this.updateBatchById(dishes);

        Set<String> keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        log.info("redis保证了菜品分类数据的一致性");

        return result;
    }

    @Override
    public boolean deleteById(List<Long> ids) {
        // 判断商品们是否正在销售
        LambdaQueryWrapper<Dish> dLqw = new LambdaQueryWrapper<>();
        dLqw.eq(Dish::getStatus,1).in(Dish::getId,ids);
        List<Dish> dishes = this.list(dLqw);
        if(dishes.size() != 0){
            // 被选中的菜品中包含正在销售的菜品
            return false;
        }

        // 被选中的菜品中不包含正在销售的菜品
        // 删除菜品
        boolean result1 = this.removeByIds(ids);
        // 删除菜品对应的口味
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(lqw);

        return result1 ;
    }
    //修改菜品时完成数据回显
    @Override
    public DishDto getOne(Long id) {
        DishDto dto = new DishDto();
        Dish dish = this.getById(id);
        BeanUtils.copyProperties(dish,dto);
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dishFlavorService.list(lqw);
        dto.setFlavors(flavors);
        return dto;
    }

    // 修改菜品
    @Override
    public boolean updateDish(DishDto dto) {
        // 保存修改后的菜品
        boolean result1 = this.updateById(dto);
        // 创建条件构造器
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dto.getId());
        // 根据菜品的id删除老口味
        boolean result2 = dishFlavorService.remove(lqw);
        // 保存新口味
        boolean result3 = dishFlavorService.saveBatch(
                dto.getFlavors().stream().map(flavor -> {
                    flavor.setDishId(dto.getId());
                    return flavor;
                }).collect(Collectors.toList())
        );
        Set<String> keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        log.info("redis保证了菜品分类数据的一致性");
        return result1 && result2 && result3;
    }

    @Override
    public List<DishDto> getByCategoryId(Dish dish) {
        List<DishDto> dtos = null;
        Long cId = dish.getCategoryId();
        Integer status = dish.getStatus();
        // 动态构造key
        String key = "dish_" + cId + "_" + status; // dish_1397844391040167938_1
        // 先从redis中获取缓存数据
        // 根据key从redis获取到json对象
        String json =  redisTemplate.opsForValue().get(key);
        if(json != null){
            try {
                // 利用jackson将json反序列化成对象
                log.info("从redis中查询{}是否存在value",key);
                dtos = objectMapper.readValue(json,List.class);
            } catch (JsonProcessingException e) {
                throw new BaseException("redis查询出错(json反序列化出现错误)");
            }
        }

        if(dtos != null){
            // 如果从缓存中读取到内容则直接返回即可，无需从数据库中调取数据
            log.info("成功使用redis中key：{}的value",key);
            return dtos;
        }
        // 在redis中没有查询到数据，需要调数据库进行查询
        // 创建条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        // 添加查询条件
        lqw.eq(cId != null,Dish::getCategoryId,cId).eq(Dish::getStatus,1)
           .orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        // 查询出菜品信息
        List<Dish> dishes = this.list(lqw);
        // 创建dto
        dtos  = dishes.stream().map(dish1 -> {
            DishDto dto = new DishDto();
            BeanUtils.copyProperties(dish1, dto);

            // 根据菜品I查询口味并添加
            LambdaQueryWrapper<DishFlavor> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(DishFlavor::getDishId,dish1.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(lqw1);
            dto.setFlavors(dishFlavors);
            return dto;
            // 将dishes和flavors封装在dto中放入集合内
        }).collect(Collectors.toList());
        // 将查询的内容存入redis中
        try {
            json = objectMapper.writeValueAsString(dtos);
        } catch (JsonProcessingException e) {
            throw new BaseException("对象json序列化出现了错误");
        }
        // 将查询的内容序列化成json字符串存入redis数据库中
        log.info("将查询的结果存入redis的key:{}中",key);
        redisTemplate.opsForValue().set(key,json,60, TimeUnit.MINUTES);

        // 返回集合
        return dtos;
    }


}
