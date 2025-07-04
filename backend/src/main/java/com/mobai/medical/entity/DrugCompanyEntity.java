package com.mobai.medical.entity;

import com.mobai.medical.domain.DrugCompany;

public class DrugCompanyEntity extends DrugCompany {
  private Long total;

  public Long getTotal() {
    return total;
  }

  public void setTotal(Long total) {
    this.total = total;
  }
}