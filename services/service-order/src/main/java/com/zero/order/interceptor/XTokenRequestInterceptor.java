package com.zero.order.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

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
