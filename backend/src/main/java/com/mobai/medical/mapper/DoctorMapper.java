package com.mobai.medical.mapper;

import com.mobai.medical.model.DoctorLevelModel;
import com.mobai.medical.model.DoctorModel;
import com.mobai.medical.model.TreatTypeModel;
import com.mobai.medical.param.DoctorParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DoctorMapper {
  List<DoctorModel> getAllDoctor(DoctorParam param);

  List<DoctorLevelModel> getAllLevel();

  List<TreatTypeModel> getAllTreatType();
}