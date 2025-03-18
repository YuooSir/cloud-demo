package com.zero.order.bean;

import com.zero.product.bean.Product;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

  private int orderId;
  private int customerId;
  private int totalAmount;
  private List<Product> productList;
}
