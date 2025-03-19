# SpringClod学习笔记

## Nacos

### 注册中心

#### 服务注册

1. 引入服务发现依赖

   ```xml
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
   </dependency>
   ```

2. 在application.yml文件中配置nacos信息

   ```yml
   spring:
     cloud:
       nacos:
         server-addr: 127.0.0.1:8848
   ```

3. 启动nacos

   ```cmd
   startup.cmd -m standalone
   ```

4. 启动微服务

5. 在[nacos配置中心](http://localhost:8848/nacos)查看是否注册成功（服务管理 → 服务列表）

#### 服务发现

1. 在微服务的启动类添加注解 @EnableDiscoveryClient

2. 测试代码

   ```java
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
   ```

#### 服务调用

1. 引入负载均衡依赖

   ```xml
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-loadbalancer</artifactId>
   </dependency>
   ```

2. 注册 RestTemplate 的 Bean ，加上 @LoadBalanced 注解

   ```java
   @Configuration
   public class RestTemplateConfig {
   
     // 注解式负载均衡
     @LoadBalanced
     @Bean
     RestTemplate restTemplate() {
       return new RestTemplate();
     }
   
   }
   ```

3. 调用服务

   ```java
   @Service
   public class OrderServiceImpl implements OrderService {
   
     @Autowired
     RestTemplate restTemplate;
   
     @Override
     public Order createOrder(int customerId, int productId, int quantity) {
   
       // ...
       
       // 调用其他服务
       // product-service 服务的名称
       // 向远程发送请求时，服务名称会被动态替换为对应机器的IP+Port
       String url = "http://product-service//product/" + productId;
       Product product = restTemplate.getForObject(url, Product.class);
       
       // ....
   
       return order;
     }
   }
   ```

### 配置中心

#### 导入配置

1. 引入配置中心依赖

   ```xml
   <dependency>
     <groupId>com.alibaba.cloud</groupId>
     <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
   </dependency>
   ```

2. 修改application.yml文件

   **如果import内导入了多个配置文件，先声明的优先，配置文件中的相同项不会被后续的文件覆盖**

   ```yml
   spring:
     cloud:
       nacos:
         server-addr: 127.0.0.1:8848
     # 导入配置中心的配置文件
     config:
       import: nacos:order-service.yml
   ```

   ```yml
   spring:
     cloud:
       nacos:
         server-addr: 127.0.0.1:8848
         #  关闭配置中心检查
         config:
           import-check:
             enabled: false
   ```

   

3. 在nacos配置中心创建新的配置文件

   **配置中心的配置文件的优先级高于项目内application.yml文件中的配置**

   1. 配置管理
   2. 配置列表
   3. 创建配置
   4. Data ID 输入在application.yml文件中记录的文件名称
   5. 设置配置文件的格式
   6. 输入配置文件的内容
   7. 发布

4. 获取方法

   ```java
     @Value("${order.timeout}")
     private String timeout;
   ```

5. 开启配置刷新功能，实时获取最新的配置文件的内容

   ```java
   @RefreshScope
   ```

6. **进阶写法**

   借助 ConfigurationProperties 实现自动刷新

   ```java
   @Component
   @ConfigurationProperties(prefix = "order")
   @Data
   public class OrderProperty {
   
     private String timeout;
     private String autoConfirm;
   }
   ```

7. 实时监听配置中心的指定配置文件的内容变化(在启动器类加入)

   ```java
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
             System.out.println("========》" + LocalDateTime.now() + "\t配置信息发生变化:《========");
             System.out.println(configInfo);
             System.out.println("============================》《============================");
           }
         });
       };
     }
   ```

#### 数据隔离

使用**名称空间（namespace）**区分不同的环境

使用**组（group）**区分不同的微服务

使用**配置文件（Data id）**区分不同的配置

```yml
spring:
  application:
    name: order-service
  # 指定当前环境
  profiles:
    active: prod
  cloud:
    nacos:
      server-addr: 127.0.0.1:8848
      config:
        # 关闭配置中心检查
        import-check:
          enabled: false
        # 指定命名空间。默认使用public
        namespace: ${spring.profiles.active:public}

---
spring:
  config:
    import:
      - nacos:common.yml?group=order
      - nacos:db.yml?group=order
    activate:
      on-profile: dev

---
spring:
  config:
    import:
      - nacos:common.yml?group=order
      - nacos:db.yml?group=order
      - nacos:test.yml?group=order
    activate:
      on-profile: test

---
spring:
  config:
    import:
      - nacos:common.yml?group=order
      - nacos:db.yml?group=order
      - nacos:prod.yml?group=order
    activate:
      on-profile: prod
```

