package com.zero.order.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "order")
@Data
public class OrderProperty {

  private String timeout;
  private String autoConfirm;
  private String dbUrl;
}
