package com.zero.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zero.order.bean.Order;
import com.zero.order.properties.OrderProperty;
import com.zero.order.service.impl.OrderServiceImpl;
import com.zero.product.bean.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 开启配置刷新功能
// @RefreshScope
@RestController
public class OrderController {


  private static final Logger log = LoggerFactory.getLogger(OrderController.class);
  @Autowired
  private OrderServiceImpl orderService;

  // @Value("${order.timeout}")
  // private String timeout;
  // @Value("${order.auto-confirm}")
  // private String autoConfirm;

  @Autowired
  private OrderProperty orderProperty;

  @GetMapping("/config")
  public String getConfig() {
    String timeout = orderProperty.getTimeout();
    String autoConfirm = orderProperty.getAutoConfirm();
    String dbUrl = orderProperty.getDbUrl();
    return "timeout=" + timeout + "\tautoConfirm=" + autoConfirm + "\tdbUrl=" + dbUrl;
  }

  @PostMapping("/order")
  public Order CreateOrder(@RequestParam int customerId, @RequestParam int productId,
      @RequestParam int quantity) {
    Order order = orderService.createOrder(customerId, productId, quantity);
    System.out.println("订单创建成功");
    return order;
  }

  @GetMapping("/sec-order")
  @SentinelResource(value = "secondOrder-sentinel", fallback = "CreateSecondOrderFallback")
  public Order CreateSecondOrder(@RequestParam int customerId, @RequestParam int productId) {
    Order order = orderService.createOrder(customerId, productId, 1);
    System.out.println("秒杀订单创建成功");
    return order;
  }

  public Order CreateSecondOrderFallback(int customerId, int productId, Throwable e) {
    System.out.println("CreateSecondOrderFallback");
    Order order = new Order();
    Product product = new Product();
    Random random = new Random();
    product.setProductId(productId);
    product.setName("未知商品");
    product.setPrice(0);
    order.setOrderId(random.nextInt(10000));
    order.setCustomerId(customerId);
    order.setTotalAmount(0);
    List<Product> products = new ArrayList<>();
    products.add(product);
    order.setProductList(products);
    log.error(String.valueOf(e.getClass()));
    return order;
  }
}
