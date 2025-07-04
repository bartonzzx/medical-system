package com.mobai.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobai.medical.domain.DrugCompany;
import com.mobai.medical.mapper.CompanyMapper;
import com.mobai.medical.utils.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyService {

  @Autowired
  private CompanyMapper companyMapper;

  public PageInfo<DrugCompany> getCompanyWithPage(Integer pn, Integer size, String name) {
    if (pn == null && size == null) {
      pn = 1;
      size = Integer.MAX_VALUE;
    }
    if (pn == null) {
      pn = 1;
    }
    if (size == null) {
      size = Integer.MAX_VALUE;
    }
    if (size == 0) {
      size = 1;
    }
    PageHelper.startPage(pn, size);
    List<DrugCompany> list = companyMapper.getAllCompany(name);
    PageInfo<DrugCompany> info = new PageInfo<>(list, 5);
    return info;
  }

  public Msg getCompanyById(Integer id) {
    DrugCompany company = companyMapper.getCompanyById(id);

    if (company == null) {
      return Msg.fail().mess("没有找到");
    }
    return Msg.success().data("company", company);
  }
}