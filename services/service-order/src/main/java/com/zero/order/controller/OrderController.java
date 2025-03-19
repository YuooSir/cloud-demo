package com.zero.order.controller;

import com.zero.order.bean.Order;
import com.zero.order.properties.OrderProperty;
import com.zero.order.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 开启配置刷新功能
// @RefreshScope
@RestController
public class OrderController {


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
}
