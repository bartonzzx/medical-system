package com.mobai.medical.controller;

import com.github.pagehelper.PageInfo;
import com.mobai.medical.domain.Sale;
import com.mobai.medical.service.SaleService;
import com.mobai.medical.utils.Msg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "销售地点信息控制器")
@RestController
@RequestMapping("/api/sales")
public class SaleController {

  @Autowired
  SaleService saleService;

  // 销售地点信息的分页查询，name不为空则模糊查询，当路径为""时，查询不分页
  @GetMapping(value = {"/{pn}/{size}", ""})
  public Msg getSaleWithPage(@PathVariable(value = "pn", required = false) Integer pn,
                             @PathVariable(value = "size", required = false) Integer size,
                             @RequestParam(required = false) String name) {

    PageInfo<Sale> info = saleService.getSaleWithPage(pn, size, name);
    if (info != null) {
      return Msg.success().data("salePageInfo", info);
    }
    return Msg.fail();
  }

  // 根据id查询一个销售地点
  @GetMapping("{id}")
  public Msg getSaleById(@PathVariable("id") Integer id) {
    Msg msg = saleService.getSaleById(id);
    return msg;
  }

}