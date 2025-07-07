package com.mobai.medical.model;

import com.mobai.medical.domain.Drug;
import lombok.Data;

import java.util.List;

@Data
public class DrugModel extends Drug {
  private List<SaleModel> drugSales; // 销售该药的药店地址（集合类型，有多个）
  private String drugSale; // 销售该药的药店地址（字符串类型，有多个）
  private Integer saleId;

  @Override
  public String toString() {
    return "DrugModel{" +
            "drugSales=" + drugSales +
            ", drugSale='" + drugSale + '\'' +
            '}';
  }

}