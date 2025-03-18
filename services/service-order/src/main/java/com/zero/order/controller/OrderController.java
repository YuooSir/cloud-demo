package com.zero.order.controller;

import com.zero.order.bean.Order;
import com.zero.order.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {


  @Autowired
  private OrderServiceImpl orderService;

  @PostMapping("/order")
  public Order CreateOrder(@RequestParam int customerId, @RequestParam int productId, @RequestParam int quantity) {
    Order order = orderService.createOrder(customerId, productId, quantity);
    System.out.println("订单创建成功");
    return order;
  }
}
