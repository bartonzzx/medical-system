package com.mobai.medical.domain;

import lombok.Data;

import java.util.Date;

@Data
public class DrugCompany {
  private Long companyId;
  private String companyName;
  private String companyPhone;
  private Date updatetime;
  private Date createtime;
}