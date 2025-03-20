package com.zero.business.service.impl;

import com.zero.business.feign.OrderFeignClient;
import com.zero.business.feign.StorageFeignClient;
import com.zero.business.service.BusinessService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private StorageFeignClient storageFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @GlobalTransactional
    @Override
    public void purchase(String userId, String commodityCode, int orderCount) {
        //TODO 1. 扣减库存
        storageFeignClient.deduct(commodityCode, orderCount);
        //TODO 2. 创建订单
        orderFeignClient.create(userId, commodityCode, orderCount);
    }
}
