package com.heu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heu.common.R;
import com.heu.domin.Orders;
import com.heu.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    // 移动端
    @GetMapping("/userPage")
    public R<Page<Orders>> getAll(Integer page,Integer pageSize){
        Page<Orders> ordersPage = orderService.getAll(page, pageSize);
        return R.success(ordersPage);
    }

    // 后台
    @GetMapping("/page")
    public R<Page<Orders>> getAll1(Integer page,Integer pageSize){
        Page<Orders> ordersPage = orderService.getAll(page, pageSize);
        return R.success(ordersPage);
    }
}
