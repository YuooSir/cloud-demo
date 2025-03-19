package com.zero.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zero.order.bean.Order;
import com.zero.order.feign.ProductFeignClient;
import com.zero.order.service.OrderService;
import com.zero.product.bean.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

  @Autowired
  ProductFeignClient productFeignClient;

  // blockHandler 标注降级处理方法
  @SentinelResource(value = "createOrder", blockHandler = "createOrderFallback")
  @Override
  public Order createOrder(int customerId, int productId, int quantity) {

    Order order = new Order();
    Random random = new Random();
    order.setOrderId(random.nextInt(1000));
    order.setCustomerId(customerId);

    Product product = productFeignClient.getProduct(productId);
    List<Product> products = new ArrayList<>();
    products.add(product);

    int price = 0;
    if (product != null) {
      price = product.getPrice();
    }
    order.setTotalAmount(price * quantity);

    order.setProductList(products);

    return order;
  }

  /**
   * 降级处理方法
   */
  public Order createOrderFallback(int customerId, int productId, int quantity, BlockException e) {

    log.error("createOrderFallback: {}", e.getClass());

    Random random = new Random();

    Order order = new Order();
    order.setOrderId(random.nextInt(1000));
    order.setCustomerId(customerId);

    Product product = new Product();
    product.setProductId(productId);
    int price = random.nextInt(100);
    product.setPrice(price);
    product.setName("未知商品");

    List<Product> products = new ArrayList<>();
    products.add(product);

    order.setTotalAmount(price * quantity);
    order.setProductList(products);

    return order;
  }

}
