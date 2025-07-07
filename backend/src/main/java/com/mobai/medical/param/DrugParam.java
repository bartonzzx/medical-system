package com.mobai.medical.param;

import com.mobai.medical.domain.Drug;
import lombok.Data;

@Data
public class DrugParam extends Drug {

  private Long[] saleIds; //销售该药品的销售地点
}