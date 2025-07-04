package com.mobai.medical.mapper;

import com.mobai.medical.domain.DrugCompany;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CompanyMapper {
  // 获取所有医药公司信息，name不为空则模糊查询
  List<DrugCompany> getAllCompany(String name);

  // 根据id查找一个医药公司信息
  DrugCompany getCompanyById(Integer id);
}
