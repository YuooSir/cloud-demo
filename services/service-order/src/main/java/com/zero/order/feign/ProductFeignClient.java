package com.zero.order.feign;

import com.zero.order.feign.fallback.ProductFeignClientFallback;
import com.zero.product.bean.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "product-service", fallback = ProductFeignClientFallback.class)
public interface ProductFeignClient {

  // 此时GetMapping表示发起GET请求
  @GetMapping("/product/{productId}")
  Product getProduct(@PathVariable int productId);

}
