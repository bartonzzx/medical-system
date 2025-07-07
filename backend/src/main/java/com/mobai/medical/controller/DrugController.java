package com.mobai.medical.controller;

import com.github.pagehelper.PageInfo;
import com.mobai.medical.model.DrugModel;
import com.mobai.medical.service.DrugService;
import com.mobai.medical.utils.Msg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "药品控制器类")
@RestController
@RequestMapping("/api/drugs")
@CrossOrigin
public class DrugController {

  @Autowired
  private DrugService drugService;

  // 药品信息的分页查询，name不为空则模糊查询
  @GetMapping("/{pn}/{size}")
  public Msg getDrugWithPage(@PathVariable("pn") int pn,
                             @PathVariable("size") int size,
                             @RequestParam(required = false) String name) {

    PageInfo<DrugModel> info = drugService.getDrugWithPage(pn, size, name);
    if (info != null) {
      return Msg.success().data("drugPageInfo", info);
    }
    return Msg.fail();
  }

}