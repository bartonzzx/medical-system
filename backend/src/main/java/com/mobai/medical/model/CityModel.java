package com.mobai.medical.model;

import com.mobai.medical.domain.City;
import lombok.Data;

@Data
public class CityModel extends City {
  private String province; // 省份
  private String city; // 市
}