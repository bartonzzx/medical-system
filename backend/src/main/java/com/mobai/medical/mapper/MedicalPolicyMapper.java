package com.mobai.medical.mapper;

import com.mobai.medical.model.MedicalPolicyModel;
import com.mobai.medical.param.MedicalPolicyParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MedicalPolicyMapper {
  // 医保政策条件查询
  List<MedicalPolicyModel> getAllPolicy(MedicalPolicyParam param);
}