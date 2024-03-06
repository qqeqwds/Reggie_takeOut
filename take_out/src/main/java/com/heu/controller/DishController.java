package com.heu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heu.common.R;
import com.heu.domin.Dish;
import com.heu.domin.DishFlavor;
import com.heu.dto.DishDto;
import com.heu.service.DishFlavorService;
import com.heu.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dto){
        boolean result = dishService.saveDish(dto);
        return result ? R.success("操作成功") : R.success("操作失败");
    }

    @GetMapping("/page")
    public R<Page<Dish>> getAll(Integer page, Integer pageSize, String name){
        Page<Dish> all = dishService.getAll(page, pageSize, name);
        return all.getRecords() != null ? R.success(all) : R.error("查询失败");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status,@RequestParam List<Long> ids){
        boolean result = dishService.updateStatus(status, ids);
        return result ? R.success("修改成功") : R.error("修改失败");
    }

    @DeleteMapping
    public R<String> deleteById(@RequestParam List<Long> ids){
        boolean result = dishService.deleteById(ids);
        return result ? R.success("删除成功") : R.error("在售商品无法被删除");
    }

    //修改菜品时完成数据回显
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto dto = dishService.getOne(id);
        return dto == null ? R.error("未知错误") : R.success(dto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dto){
        boolean result = dishService.updateDish(dto);
        return result ? R.success("修改成功") : R.error("出现未知错误");
    }

    @GetMapping("/list")
    public R<List<DishDto>> getByCategory(Dish dish) {
        List<DishDto> dtos = dishService.getByCategoryId(dish);
        return dtos != null ? R.success(dtos) : R.error("出现未知错误");
    }
}
