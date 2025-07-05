package com.mobai.medical.mapper;

import com.mobai.medical.domain.City;
import com.mobai.medical.model.CityModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CityMapper {
  // 获取所有城市信息，name不为空则模糊查询
  List<CityModel> getAllCity(String name);

  // 根据id查找一个城市信息
  City getCityById(Integer id);
}