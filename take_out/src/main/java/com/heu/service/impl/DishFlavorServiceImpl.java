package com.heu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heu.dao.DishFlavorDao;
import com.heu.domin.DishFlavor;
import com.heu.dto.DishDto;
import com.heu.service.DishFlavorService;
import com.heu.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorDao, DishFlavor> implements DishFlavorService {

    @Autowired
    private DishFlavorDao dishFlavorDao;

}
