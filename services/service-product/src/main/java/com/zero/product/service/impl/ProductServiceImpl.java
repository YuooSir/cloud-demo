package com.zero.product.service.impl;

import com.zero.product.bean.Product;
import com.zero.product.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  @Override
  public Product getProduct(int productId) {

    // 模拟查询
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    Product product = new Product();
    product.setProductId(productId);
    product.setName("9070 XT");
    product.setPrice(4999);
    return product;
  }
}
