package com.mobai.medical.model;

import com.mobai.medical.domain.Account;
import lombok.Data;
import org.apache.catalina.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class AccountModel extends Account implements UserDetails {

  private String urealName; // 用户真实姓名
  private Collection<?extends GrantedAuthority> authorities; // 认证

  public AccountModel() {}

  public AccountModel(Long id, String uname,String realName,String role,String pwd,Collection<? extends GrantedAuthority> authorities) {
    this.setId(id);
    this.setUname(uname);
    this.setUtype(role);
    this.setUrealName(realName);
    this.setPwd(pwd);
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return "";
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}