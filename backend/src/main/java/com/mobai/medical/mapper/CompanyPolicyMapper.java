package com.mobai.medical.mapper;

import com.mobai.medical.entity.CompanyPolicyEntity;
import com.mobai.medical.model.CompanyPolicyModel;
import com.mobai.medical.param.CompanyPolicyParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CompanyPolicyMapper {
  List<CompanyPolicyModel> getAllPolicy(CompanyPolicyParam param);

  int savePolicy(CompanyPolicyEntity entity);

  int updatePolicy(CompanyPolicyEntity entity);

  // 删除医药公司政策
  int deletePolicy(Long id);

  int deletePolicyByCompany(Integer id);
}