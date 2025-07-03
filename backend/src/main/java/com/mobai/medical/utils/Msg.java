package com.mobai.medical.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Msg {
  /*状态码*/
  private Integer code;
  /*响应信息*/
  private String message;
  /*响应是否成功标志*/
  private boolean success;
  /*响应数据*/
  private Map<String, Object> data = new HashMap<>();

  // 响应成功
  public static Msg success() {
    Msg msg = new Msg();
    msg.setCode(20000);
    msg.setMessage("响应成功");
    msg.setSuccess(true);
    return msg;
  }

  // 响应失败
  public static Msg fail() {
    Msg msg = new Msg();
    msg.setCode(10000);
    msg.setMessage("响应失败");
    msg.setSuccess(false);
    return msg;
  }

  // 添加响应数据
  public Msg data(String key, Object value) {
    this.data.put(key, value);
    return this;
  }

  // 修改状态码
  public Msg code(Integer code) {
    this.setCode(code);
    return this;
  }

  // 修改信息
  public Msg mess(String str) {
    this.setMessage(str);
    return this;
  }
}