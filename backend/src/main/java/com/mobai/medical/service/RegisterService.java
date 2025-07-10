package com.mobai.medical.service;

import com.mobai.medical.entity.AccountEntity;
import com.mobai.medical.mapper.AccountMapper;
import com.mobai.medical.utils.Msg;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

  @Autowired
  AccountMapper accountMapper;

  public Msg register(AccountEntity ae) {
    ae.setRealname(ae.getUname());
    ae.setUname(ae.getUname());
    ae.setPwd(new BCryptPasswordEncoder().encode(ae.getPwd())); // 密码加密
    ae.setCreatetime(new DateTime().toDate());
    ae.setUpdatetime(new DateTime().toDate());
    ae.setUtype("ROLE_2");

    if (accountMapper.checkUname(ae.getUname()) > 0) {
      return Msg.fail().code(10001).mess("用户名已被使用");
    }

    if (accountMapper.checkRegisterPhone(ae.getPhoneNumber()) > 0) {
      return Msg.fail().code(10001).mess("手机号已被使用");
    }

    try {
      int i = accountMapper.register(ae); // 创建账户
      return Msg.success().mess("添加成功")
              .data("uName", ae.getUname());
    } catch (DuplicateKeyException e) {
      return Msg.fail().mess("该账号已经注册");
    }
  }

}