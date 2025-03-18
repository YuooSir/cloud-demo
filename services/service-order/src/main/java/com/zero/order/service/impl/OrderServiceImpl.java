package com.zero.order.service.impl;

import com.zero.order.bean.Order;
import com.zero.order.service.OrderService;
import com.zero.product.bean.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  RestTemplate restTemplate;

  @Override
  public Order createOrder(int customerId, int productId, int quantity) {

    Order order = new Order();
    Random random = new Random();
    order.setOrderId(random.nextInt(1000));
    order.setCustomerId(customerId);

    // 调用其他服务
    String url = "http://product-service//product/" + productId;
    Product product = restTemplate.getForObject(url, Product.class);
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
