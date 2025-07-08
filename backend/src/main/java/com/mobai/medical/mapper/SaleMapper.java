package com.mobai.medical.mapper;

import com.mobai.medical.domain.Sale;
import com.mobai.medical.entity.SaleEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SaleMapper {

  // 获取所有销售地点信息，name不为空则模糊查询
  List<Sale> getAllSale(String name);

  // 根据id查找一个销售地点信息
  Sale getSaleById(Integer id);

  // 添加一个销售地点
  int saveSale(SaleEntity sale);

  // 根据id删除销售地点信息
  int deleteSaleById(Integer id);

  // 根据id更新销售地点数据
  int updateSaleById(Sale sale);
}