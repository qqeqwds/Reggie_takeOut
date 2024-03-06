package com.heu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heu.common.R;
import com.heu.domin.AddressBook;
import com.heu.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService service;

    @GetMapping("/list")
    public R<List<AddressBook>> getAll(){
        List<AddressBook> list = service.getALl();
        return list != null ? R.success(list) : R.error("查询失败");
    }

    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook){
        boolean res = service.saveAddress(addressBook);
        return res ? R.success("添加成功") : R.error("添加失败");
    }

    @PutMapping("/default")
    public R<String> setDefaultAddress(@RequestBody AddressBook addressBook){
        boolean res = service.setDefaultAddress(addressBook);
        return res ? R.success("设置成功") : R.error("设置失败");
    }

    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable Long id){
        AddressBook res = service.showById(id);
        return res != null ? R.success(res) : R.error("回显失败");
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        boolean res = service.update(addressBook);
        return res ? R.success("修改成功") : R.error("修改失败");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefaultAddress(){
        AddressBook address = service.getDefaultAddress();
        return address != null ? R.success(address) : R.error("查询失败");
    }
}
