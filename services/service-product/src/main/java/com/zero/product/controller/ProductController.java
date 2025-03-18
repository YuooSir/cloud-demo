package com.zero.product.controller;

import com.zero.product.bean.Product;
import com.zero.product.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {


  @Autowired
  private ProductServiceImpl productService;

  @GetMapping("/product/{productId}")
  public Product getProduct(@PathVariable int productId) {
    System.out.println("新的请求：" + productId);
    return productService.getProduct(productId);
  }
}
