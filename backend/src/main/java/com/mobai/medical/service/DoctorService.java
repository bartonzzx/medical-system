package com.mobai.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobai.medical.entity.AccountEntity;
import com.mobai.medical.entity.DoctorEntity;
import com.mobai.medical.mapper.AccountMapper;
import com.mobai.medical.mapper.DoctorMapper;
import com.mobai.medical.model.DoctorLevelModel;
import com.mobai.medical.model.DoctorModel;
import com.mobai.medical.model.TreatTypeModel;
import com.mobai.medical.param.DoctorParam;
import com.mobai.medical.utils.Msg;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DoctorService {
  @Autowired
  private DoctorMapper doctorMapper;
  @Autowired
  private AccountMapper accountMapper;

  // 1. 获取分页医生信息
  public Msg getDoctorWithPage(DoctorParam param) {
    if (param.getSize() == 0) param.setSize(1); // 分页大小默认值处理
    PageHelper.startPage(param.getPn(), param.getSize());
    List<DoctorModel> list = doctorMapper.getAllDoctor(param);
    PageInfo<DoctorModel> info = new PageInfo<>(list, 5);
    return Msg.success().data("doctorInfo", info);
  }

  // 2. 获取医生等级和治疗类型
  public Msg getLevelAndType() {
    List<TreatTypeModel> allTreatType = doctorMapper.getAllTreatType();
    List<DoctorLevelModel> allLevel = doctorMapper.getAllLevel();
    return Msg.success().data("allTreatType", allTreatType).data("allLevel", allLevel);
  }

  // 3. 新增医生（核心事务操作）
  public Msg saveDoctor(DoctorParam param) {
    // 账户信息处理
    AccountEntity aEntity = new AccountEntity();
    aEntity.setPhoneNumber(param.getPhoneNumber());
    aEntity.setUname(param.getName() + param.getPhoneNumber().substring(7)); // 用户名规则：姓名+手机后四位
    aEntity.setRealname(param.getName());
    aEntity.setPwd(new BCryptPasswordEncoder().encode(param.getPwd())); // 密码加密
    aEntity.setCreatetime(new DateTime().toDate());
    aEntity.setUpdatetime(new DateTime().toDate());
    aEntity.setUtype("ROLE_2"); // 医生角色标识

    // 手机号查重
    if (accountMapper.checkPhone(param.getPhoneNumber()) > 0) {
      return Msg.fail().code(10001).mess("手机号已被使用");
    }

    try {
      int i = accountMapper.register(aEntity); // 创建账户
      // 医生信息处理
      DoctorEntity de = new DoctorEntity();
      BeanUtils.copyProperties(param, de);
      de.setCreateTime(new DateTime().toDate());
      de.setUpdateTime(new DateTime().toDate());
      de.setAccountId(aEntity.getId()); // 关联账户ID

      int j = doctorMapper.saveDoctor(de); // 创建医生记录
      // 事务结果处理
      System.out.println("这里是新增医师，新增账户信息i=" + i + " 新增医师信息j=" + j);
      if (i > 0 && j > 0) {
        List<DoctorModel> allDoctor = doctorMapper.getAllDoctor(null);
        Long total = (long) allDoctor.size();
        Long pageNum = total % 5 == 0 ? (total / 5) : (total / 5) + 1; // 计算总页数
        return Msg.success().mess("添加成功")
                .data("pages", pageNum)
                .data("addData", param);
      }
    } catch (DuplicateKeyException e) {
      return Msg.fail().mess("该账号已经注册");
    }
    return Msg.fail().mess("添加失败");
  }

  // 4. 更新医生信息
  public Msg updateDoctor(Long id, DoctorParam param) {
    // 手机号冲突检查（排除当前账户）
    if (accountMapper.checkPhoneExcludeAccount(param.getPhoneNumber(), param.getAccountId()) > 0) {
      return Msg.fail().code(10001).mess("手机号已被使用");
    }
    // 更新账户信息
    AccountEntity ae = new AccountEntity();
    ae.setId(param.getAccountId());
    System.out.println("医师对应的accountId为：" + param.getAccountId());
    ae.setUpdatetime(new DateTime().toDate());
    ae.setUname(param.getName() + param.getPhoneNumber().substring(7));
    ae.setPhoneNumber(param.getPhoneNumber());
    int i = accountMapper.updateAccount(ae);
    // 更新医生信息
    DoctorEntity de = new DoctorEntity();
    BeanUtils.copyProperties(param, de);
    de.setId(id);
    de.setUpdateTime(new DateTime().toDate());
    int j = doctorMapper.updateDoctor(de);
    // 事务结果
    System.out.println("这里是修改医师，更新账户信息i=" + i + " 更新医生信息j=" + j);
    if (i > 0 && j > 0) {
      return Msg.success().mess("修改成功").data("updateData", param);
    }
    return Msg.fail().mess("修改失败");
  }

  // 5. 删除医生（级联删除账户）
  public Msg deleteDoctorById(Long id) {
    int i = doctorMapper.deleteDoctorById(id); // 返回值应表示影响的行数
    if (i >= 2) { // 预期删除医生+关联账户共2条记录
      return Msg.success().mess("删除成功")
              .data("deleteDocInfo", 1)
              .data("deleteDocAccount", 1);
    }
    return Msg.fail().mess("删除失败");
  }

  // 6. 密码重置
  public Msg resetPwd(Long id) {
    String newPwd = new BCryptPasswordEncoder().encode("123456"); // 默认密码加密
    int i = accountMapper.resetPwd(id, newPwd);
    System.out.println("这里是修改医师密码，修改返回值i=" + i);
    return i > 0 ?
            Msg.success().mess("重置成功") :
            Msg.fail().mess("重置失败");
  }
}