package com.heu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heu.common.BaseContext;
import com.heu.dao.AddressBookDao;
import com.heu.domin.AddressBook;
import com.heu.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookDao, AddressBook> implements AddressBookService {


    @Override
    public List<AddressBook> getALl() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId,userId)
           .orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = this.list(lqw);
        return list;
    }

    @Override
    public boolean saveAddress(AddressBook addressBook) {
        log.info("保存地址:{}",addressBook);
        Long id = BaseContext.getCurrentId();
        addressBook.setUserId(id);
        boolean res = this.save(addressBook);
        return res;
    }

    @Override
    public boolean setDefaultAddress(AddressBook addressBook) {
        // 查询已存在的默认地址
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getIsDefault,1);
        AddressBook one = this.getOne(lqw);
        // 存在默认地址时
        if(one != null){
            // 修改默认状态
            one.setIsDefault(0);
            this.updateById(one);
        }
        // 设置默认地址
        addressBook.setIsDefault(1);
        boolean res = this.updateById(addressBook);
        return res;
    }

    @Override
    public AddressBook showById(Long id) {
        AddressBook addressBook = this.getById(id);
        return addressBook;
    }

    @Override
    public boolean update(AddressBook addressBook) {
        boolean res = this.updateById(addressBook);
        return res;
    }

    @Override
    public AddressBook getDefaultAddress() {
        Long id = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId,id).eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = this.getOne(lqw);
        return addressBook;
    }
}
