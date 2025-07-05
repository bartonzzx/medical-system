package com.mobai.medical.controller;

import com.mobai.medical.domain.CompanyPolicy;
import com.mobai.medical.domain.DrugCompany;
import com.mobai.medical.entity.CompanyPolicyEntity;
import com.mobai.medical.param.CompanyPolicyParam;
import com.mobai.medical.service.CompanyPolicyService;
import com.mobai.medical.utils.Msg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

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

  @RolesAllowed({"1"})
  @PostMapping(value = "")
  public Msg saveCompanyPolicy(@RequestBody CompanyPolicy cp) {
    String title = cp.getTitle();
    String message = cp.getMessage();
    Long companyId = cp.getCompanyId();

    if (title == null || message == null || companyId == null || title == "" || message == "") {
      return Msg.fail().mess("填写信息不完整");
    }

    return companyPolicyService.saveCompanyPolicy(cp);
  }

  @RolesAllowed({"1"})
  @PutMapping(value = "/{id}")
  public Msg updateCompanyPolicy(@PathVariable("id") Long id, @RequestBody CompanyPolicyEntity ce) {
    Msg msg = companyPolicyService.updataCompanyPolicy(id, ce);
    return msg;
  }

  @RolesAllowed({"1"})
  @DeleteMapping("{id}")
  public Msg deleteCompanyPolicyById(@PathVariable("id") Integer id) {
    Msg msg = companyPolicyService.deleteCompanyPolicyById(id);
    return msg;
  }
}