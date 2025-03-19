package com.zero.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

// 远程调用第三方接口时，value任意（不能和现有重复）,url需要正确填写
// https://jsonplaceholder.typicode.com/posts
// url：https://jsonplaceholder.typicode.com
// 参数：/posts
@FeignClient(value = "another-user-service", url = "https://randomuser.me")
public interface AnotherFeignClient {

  @GetMapping("/api")
  String getUserInfo();
}
