package com.mobai.medical.entity;

import com.mobai.medical.domain.Sale;

public class SaleEntity extends Sale {

  private Long total; // 插入成功后返回的总记录数

  public Long getTotal() {
    return total;
  }

  public void setTotal(Long total) {
    this.total = total;
  }
}