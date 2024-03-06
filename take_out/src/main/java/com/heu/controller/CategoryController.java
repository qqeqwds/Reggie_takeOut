package com.heu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heu.common.R;
import com.heu.domin.Category;
import com.heu.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> add(@RequestBody Category category){
        log.info("传入:{}",category);
        boolean save = categoryService.save(category);
        return save ? R.success("添加成功") : R.success("添加失败");
    }

    @GetMapping("/page")
    public R<Page> getByInfo(Integer page , Integer pageSize){
        log.info("拆线呢第{}页，页面有{}条数据",page,pageSize);
        Page categoryPage = categoryService.getByInfo(page, pageSize);
        return R.success(categoryPage);
    }

    @DeleteMapping
    public R<String> delete(Long id){
        log.info("即将删除菜品或套餐id:{}",id);
        boolean delete = categoryService.delete(id);
        return delete ? R.success("删除成功") : R.error("删除失败");

    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("即将删除菜品或套餐:{}",category.toString());
        boolean update = categoryService.updateById(category);
        return update ? R.success("修改成功") : R.error("该分类下包含商品,删除失败");
    }

    // 菜品管理--新建菜品数据回显
    @GetMapping("/list")
    public R<List<Category>> getByType(Integer type){
        List<Category> list = categoryService.getByType(type);
        return list != null ? R.success(list) : R.error("查询失败");
    }
}
