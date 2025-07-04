package com.mobai.medical.handler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobai.medical.utils.Msg;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuhenticationFailHandler implements AuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
    System.out.println("here in MyAuhenticationFailHandler");
    httpServletResponse.setContentType("text/json;charset=utf-8");
    httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(Msg.fail().mess("用户名或密码错误")));
  }
}