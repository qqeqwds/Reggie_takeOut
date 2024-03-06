package com.heu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heu.common.BaseContext;
import com.heu.dao.ShoppingCartDao;
import com.heu.domin.ShoppingCart;
import com.heu.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements ShoppingCartService {


    @Override
    public List<ShoppingCart> getAll() {
        // 获取用户ID
        Long id = BaseContext.getCurrentId();
        // 根据用户id查询购物车商品
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,id);
        List<ShoppingCart> shoppingCarts = this.list(lqw);
        return shoppingCarts;
    }

    @Override
    public boolean add(ShoppingCart shoppingCart) {
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(shoppingCart.getDishId() != null,ShoppingCart::getDishId,shoppingCart.getDishId())
           .eq(shoppingCart.getSetmealId() != null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());

        ShoppingCart one = this.getOne(lqw);

        boolean res = false;

        if(one != null){
            one.setNumber(one.getNumber() + 1);
            res = this.updateById(one);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            res = this.save(shoppingCart);
        }
        return res;
    }

    @Override
    public boolean updateNumber(ShoppingCart shoppingCart) {

        LambdaQueryWrapper<ShoppingCart> lqw =new LambdaQueryWrapper<>();
        lqw.eq(shoppingCart.getDishId() != null,ShoppingCart::getDishId,shoppingCart.getDishId())
           .eq(shoppingCart.getSetmealId() != null,ShoppingCart::getSetmealId,shoppingCart.getNumber());

        shoppingCart = this.getOne(lqw);

        Integer number = shoppingCart.getNumber();

        boolean res = false;

        if(number > 1){
            shoppingCart.setNumber(number - 1);
           res = this.updateById(shoppingCart);
        } else {
           res = this.removeById(shoppingCart.getId());
        }
        return res;
    }

    @Override
    public boolean deleteAll() {
        Long id = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,id);
        boolean res = this.remove(lqw);
        return res;
    }
}
