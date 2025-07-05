package com.mobai.medical.mapper;

import com.mobai.medical.domain.DrugCompany;
import com.mobai.medical.entity.DrugCompanyEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CompanyMapper {
  List<DrugCompany> getAllCompany(String name);

  DrugCompany getCompanyById(Integer id);

  int saveCompany(DrugCompanyEntity company);

  int deleteCompanyById(Integer id);

  int updateCompanyById(DrugCompany company);
}