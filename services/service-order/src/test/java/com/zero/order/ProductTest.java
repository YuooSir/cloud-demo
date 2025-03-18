package com.zero.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@SpringBootTest
public class ProductTest {

  @Autowired
  private DiscoveryClient discoveryClient;

  @Test
  public void testDiscoveryClient() {
    for (String service : discoveryClient.getServices()) {
      // 微服务名称
      System.out.println(service);
      // 微服务的ip+端口
      discoveryClient.getInstances(service)
          .forEach(instance -> System.out.println(instance.getHost() + "\t" + instance.getPort()));
    }
  }

}
