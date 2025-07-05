package com.mobai.medical.model;

import com.mobai.medical.domain.CompanyPolicy;
import lombok.Data;

@Data
public class CompanyPolicyModel extends CompanyPolicy {
  private DrugCompanyModel drugCompanyModel;
}