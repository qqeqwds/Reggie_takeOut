package com.heu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heu.domin.OrderDetail;
import com.heu.domin.Orders;
import org.springframework.beans.factory.annotation.Autowired;

public interface OrderService extends IService<Orders> {

    public boolean submit(Orders orders);

    public Page<Orders> getAll(Integer page, Integer pageSize);

}
