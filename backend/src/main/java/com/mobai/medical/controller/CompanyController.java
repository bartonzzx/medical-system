package com.mobai.medical.controller;

import com.github.pagehelper.PageInfo;
import com.mobai.medical.domain.DrugCompany;
import com.mobai.medical.service.CompanyService;
import com.mobai.medical.utils.Msg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "医药公司信息控制器")
@RestController
@RequestMapping("/api/companys")
@CrossOrigin
public class CompanyController {

  @Autowired
  private CompanyService companyService;

  @GetMapping(value = {"/{pn}/{size}", ""})
  public Msg getCompanyWithPage(
          @PathVariable(value = "pn", required = false) Integer pn,
          @PathVariable(value = "size", required = false) Integer size,
          @RequestParam(required = false) String name) {
    PageInfo<DrugCompany> info = companyService.getCompanyWithPage(pn, size, name);
    if (info != null) {
      return Msg.success().data("pageInfo", info);
    }
    return Msg.fail();
  }

  @GetMapping("{id}")
  public Msg getCompanyById(@PathVariable("id") Integer id) {
    Msg msg = companyService.getCompanyById(id);
    return msg;
  }
}