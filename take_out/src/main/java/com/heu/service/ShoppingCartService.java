package com.heu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heu.domin.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {

    public List<ShoppingCart> getAll();

    public boolean add(ShoppingCart shoppingCart);

    public boolean updateNumber(ShoppingCart shoppingCart);

    public boolean deleteAll();
}
