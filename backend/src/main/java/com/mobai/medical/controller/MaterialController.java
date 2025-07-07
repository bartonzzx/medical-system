package com.mobai.medical.controller;

import com.mobai.medical.param.MaterialParam;
import com.mobai.medical.service.MaterialService;
import com.mobai.medical.utils.Msg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "必备材料控制器")
@RestController
@RequestMapping("/api/materials")
@CrossOrigin
public class MaterialController {

  @Autowired
  MaterialService materialService;

  @GetMapping(value = "")
  public Msg getPolicyWithPage(MaterialParam param) {
    Msg msg = materialService.getAllMaterialWithPage(param);
    return msg;
  }
}