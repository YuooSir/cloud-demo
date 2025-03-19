package com.zero.order.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

  // 开启Feign日志
  @Bean
  Logger.Level getLevel() {
    return Logger.Level.FULL;
  }

  // 开启Feign重试机制
  @Bean
  Retryer getRetryer() {
    return new Retryer.Default();
  }
}
