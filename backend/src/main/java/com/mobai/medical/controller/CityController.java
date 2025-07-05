package com.mobai.medical.controller;

import com.github.pagehelper.PageInfo;
import com.mobai.medical.model.CityModel;
import com.mobai.medical.service.CityService;
import com.mobai.medical.utils.Msg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "城市信息控制器")
@RestController
@RequestMapping("/api/citys")
@CrossOrigin
public class CityController {

  @Autowired
  CityService cityService;

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
}