package com.mobai.medical.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 在线接口配置类-SwaggerConfig
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket webApiConfig() {

    return new Docket(DocumentationType.SWAGGER_2)
            .groupName("Medical-API")
            .apiInfo(webApiInfo())
            .select()
            .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
            .paths(Predicates.not(PathSelectors.regex("/error.*")))
            .build();

  }

  private ApiInfo webApiInfo() {

    return new ApiInfoBuilder()
            .title("慧医数字医疗应用系统-API文档")
            .description("本文档描述了慧医数字医疗应用系统的接口定义")
            .version("1.0")
            .contact(new Contact("mobai", "https://github.com/mintoneko", "mobaisilent@gmail.com"))
            .build();
  }
}