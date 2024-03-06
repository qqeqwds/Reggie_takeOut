package com.heu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heu.common.R;
import com.heu.domin.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    public Page getByInfo(Integer page , Integer pageSize);

    public List<Category> getByType(Integer type);

    public boolean delete(Long id);
}
