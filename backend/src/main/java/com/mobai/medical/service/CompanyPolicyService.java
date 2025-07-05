package com.mobai.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobai.medical.domain.CompanyPolicy;
import com.mobai.medical.domain.DrugCompany;
import com.mobai.medical.entity.CompanyPolicyEntity;
import com.mobai.medical.entity.DrugCompanyEntity;
import com.mobai.medical.mapper.CompanyPolicyMapper;
import com.mobai.medical.model.CompanyPolicyModel;
import com.mobai.medical.param.CompanyPolicyParam;
import com.mobai.medical.utils.Msg;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.List;

@Service
public class CompanyPolicyService {
  @Autowired
  private CompanyPolicyMapper companyPolicyMapper;

  public Msg getAllPolicyWithPage(CompanyPolicyParam param) {
    if (param.getSize() == 0) {
      param.setSize(1);
    }
    PageHelper.startPage(param.getPn(), param.getSize());
    List<CompanyPolicyModel> list = companyPolicyMapper.getAllPolicy(param);
    PageInfo<CompanyPolicyModel> info = new PageInfo<>(list, 5);
    return Msg.success().data("policyInfo", info);
  }

  public Msg saveCompanyPolicy(CompanyPolicy cp) {
    Date d = new Date();
    cp.setCreateTime(d);
    cp.setUpdateTime(d);

    CompanyPolicyEntity cpe = new CompanyPolicyEntity();
    BeanUtils.copyProperties(cp, cpe); // 对象拷贝

    int i = companyPolicyMapper.saveCompanyPolicy(cpe);
    if (i > 0) {
      Long num = cpe.getTotal() % 5 == 0 ?
              (cpe.getTotal() / 5) :
              (cpe.getTotal() / 5) + 1;
      return Msg.success().data("numberOfAdd", 1).data("pages", num).mess("添加成功");
    }
    return Msg.fail().mess("添加失败");
  }

  public Msg updataCompanyPolicy(Long id, CompanyPolicyEntity ce) {
    ce.setUpdateTime(new Date());
    ce.setId(id);
    // 注意这里给传入数据库的实体进行id赋值，和updateTime赋值，参考CompanyService也是一样的，当然，还是一步一步标断点调试过来！
    // System.out.println(ce.toString());
    int i = companyPolicyMapper.updateCompanyPolicy(ce);
    if (i > 0) {
      return Msg.success().mess("修改成功");
    } else {
      return Msg.fail().mess("修改失败");
    }
  }

  public Msg deleteCompanyPolicyById(Integer id) {
    int i = companyPolicyMapper.deleteByCompanyPolicyById(id);
    if (i > 0) {
      return Msg.success().mess("删除成功");
    } else {
      return Msg.fail().mess("删除失败");
    }
  }
}