package com.mobai.medical.mapper;

import com.mobai.medical.entity.MaterialEntity;
import com.mobai.medical.model.MaterialModel;
import com.mobai.medical.param.MaterialParam;
import org.springframework.stereotype.Component;

import java.util.List;

// mapper包 - 数据访问接口
@Component
public interface MaterialMapper {

  // 查询所有的材料
  List<MaterialModel> getAllMaterial(MaterialParam param);

  // 新增材料
  int saveMaterial(MaterialEntity entity);

  // 更新材料
  int updateMaterial(MaterialEntity entity);

  // 删除材料
  int deleteMaterial(Long id);
}