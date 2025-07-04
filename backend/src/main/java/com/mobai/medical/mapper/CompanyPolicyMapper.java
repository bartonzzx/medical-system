package com.mobai.medical.mapper;

import com.mobai.medical.model.CompanyPolicyModel;
import com.mobai.medical.param.CompanyPolicyParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CompanyPolicyMapper {
  List<CompanyPolicyModel> getAllPolicy(CompanyPolicyParam param);
}