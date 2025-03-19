package com.zero.order.service.impl;

import com.zero.order.bean.Order;
import com.zero.order.feign.ProductFeignClient;
import com.zero.order.service.OrderService;
import com.zero.product.bean.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  ProductFeignClient productFeignClient;

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
}
