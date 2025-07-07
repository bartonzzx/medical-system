package com.mobai.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobai.medical.domain.Sale;
import com.mobai.medical.mapper.SaleMapper;
import com.mobai.medical.utils.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

  @Autowired
  SaleMapper saleMapper;

  // 获取所有销售地点信息并分页，name不为空则模糊查询
  public PageInfo<Sale> getSaleWithPage(Integer pn, Integer size, String name) {
    if (pn == null && size == null) {
      pn = 1;
      size = 0;
    }
    PageHelper.startPage(pn, size);
    List<Sale> list = saleMapper.getAllSale(name);
    PageInfo<Sale> info = new PageInfo<>(list, 5);
    return info;
  }

  // 根据id获取对应销售地点信息
  public Msg getSaleById(Integer id) {
    Sale sale = saleMapper.getSaleById(id);
    if (sale == null) {
      return Msg.fail().mess("没有找到");
    }
    return Msg.success().data("sale", sale);
  }

}
