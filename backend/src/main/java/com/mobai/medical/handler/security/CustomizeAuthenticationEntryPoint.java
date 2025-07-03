package com.mobai.medical.handler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobai.medical.utils.Msg;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
    httpServletResponse.setContentType("text/json;charset=utf-8");
    httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(Msg.fail().mess("未登录或登录已过期").code(10006)));
  }
}