package com.mobai.medical.controller;

import com.mobai.medical.domain.CompanyPolicy;
import com.mobai.medical.domain.DrugCompany;
import com.mobai.medical.entity.CompanyPolicyEntity;
import com.mobai.medical.param.CompanyPolicyParam;
import com.mobai.medical.service.CompanyPolicyService;
import com.mobai.medical.utils.Msg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
    // 注意这个地方的分页有问题，实际上前端传过来的参数没有使用到，具体的分页参考CompanyController，以及参考postman中公司分页查询的参数
    Msg msg = companyPolicyService.getAllPolicyWithPage(param);
    return msg;
  }

  // 添加医药公司政策信息
  @RolesAllowed({"1"})
  @PostMapping(value = "")
  public Msg saveMedicalPolicy(@RequestBody CompanyPolicyParam param) {
    Msg msg = companyPolicyService.savePolicy(param);
    return msg;
  }

  // 更新医药公司政策信息
  @RolesAllowed({"1"})
  @PutMapping(value = "/{id}")
  public Msg updateMedicalPolicy(@PathVariable("id") Long id, @RequestBody CompanyPolicyParam param) {
    if (!StringUtils.hasLength(param.getTitle())) {
      return Msg.fail().mess("标题不能为空");
    }
    if (!StringUtils.hasLength(param.getMessage())) {
      return Msg.fail().mess("内容不能为空");
    }
    if (param.getCompanyId() == null) {
      return Msg.fail().mess("公司ID不能为空");
    }
    return companyPolicyService.updatePolicy(id, param);
  }

  // 根据id删除医药公司政策
  @RolesAllowed({"1"})
  @DeleteMapping("/{id}")
  public Msg deletePolicy(@PathVariable("id") Long id) {
    return companyPolicyService.deletePolicy(id);
  }
}