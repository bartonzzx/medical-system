package com.mobai.medical.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Msg {
  private Integer code;
  private String message;
  private boolean success;
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

  // 修改响应码
  public Msg code(Integer code) {
    this.setCode(code);
    return this;
  }

  // 修改响应消息
  public Msg mess(String str) {
    this.setMessage(str);
    return this;
  }

// 单元测试需要方法
  private Map<String, Object> dataMap = new HashMap<>();

  public Object get(String key) {
    return dataMap.get(key);
  }


}