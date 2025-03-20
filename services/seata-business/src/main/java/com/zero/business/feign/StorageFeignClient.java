package com.zero.business.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "seata-storage")
public interface StorageFeignClient {

  @PostMapping("/deduct")
  String deduct(@RequestParam("commodityCode") String commodityCode,
      @RequestParam("count") Integer count);
}
