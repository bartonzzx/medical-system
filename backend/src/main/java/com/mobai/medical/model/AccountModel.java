package com.mobai.medical.model;

import com.mobai.medical.domain.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class AccountModel extends Account implements UserDetails {

  private String urealName; // 用户真实姓名
  private Collection<? extends GrantedAuthority> authorities; // 认证

  public AccountModel() {
  }

  public AccountModel(Long id, String uname, String realName, String role, String pwd, Collection<? extends GrantedAuthority> authorities) {
    this.setId(id);
    this.setUname(uname);
    this.setUtype(role);
    this.setUrealName(realName);
    this.setPwd(pwd);
    this.authorities = authorities;
  }

  public String getUrealName() {
    return urealName;
  }

  public void setUrealName(String urealName){
    this.urealName = urealName;
  }

  public void setAuthorities(Collection<? extends GrantedAuthority> authorities){
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return this.getPwd();
  }

  @Override
  public String getUsername() {
    return this.getUname();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}