package com.mobai.medical.controller;

import com.mobai.medical.entity.AccountEntity;
import com.mobai.medical.service.RegisterService;
import com.mobai.medical.service.security.UserDetailsServiceImpl;
import com.mobai.medical.utils.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@Validated // 启用参数校验[2,6](@ref)
public class RegisterController {

  @Autowired
  private RegisterService registerService;

  @PostMapping("")
  public Msg register(@RequestBody AccountEntity ae) {
    return registerService.register(ae);
  }

}