package com.heu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heu.domin.AddressBook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface AddressBookService extends IService<AddressBook> {

    public List<AddressBook> getALl();

    public boolean saveAddress(AddressBook addressBook);

    public boolean setDefaultAddress(AddressBook addressBook);

    public AddressBook showById(Long id);

    public boolean update(AddressBook addressBook);

    public AddressBook getDefaultAddress();
}
