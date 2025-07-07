package com.mobai.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobai.medical.mapper.MaterialMapper;
import com.mobai.medical.model.MaterialModel;
import com.mobai.medical.param.MaterialParam;
import com.mobai.medical.utils.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

  @Autowired
  MaterialMapper materialMapper;

  // 分页、关键字查询必备材料信息
  public Msg getAllMaterialWithPage(MaterialParam param) {
    if (param.getSize() == 0) {
      param.setSize(1);
    }
    PageHelper.startPage(param.getPn(), param.getSize());
    List<MaterialModel> list = materialMapper.getAllMaterial(param);
    PageInfo<MaterialModel> info = new PageInfo<>(list, 5);
    return Msg.success().data("materialInfo", info);
  }
}