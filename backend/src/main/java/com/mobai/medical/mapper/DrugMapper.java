package com.mobai.medical.mapper;

import com.mobai.medical.model.DrugModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DrugMapper {

  // 查询所有药品同时，服装药品销售地点
  List<DrugModel> getAllDrug(String name);
}