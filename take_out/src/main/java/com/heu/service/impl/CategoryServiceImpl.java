package com.heu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heu.common.R;
import com.heu.dao.CategoryDao;
import com.heu.domin.Category;
import com.heu.domin.Dish;
import com.heu.service.CategoryService;
import com.heu.service.DishService;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao,Category> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private DishService dishService;

    @Override
    public Page getByInfo(Integer page , Integer pageSize){
        Page<Category> categoryPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Category::getSort);
        categoryDao.selectPage(categoryPage,lqw);
        return categoryPage;
    }

    @Override
    public List<Category> getByType(Integer type) {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(type != null,Category::getType,type);
        List<Category> list = categoryDao.selectList(lqw);
        return list;
    }

    @Override
    public boolean delete(Long id) {
        // 检查该分类id下是否关联着菜品
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Dish::getCategoryId,id);
        List<Dish> list = dishService.list(lqw);
        if(list != null && list.size() > 0){
            // 该分类下包含商品
            return false;
        }
        // 该分类中不包含商品
        boolean result = this.removeById(id);
        return result;
    }


}
