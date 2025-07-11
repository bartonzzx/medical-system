package com.mobai.medical.config.security;

import com.mobai.medical.filter.JwtFilter;
import com.mobai.medical.handler.security.CustomizeAuthenticationEntryPoint;
import com.mobai.medical.handler.security.MyAccessDeniedHandler;
import com.mobai.medical.handler.security.MyAuhenticationFailHandler;
import com.mobai.medical.handler.security.MyAuthenticationSuccesHandler;
import com.mobai.medical.service.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsServiceImpl userDetailsService; // 自定义登录业务类

  @Autowired
  private MyAuhenticationFailHandler authenticationFailHandler; // 登录失败处理器

  @Autowired
  private MyAuthenticationSuccesHandler authenticationSuccessHandler; // 登录成功处理器

  @Autowired
  private CustomizeAuthenticationEntryPoint authenticationEntryPoint; // 未登录或登录失效处理器

  @Autowired
  private MyAccessDeniedHandler accessDeniedHandler; // 无权限访问处理器

  @Autowired
  private JwtFilter jwtFilter; // JWT校验类

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    // 配置表单登录
    http.formLogin()
            .loginProcessingUrl("/api/login")
            .successHandler(authenticationSuccessHandler)
            .failureHandler(authenticationFailHandler)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler);

    // 配置授权
    http.authorizeRequests(requests -> {
      // 注册接口设置为完全公开
      ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = requests
              .antMatchers(
                      "/api/register",             // 添加注册接口
                      "/api/login",
                      "/login",
                      "/v2/api-docs",
                      "/swagger-resources/configuration/ui",
                      "/swagger-resources",
                      "/swagger-resources/configuration/security",
                      "/swagger-ui.html",
                      "/webjars/**",
                      "/image/**"
              ).permitAll();

      // 其他请求需要认证
      registry.anyRequest().authenticated();
    });

    // 配置其他参数
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .cors()
            .configurationSource(configurationSource())
            .and()
            .csrf().disable();
  }

  // 解决前后端使用security跨域问题
  CorsConfigurationSource configurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.addAllowedOriginPattern("*");
    // corsConfiguration.addAllowedOrigin("*");
    corsConfiguration.setAllowCredentials(true);//前后端分离项目需要携带cookie时，需要此句，但加上之后origin里就不能为"*"
    corsConfiguration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }
}