package com.heu.controller;

import com.heu.common.R;
import com.heu.domin.ShoppingCart;
import com.heu.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService service;

    @GetMapping("/list")
    public R<List<ShoppingCart>> getAll(){
        List<ShoppingCart> list = service.getAll();
        return list != null ? R.success(list) : R.error("查询失败");
    }

    @PostMapping("/add")
    public R<String> save(@RequestBody ShoppingCart shoppingCart){
        boolean res = service.add(shoppingCart);
        return res ? R.success("添加成功") : R.error("添加失败");
    }

    @PostMapping("/sub")
    public R<String> udpate(@RequestBody ShoppingCart shoppingCart){
        boolean res = service.updateNumber(shoppingCart);
        return res ? R.success("修改成功") : R.error("修改失败");
    }

    @DeleteMapping("/clean")
    public R<String> deleteAll(){
        boolean res = service.deleteAll();
        return res ? R.success("清空成功") : R.error("清空失败");
    }
}
