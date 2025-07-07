package com.mobai.medical.controller;

import com.mobai.medical.param.MedicalPolicyParam;
import com.mobai.medical.service.MedicalPolicyService;
import com.mobai.medical.utils.Msg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "医保政策控制器类")
@RestController
@RequestMapping("/api/medical_policys")
@CrossOrigin
public class MedicalPolicyController {
  @Autowired
  MedicalPolicyService medicalPolicyService;

  // 分页，条件查询医保政策信息
  @GetMapping(value = "")
  public Msg getMedicalPolicyWithPage(MedicalPolicyParam param) {
    Msg msg = medicalPolicyService.getMedicalPolicyWithPage(param);
    return msg;
  }
}