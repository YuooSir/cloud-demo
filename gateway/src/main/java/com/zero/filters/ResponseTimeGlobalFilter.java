package com.zero.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器，响应时间过滤器
 */
@Component
@Slf4j
public class ResponseTimeGlobalFilter implements GlobalFilter, Ordered {

  /**
   * 过滤器处理逻辑
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String requestUri = exchange.getRequest().getURI().toString();
    long start = System.currentTimeMillis();
    log.info("==========请求「{}」开始==========", requestUri);

    return chain.filter(exchange).doFinally(signalType -> {
      long end = System.currentTimeMillis();
      log.info("==========请求「{}」结束，耗时{}s==========", requestUri, (end - start) * 1.0 / 1000);
    });
  }

  /**
   * 设置过滤器优先级，越小越靠前
   */
  @Override
  public int getOrder() {
    return 0;
  }
}
