package com.mobai.medical.controller;

import com.mobai.medical.param.CompanyPolicyParam;
import com.mobai.medical.service.CompanyPolicyService;
import com.mobai.medical.utils.Msg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "医药公司政策控制器类")
@RestController
@RequestMapping("/api/company_policys")
@CrossOrigin
public class CompanyPolicyController {

  @Autowired
  private CompanyPolicyService companyPolicyService;

  @GetMapping(value = "")
  public Msg getPolicyWithPage(CompanyPolicyParam param) {
    Msg msg = companyPolicyService.getAllPolicyWithPage(param);
    return msg;
  }
}