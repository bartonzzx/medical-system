package com.mobai.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobai.medical.mapper.DrugMapper;
import com.mobai.medical.model.DrugModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrugService {

  @Autowired
  DrugMapper drugMapper;

  // 获取所有药品信息并分页，name不为空则模糊查询
  public PageInfo<DrugModel> getDrugWithPage(int pn, int size, String name) {
    PageHelper.startPage(pn, size);
    List<DrugModel> list = drugMapper.getAllDrug(name);
    // list.forEach();
    PageInfo<DrugModel> info = new PageInfo<>(list, 5);
    return info;
  }
}