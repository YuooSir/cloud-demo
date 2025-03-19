package com.zero.filters;

import java.util.UUID;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AddCustomTokenGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {

  @Override
  public GatewayFilter apply(NameValueConfig config) {
    return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
      ServerHttpResponse response = exchange.getResponse();
      HttpHeaders headers = response.getHeaders();
      String value = config.getValue();
      String token = "";
      if ("uuid".equals(value)) {
        token = UUID.randomUUID().toString();
      } else if ("jwt".equals(value)) {
        token = "假装是个jwt";
      } else {
        token = String.valueOf(System.currentTimeMillis());
      }
      headers.add(config.getName(), token);
    }));
  }
}
