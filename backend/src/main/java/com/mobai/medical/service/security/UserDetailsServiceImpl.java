package com.mobai.medical.service.security;

import com.mobai.medical.mapper.AccountMapper;
import com.mobai.medical.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private AccountMapper accountMapper;

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    AccountModel accountModel = accountMapper.securityLogin(s);
    if (accountModel == null) {
      System.out.println("这里是UserDetailsServiceImpl.loadUserByUsername，用户不存在！");
      throw new UsernameNotFoundException("用户不存在！");
    }
    System.out.println("这里是UserDetailsServiceImpl.loadUserByUsername，正在打印登录用户信息！");
    System.out.println("用户名："+accountModel.getUsername()+'\n'+"用户密码："+accountModel.getPassword()+'\n'+"用户认证："+accountModel.getAuthorities()+"\n"+"用户电话号码："+accountModel.getPhoneNumber()+'\n'+"用户角色："+accountModel.getUtype()+'\n'+"用户的真实姓名："+accountModel.getRealname());
    String role = accountModel.getUtype();
    List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(role);
    return new AccountModel(accountModel.getId(), accountModel.getUname(),
            accountModel.getRealname(), role, accountModel.getPwd(), auths);
  }
}