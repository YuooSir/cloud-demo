package com.zero.order.service;

import com.zero.order.bean.Order;

public interface OrderService {

  Order createOrder(int customerId, int productId, int quantity);
}
