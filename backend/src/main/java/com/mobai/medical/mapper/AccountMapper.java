package com.mobai.medical.mapper;

import com.mobai.medical.entity.AccountEntity;
import com.mobai.medical.model.AccountModel;
import org.springframework.stereotype.Component;

@Component
public interface AccountMapper {
  // 用户登录security
  AccountModel securityLogin(String uname);

  int checkPhone(String phone);

  int checkPhoneExcludeAccount(String phone, Long excludeAccountId);

  int resetPwd(Long id, String pwd);

  int updateAccount(AccountEntity entity);

  // 注册账号
  int register(AccountEntity entity);

  // 检查用户名
  int checkUname(String uname);

  // 检查手机号
  int checkRegisterPhone(String phone);
}