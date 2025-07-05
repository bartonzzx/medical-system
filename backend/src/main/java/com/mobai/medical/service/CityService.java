package com.mobai.medical.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobai.medical.domain.City;
import com.mobai.medical.entity.CityEntity;
import com.mobai.medical.mapper.CityMapper;
import com.mobai.medical.model.CityModel;
import com.mobai.medical.utils.Msg;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CityService {

  @Autowired
  CityMapper cityMapper;

  // 获取所有城市信息并分页，name不为空则模糊查询，当pn和size为null，则整页查询
  public PageInfo<CityModel> getCityWithPage(Integer pn, Integer size, String name) {
    if (pn == null && size == null) {
      pn = 1;
      size = 0;
      // size=0 表示不进行实际数据分页，仅返回元信息（如总记录数）。
    }
    PageHelper.startPage(pn, size);
    List<CityModel> list = cityMapper.getAllCity(name);
    PageInfo<CityModel> info = new PageInfo<>(list, 5);
    return info;
  }

  // 根据id查找一个城市
  public Msg getCityById(Integer id) {
    City city = cityMapper.getCityById(id);
    if (city == null) {
      return Msg.fail().mess("没有找到");
    }
    return Msg.success().data("city", city);
  }

  // 添加一个城市
  public Msg saveCity(Integer cityNumber) {
    City city = new City();
    Date d = new Date();
    city.setCityNumber(cityNumber);
    city.setCreatetime(d);
    city.setUpdatetime(d);
    CityEntity ce = new CityEntity();
    BeanUtils.copyProperties(city, ce); //对象拷贝
    int i = cityMapper.saveCity(ce);
    if (i > 0) {
      Long num = ce.getTotal() % 5 == 0 ? (ce.getTotal() / 5) : (ce.getTotal() / 5) + 1;
      return Msg.success().data("pages", num).mess("添加成功");
    }
    return Msg.fail().mess("添加成功");
  }

  // 传入id，删除一个城市
  public Msg deleteCityById(Integer id) {
    int i = cityMapper.deleteCityById(id);
    if (i > 0) {
      return Msg.success().mess("删除成功");
    } else {
      return Msg.fail().mess("删除失败");
    }
  }

  // 传入id，检查城市名是否存在
  public int checkCityByName(Integer number) {
    return cityMapper.checkCityByName(number);
  }
}