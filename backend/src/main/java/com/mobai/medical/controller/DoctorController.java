package com.mobai.medical.controller;

import com.mobai.medical.param.DoctorParam;
import com.mobai.medical.service.DoctorService;
import com.mobai.medical.utils.Msg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@Api(tags = "医师控制器类")
@RestController
@RequestMapping("/api/doctors")
@CrossOrigin
public class DoctorController {
  @Autowired
  private DoctorService doctorService;

  // 分页查询医师信息
  @RolesAllowed({"1", "2"})
  @GetMapping(value = "")
  public Msg getDoctorWithPage(DoctorParam param) {
    return doctorService.getDoctorWithPage(param);
  }

  // 进行接口权限控制，只有拥有1或者2权限的才能访问该接口

  // 获取所有医师级别信息和诊治类型信息
  @RolesAllowed({"1", "2"})
  @GetMapping("/info")
  public Msg getLevelAndType() {
    return doctorService.getLevelAndType();
  }
}