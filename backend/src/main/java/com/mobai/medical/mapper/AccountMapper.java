package com.mobai.medical.mapper;

import com.mobai.medical.entity.AccountEntity;
import com.mobai.medical.model.AccountModel;
import org.springframework.stereotype.Component;

@Component
public interface AccountMapper {
  // 用户登录security
  AccountModel securityLogin(String uname);

  int checkPhone(String phone);

  int resetPwd(Long id, String pwd);

  int regist(AccountEntity entity);

  int updateAccount(AccountEntity entity);
}