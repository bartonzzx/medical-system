package com.mobai.medical.domain;

import com.mobai.medical.domain.superdomain.SuperDomain;
import lombok.Data;

import java.util.Date;

@Data
public class Account extends SuperDomain {

  private Long id;
  private String uname; // 用户名
  private String pwd;
  private String phoneNumber;
  private String utype;  //取值：ROLE_1：管理员、ROLE_2：医生、ROLE_3：患者
  private Date updatetime;
  private Date createtime;
  private String realname; //真实姓名

  // 注意加上@Data启用Lombok

  @Override
  public String toString() {
    return "Account{" +
            "id=" + id +
            ", uname='" + uname + '\'' +
            ", pwd='" + pwd + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", utype='" + utype + '\'' +
            ", updatetime=" + updatetime +
            ", createtime=" + createtime +
            ", realname='" + realname + '\'' +
            '}';
  }
}