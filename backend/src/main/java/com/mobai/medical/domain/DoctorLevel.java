package com.mobai.medical.domain;

import com.mobai.medical.domain.superdomain.SuperDomain;
import lombok.Data;

@Data
public class DoctorLevel extends SuperDomain {
  private Long id; // 级别id
  private String name; // 级别名称
}