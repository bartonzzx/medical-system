package com.mobai.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobai.medical.mapper.DoctorMapper;
import com.mobai.medical.model.DoctorLevelModel;
import com.mobai.medical.model.DoctorModel;
import com.mobai.medical.model.TreatTypeModel;
import com.mobai.medical.param.DoctorParam;
import com.mobai.medical.utils.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DoctorService {
  @Autowired
  private DoctorMapper doctorMapper;
  public Msg getDoctorWithPage(DoctorParam param) {
    if (param.getSize()==0){
      param.setSize(1);
    }
    PageHelper.startPage(param.getPn(),param.getSize());
    List<DoctorModel> list=doctorMapper.getAllDoctor(param);
    PageInfo<DoctorModel> info=new PageInfo<>(list,5);
    return Msg.success().data("doctorInfo",info);
  }
  public Msg getLevelAndType() {
    List<TreatTypeModel> allTreatType=doctorMapper.getAllTreatType();
    List<DoctorLevelModel> allLevel=doctorMapper.getAllLevel();
    return Msg.success().data("allTreatType",allTreatType).data("allLevel",allLevel);
  }
}