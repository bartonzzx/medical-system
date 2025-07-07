package com.mobai.medical.domain;

import com.mobai.medical.domain.superdomain.SuperDomain;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class MedicalPolicy extends SuperDomain {

  @Pattern(regexp = "(^[0-9]*)", message = "编号只能为数字")
  private Long id; // 政策id
  private String title; // 政策标题
  private String message; // 政策信息
  private Long cityId; // 政策对应的城市
  private String createTime; // 创建时间
  private String updateTime; // 更新时间兼发布时间
}