package com.mobai.medical.controller;

import com.github.pagehelper.PageInfo;
import com.mobai.medical.model.DrugModel;
import com.mobai.medical.param.DrugParam;
import com.mobai.medical.service.DrugService;
import com.mobai.medical.utils.Msg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Date;

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

  // 新增药品信息
  @RolesAllowed({"1", "2"})
  @PostMapping("")
  public Msg saveDrug(@RequestBody DrugParam drugParam) {
    drugParam.setCreatetime(new Date());
    drugParam.setUpdatetime(new Date());
    Msg msg = drugService.saveDrug(drugParam);
    return msg;
  }

  // 更新药品信息
  @RolesAllowed({"1"})
  @PutMapping("/{id}")
  public Msg updateDrug(@PathVariable("id") Long id, @RequestBody DrugParam drugParam) {
    return drugService.updateDrug(id, drugParam);
  }

  // 根据id删除药品信息以及药品-药店关联表的信息
  @RolesAllowed({"1"})
  @DeleteMapping("/{drugId}")
  public Msg deleteDrug(@PathVariable("drugId") Long drugId) {
    return drugService.deleteDrug(drugId);
  }

}