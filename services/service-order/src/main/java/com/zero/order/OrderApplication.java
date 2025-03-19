package com.zero.order;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
// 开启服务发现功能
@EnableDiscoveryClient
// 开启Feign远程调用功能
@EnableFeignClients
public class OrderApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderApplication.class, args);
  }

  @Bean
  ApplicationRunner runner(NacosConfigManager manager) {
    return args -> {
      ConfigService configService = manager.getConfigService();
      configService.addListener("order-service.yml", "DEFAULT_GROUP", new Listener() {
        @Override
        public Executor getExecutor() {
          return Executors.newFixedThreadPool(2);
        }

        @Override
        public void receiveConfigInfo(String configInfo) {
          System.out.println("=========》" + LocalDateTime.now() + "\t配置信息发生变化:《=========");
          System.out.println(configInfo);
          System.out.println("=============================》《=============================");
        }
      });
    };
  }
}
