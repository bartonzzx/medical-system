package com.mobai.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobai.medical.domain.DrugCompany;
import com.mobai.medical.entity.DrugCompanyEntity;
import com.mobai.medical.mapper.CompanyMapper;
import com.mobai.medical.utils.Msg;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class CompanyService {

  @Autowired
  private CompanyMapper companyMapper;

  public PageInfo<DrugCompany> getCompanyWithPage(Integer pn, Integer size, String name) {
    if (pn == null && size == null) {
      pn = 1;
      size = Integer.MAX_VALUE;
    }
    if (pn == null) {
      pn = 1;
    }
    if (size == null) {
      size = Integer.MAX_VALUE;
    }
    if (size == 0) {
      size = 1;
    }
    PageHelper.startPage(pn, size);
    List<DrugCompany> list = companyMapper.getAllCompany(name);
    PageInfo<DrugCompany> info = new PageInfo<>(list, 5);
    return info;
  }

  public Msg getCompanyById(Integer id) {
    DrugCompany company = companyMapper.getCompanyById(id);

    if (company == null) {
      return Msg.fail().mess("没有找到");
    }
    return Msg.success().data("company", company);
  }

  public Msg saveCompany(DrugCompany company) {
    Date d = new Date();
    company.setCreatetime(d);
    company.setUpdatetime(d);

    DrugCompanyEntity dce = new DrugCompanyEntity();
    BeanUtils.copyProperties(company, dce); // 对象拷贝

    int i = companyMapper.saveCompany(dce);
    if (i > 0) {
      Long num = dce.getTotal() % 5 == 0 ?
              (dce.getTotal() / 5) :
              (dce.getTotal() / 5) + 1;
      return Msg.success().data("pages", num).mess("添加成功");
    }
    return Msg.fail().mess("添加失败");
  }

  public Msg updateCompanyById(Long id, DrugCompany company) {
    company.setUpdatetime(new Date());
    company.setCompanyId(id);
    int i = companyMapper.updateCompanyById(company);
    if (i > 0) {
      return Msg.success().mess("修改成功");
    }
    return Msg.fail().mess("修改失败");
  }

  public Msg deleteCompanyById(Integer id) {
    int i = companyMapper.deleteCompanyById(id);
    if (i > 0) {
      return Msg.success().mess("删除成功");
    } else {
      return Msg.fail().mess("删除失败");
    }
  }
}