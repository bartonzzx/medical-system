package com.mobai.medical.domain.superdomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

// 用于给param包下的其他类继承pn和size以及keyword，用于分页查询
@Data
public class SuperDomain implements Serializable {
  private Long total; //总记录数
  @JsonIgnore
  private Integer pn = 1; //当前页
  @JsonIgnore
  private Integer size = Integer.MAX_VALUE; //每页大小
  @JsonIgnore
  private String keyword; //查询关键字

}