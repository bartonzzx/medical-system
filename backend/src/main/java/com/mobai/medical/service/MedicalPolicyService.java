package com.mobai.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobai.medical.mapper.MedicalPolicyMapper;
import com.mobai.medical.model.MedicalPolicyModel;
import com.mobai.medical.param.MedicalPolicyParam;
import com.mobai.medical.utils.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalPolicyService {
  @Autowired
  MedicalPolicyMapper medicalPolicyMapper;

  // 分页，条件查询医保政策信息
  public Msg getMedicalPolicyWithPage(MedicalPolicyParam param) {
    PageHelper.startPage(param.getPn(), param.getSize());
    List<MedicalPolicyModel> list = medicalPolicyMapper.getAllPolicy(param);
    PageInfo<MedicalPolicyModel> info = new PageInfo<>(list, 5);
    return Msg.success().data("policyInfo", info);
  }
}
