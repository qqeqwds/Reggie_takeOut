package com.heu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heu.common.R;
import com.heu.domin.Setmeal;
import com.heu.domin.SetmealDish;
import com.heu.dto.DishDto;
import com.heu.dto.SetmealDto;
import com.heu.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "套餐相关接口")
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService service;

    @ApiOperation("添加套餐接口")
    @PostMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dto",value = "套餐与菜品的关系类",required = true)
    })
    public R<String> save(@RequestBody SetmealDto dto){
        boolean result = service.saveCombo(dto);
        return result ? R.success("添加成功过") : R.error("出现未知错误");
    }

    @ApiOperation("根据条件展示套餐接口")
    @ApiImplicitParams({
            //     参数名       参数的作用     参数是否是必须有的
            @ApiImplicitParam(name = "page", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面数据大小", required = true),
            @ApiImplicitParam(name = "name", value = "菜品名称", required = false)
    }
    )
    @GetMapping("/page")
    public R<Page<Setmeal>> getAll(Integer page,Integer pageSize,String name){
        Page<Setmeal> list = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(name != null,Setmeal::getName,name).orderByDesc(Setmeal::getUpdateTime);
        service.page(list,lqw);
        return list.getRecords() != null ? R.success(list) : R.error("查询失败");
    }

    @ApiOperation("套餐数据回显接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "需要数据回显的id",required = true)
    })
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        SetmealDto dto = service.getBycId(id);
        return dto != null ? R.success(dto) : R.error("发生未知错误");
    }

    @ApiOperation("修改套餐信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "setmealDto",value = "套餐与菜品的关系类",required = true)
    })
    @CacheEvict(value = "setmealCache",key = "#categoryId + '_' + #status")
    @PutMapping
    public R<String> updateCombo(@RequestBody SetmealDto setmealDto){
        boolean result = service.updateCombo(setmealDto);
        return result ? R.success("修改成功") : R.error("修改失败");
    }

    @ApiOperation("批量修改套餐状态接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status",value = "要修改成的菜品状态",required = true),
            @ApiImplicitParam(name = "ids",value = "要修改状态的套餐id",required = true)
    })
    @CacheEvict(value = "setmealCache",allEntries = true)
    @PostMapping("/status/{status}")
    public R<String> updateState(@PathVariable Integer status,@RequestParam List<Long> ids){
        boolean result = service.updateState(status, ids);
        return result ? R.success("修改成功") : R.error("修改失败");
    }

    @ApiOperation("(批量)删除套餐")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids" , value = "要删除的菜品id",required = true)
    })
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        boolean result = service.deleteCombo(ids);
        return result ? R.success("删除成功") : R.error("在售套餐无法删除");
    }

    @ApiOperation("移动端页面根据套餐分类展示套餐接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId",value = "套餐分类id",required = true),
            @ApiImplicitParam(name = "status",value = "在售套餐状态",required = true)
    })
    @Cacheable(value = "setmealCache",key = "#categoryId + '_' + #status" , unless = "#dtos != null")
    @GetMapping("/list")
    public R<List<SetmealDto>> getByCategoryId(Long categoryId,Integer status){
        List<SetmealDto> dtos = service.getByCategoryId(categoryId, status);
        return dtos != null ? R.success(dtos) : R.error("查询失败");
    }

    @ApiOperation("套餐内菜品口味展示接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "套餐id",required = true)
    })
    @GetMapping("/dish/{id}")
    public R<List<SetmealDish>> getDishBySetmealId(@PathVariable Long id){
        List<SetmealDish> dishes = service.getDishBySetmealId(id);
        return dishes != null ? R.success(dishes) : R.error("查询失败");
    }
}
