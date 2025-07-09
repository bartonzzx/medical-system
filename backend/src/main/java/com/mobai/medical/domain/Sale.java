package com.mobai.medical.domain;

import com.mobai.medical.domain.superdomain.SuperDomain;
import lombok.Data;

import java.util.Date;

@Data
public class Sale extends SuperDomain {

  private Long saleId; // 销售地点id

  private String saleName; // 销售地点名

  private String salePhone; // 销售地点电话

  private Date createtime; // 创建时间

  private Date updatetime; // 修改时间

  private Double Lng; // 新增经纬度

  private Double lat; // 新增经纬度

  private String address; //新增地址
}