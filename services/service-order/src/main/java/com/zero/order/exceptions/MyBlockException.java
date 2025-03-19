package com.zero.order.exceptions;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.common.R;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 * Web接口被Sentinel限制后的自定义异常处理
 */
@Component
public class MyBlockException implements BlockExceptionHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e)
      throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
    response.setContentType("application/json;charset=UTF-8");
    PrintWriter writer = response.getWriter();
    R.error(500, e.getMessage());
    writer.write(objectMapper.writeValueAsString(R.error(500, "违反Sentinel的流控规则，禁止访问")));
    writer.flush();
    writer.close();
  }
}
