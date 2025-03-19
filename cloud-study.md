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



## OpenFeign

### 远程调用

1. 引入openFeign依赖

   ```xml
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-openfeign</artifactId>
   </dependency>
   ```

2. 在启动类加入注解 

   ```java
   @EnableFeignClients
   ```

3. 创建FeignClient

   ```java
   @FeignClient(value = "product-service")
   public interface ProductFeignClient {
   
     // 此时GetMapping表示发起GET请求
     @GetMapping("/product/{productId}")
     Product getProduct(@PathVariable int productId);
   
   }
   ```

4. 通过FeignClient调用其他服务

   ```java
   @Service
   public class OrderServiceImpl implements OrderService {
   
     @Autowired
     ProductFeignClient productFeignClient;
   
     @Override
     public Order createOrder(int customerId, int productId, int quantity) {
   
       // ...
       Product product = productFeignClient.getProduct(productId);
       // ...
   
       return order;
     }
   }
   ```

5.  第三方API接口

   ```java
   // 远程调用第三方接口时，value任意（不能和现有重复）,url需要正确填写
   // https://jsonplaceholder.typicode.com/posts
   // url：https://jsonplaceholder.typicode.com
   // 参数：/posts
   @FeignClient(value = "another-blog-service", url = "https://jsonplaceholder.typicode.com")
   public interface AnotherFeignClient {
   
     @GetMapping("/posts")
     String getBlogList(@RequestHeader("Authorization") String auth,
         @RequestParam("token") String token, @RequestParam("page") int page);
   }
   ```

### 开启日志

1. 修改application.yml

   ```yml
   logging:
     level:
       com.zero.order.feign : debug
   ```

2. 修改配置类

   ```java
   import feign.Logger;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   
   @Configuration
   public class OrderConfig {
   
     // 开启Feign日志
     @Bean
     Logger.Level getLevel() {
       return Logger.Level.FULL;
     }
   }
   ```

### 超时配置

1. 修改application.yml文件

   ```yml
   # openfeign 配置
   feign:
     client:
       config:
         # 默认配置
         default:
           logger-level: full
           connect-timeout: 2000
           read-timeout: 2000
         # 指定feign客户端配置
         product-service:
           logger-level: full
           connect-timeout: 5000
           read-timeout: 5000
   ```

   

### 重试机制

1. 修改配置类

   ```java
   // 开启Feign重试机制
   @Bean
   Retryer getRetryer() {
   	return new Retryer.Default();
   }
   ```

   

### 拦截器

#### 请求拦截器

- 作用

  - 给远程服务发送请求前，对请求进行拦截，可在此做一些修改，比如修改请求头等..

  ```java
  @Component
  public class XTokenRequestInterceptor implements RequestInterceptor {
  
    /**
     * 请求拦截器
     * @param template 请求模板
     */
    @Override
    public void apply(RequestTemplate template) {
      System.out.println("请求拦截器拦截到请求");
      template.header("X-Token", "123456");
    }
  }
  ```

  

#### 响应拦截器

- 作用
  - 对远程服务的响应数据进行修改



### 兜底返回

1. 实现FeignClient接口

   ```java
   @Component
   public class ProductFeignClientFallback implements ProductFeignClient {
   
     @Override
     public Product getProduct(int productId) {
       Product product = new Product();
       product.setProductId(productId);
       product.setName("未知商品");
       product.setPrice(0);
       return product;
     }
   
   }
   ```

2. 将接口实现类设置为fallback

   ```java
   @FeignClient(value = "product-service", fallback = ProductFeignClientFallback.class)
   public interface ProductFeignClient {
   
     // 此时GetMapping表示发起GET请求
     @GetMapping("/product/{productId}")
     Product getProduct(@PathVariable int productId);
   
   }
   ```

3. 引入Sentinel依赖

   ```xml
   <dependency>
     <groupId>com.alibaba.cloud</groupId>
     <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
   </dependency>
   ```

4. 修改application.yml，开启熔断

   ```yml
   # openfeign 配置
   feign:
     # 开启服务熔断
     sentinel:
       enabled: true
   ```

   