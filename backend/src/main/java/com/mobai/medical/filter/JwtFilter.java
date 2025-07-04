package com.mobai.medical.filter;

import com.mobai.medical.model.AccountModel;
import com.mobai.medical.service.security.UserDetailsServiceImpl;
import com.mobai.medical.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private static Logger logger = Logger.getLogger(String.valueOf(JwtFilter.class));

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    String token = httpServletRequest.getHeader("Authorization");
    httpServletResponse.setContentType("text/json;charset=utf-8");
    System.out.println("这里是JwtFilter.doFilterInternal，当前Token为："+token);
    if (StringUtils.hasLength(token)) {
      String uname = null;
      try {
        uname = (String) JwtUtils.getClaims(token).get("uname");
        System.out.println("解析Token成功，当前登录用户为："+uname);
      } catch (Exception e) {
        logger.info("失效身份");
      }
      if (uname != null) {
        String role = (String) JwtUtils.getClaims(token).get("role");
        UserDetails userDetails = userDetailsService.loadUserByUsername(uname);
        AccountModel model = (AccountModel) userDetails;
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(model, null, grantedAuthorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));// 设置认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("当前登录用户身份认证成功！");
        logger.info("身份认证成功");
      }
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}