package com.zero.order.feign.fallback;

import com.zero.order.feign.ProductFeignClient;
import com.zero.product.bean.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductFeignClientFallback implements ProductFeignClient {

  @Override
  public Product getProduct(int productId) {
    Product product = new Product();
    product.setProductId(productId);
    product.setName("未知商品");
    product.setPrice(0);
    return product;
  }

}
