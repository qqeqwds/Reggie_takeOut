package com.heu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heu.dao.OrderDetailDao;
import com.heu.domin.OrderDetail;
import com.heu.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailDao, OrderDetail> implements OrderDetailService {


}
