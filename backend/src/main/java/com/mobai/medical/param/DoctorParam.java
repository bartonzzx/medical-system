package com.mobai.medical.param;

import com.mobai.medical.domain.Doctor;
import lombok.Data;

@Data
public class DoctorParam extends Doctor {
  private String pwd; // 新增医师密码
}