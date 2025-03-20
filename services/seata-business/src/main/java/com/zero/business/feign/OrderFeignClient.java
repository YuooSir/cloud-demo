package com.zero.business.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "seata-order")
public interface OrderFeignClient {

  @PostMapping("/create")
  String create(@RequestParam("userId") String userId,
      @RequestParam("commodityCode") String commodityCode,
      @RequestParam("count") int orderCount);
}
