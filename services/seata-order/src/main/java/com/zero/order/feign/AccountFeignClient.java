package com.zero.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "seata-account")
public interface AccountFeignClient {

  @PostMapping("/debit")
  String debit(@RequestParam("userId") String userId, @RequestParam("money") int money);
}
