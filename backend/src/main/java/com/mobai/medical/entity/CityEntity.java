package com.mobai.medical.entity;

import com.mobai.medical.domain.City;
import lombok.Data;

@Data
public class CityEntity extends City {
  private Long total;
}