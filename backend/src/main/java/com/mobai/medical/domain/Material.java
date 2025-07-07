package com.mobai.medical.domain;

import com.mobai.medical.domain.superdomain.SuperDomain;
import lombok.Data;

import java.util.Date;

@Data
public class Material extends SuperDomain {

  private Integer id;//材料id

  private String title;//材料标题

  private String message;//材料内容

  private Date createTime;//创建时间

  private Date updateTime;//更新时间

}