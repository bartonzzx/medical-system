package com.mobai.medical.model;

import com.mobai.medical.domain.MedicalPolicy;
import lombok.Data;

@Data
public class MedicalPolicyModel extends MedicalPolicy {

  private CityModel cityModel; // 医保政策包含的所属城市
}