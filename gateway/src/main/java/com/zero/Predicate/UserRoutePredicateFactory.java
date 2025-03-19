package com.zero.Predicate;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import javax.validation.constraints.NotEmpty;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

@Component
public class UserRoutePredicateFactory extends AbstractRoutePredicateFactory<UserRoutePredicateFactory.Config> {

  public UserRoutePredicateFactory() {
    super(UserRoutePredicateFactory.Config.class);
  }

  @Override
  public List<String> shortcutFieldOrder() {
    return Arrays.asList("param", "value");
  }

  @Override
  public Predicate<ServerWebExchange> apply(Config config) {
    return serverWebExchange -> {
      ServerHttpRequest request = serverWebExchange.getRequest();
      String first = request.getQueryParams().getFirst(config.param);
      return StringUtils.hasText(first) && first.equals(config.value);
    };
  }


  @Validated
  public static class Config {

    @NotEmpty
    private String param;

    @NotEmpty
    private String value;

    public String getParam() {
      return param;
    }

    public void setParam(String param) {
      this.param = param;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

  }

}
