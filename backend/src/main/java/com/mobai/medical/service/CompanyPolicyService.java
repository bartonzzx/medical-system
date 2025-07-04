package com.mobai.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobai.medical.mapper.CompanyPolicyMapper;
import com.mobai.medical.model.CompanyPolicyModel;
import com.mobai.medical.param.CompanyPolicyParam;
import com.mobai.medical.utils.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyPolicyService {
  @Autowired
  private CompanyPolicyMapper companyPolicyMapper;

  public Msg getAllPolicyWithPage(CompanyPolicyParam param) {
    if (param.getSize() == 0) {
      param.setSize(1);
    }
    PageHelper.startPage(param.getPn(), param.getSize());
    List<CompanyPolicyModel> list = companyPolicyMapper.getAllPolicy(param);
    PageInfo<CompanyPolicyModel> info = new PageInfo<>(list, 5);
    return Msg.success().data("policyInfo", info);
  }
}