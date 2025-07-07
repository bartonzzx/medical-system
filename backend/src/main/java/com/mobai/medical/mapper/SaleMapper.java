package com.mobai.medical.mapper;

import com.mobai.medical.domain.Sale;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SaleMapper {

  // 获取所有销售地点信息，name不为空则模糊查询
  List<Sale> getAllSale(String name);

  // 根据id查找一个销售地点信息
  Sale getSaleById(Integer id);
}