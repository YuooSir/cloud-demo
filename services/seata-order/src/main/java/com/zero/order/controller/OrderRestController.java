package com.zero.order.controller;


import com.zero.order.bean.OrderTbl;
import com.zero.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {

  @Autowired
  OrderService orderService;


  /**
   * 创建订单
   *
   * @param userId
   * @param commodityCode
   * @param orderCount
   * @return
   */
  @PostMapping("/create")
  public String create(@RequestParam("userId") String userId,
      @RequestParam("commodityCode") String commodityCode, @RequestParam("count") int orderCount) {
    OrderTbl tbl = orderService.create(userId, commodityCode, orderCount);
    return "order create success = 订单id：【" + tbl.getId() + "】";
  }

}
