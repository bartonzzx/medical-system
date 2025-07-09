package com.mobai.medical.controller;

import com.github.pagehelper.PageInfo;
import com.mobai.medical.model.CityModel;
import com.mobai.medical.service.CityService;
import com.mobai.medical.utils.Msg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@Api(tags = "城市信息控制器")
@RestController
@RequestMapping("/api/citys")
@CrossOrigin
public class CityController {

  @Autowired
  CityService cityService;

  // 处理无路径变量的请求：/api/citys
  // 这里对下面那里的required = false不要误解，必须明确出来这个get方法(http://localhost:8080，不然就会"Method Not Allowed"
  @GetMapping
  public Msg getCityDefault(@RequestParam(required = false) String name) {
    return getCityWithPage(null, null, name); // 调用核心方法
  }

  @GetMapping(value = "/{pn}/{size}")
  public Msg getCityWithPage(@PathVariable(value = "pn", required = false) Integer pn,
                             @PathVariable(value = "size", required = false) Integer size,
                             @RequestParam(required = false) String name) {
    PageInfo<CityModel> info = cityService.getCityWithPage(pn, size, name);
    if (info != null) {
      return Msg.success().data("cityPageInfo", info);
    }
    return Msg.fail();
  }

  // 根据id查询一个城市信息
  @GetMapping("{id}")
  public Msg getCityById(@PathVariable("id") Integer id) {
    Msg msg = cityService.getCityById(id);
    return msg;
  }

  // 新增一个城市信息
  @RolesAllowed({"1"})
  @PostMapping(value = "")
  public Msg saveCity(Integer cityNumber) {
    if (cityService.checkCityByName(cityNumber) > 0) {
      return Msg.fail().mess("城市已经存在").code(10004);
    }
    return cityService.saveCity(cityNumber);
  }

  // 根据id删除城市
  @RolesAllowed({"1"})
  @DeleteMapping({"{id}"})
  public Msg deleteCityById(@PathVariable("id") Integer id) {
    Msg msg = cityService.deleteCityById(id);
    return msg;
  }
}
