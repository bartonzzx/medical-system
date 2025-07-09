package com.mobai.medical.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mobai.medical.domain.DrugCompany;
import com.mobai.medical.domain.Sale;
import com.mobai.medical.mapper.CompanyMapper;
import com.mobai.medical.mapper.DoctorMapper;
import com.mobai.medical.mapper.DrugMapper;
import com.mobai.medical.mapper.SaleMapper;
import com.mobai.medical.model.*;
import com.mobai.medical.param.DoctorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class DashboardService {
  @Autowired
  DoctorMapper doctorMapper;
  @Autowired
  DrugMapper drugMapper;
  @Autowired
  CompanyMapper companyMapper;
  @Autowired
  SaleMapper saleMapper;
  @Autowired
  MaterialService materialService;
  @Autowired
  CompanyPolicyService companyPolicyService;

  public ObjectNode getDashboardData() {
    List<DoctorModel> allDoctor = doctorMapper.getAllDoctor(new DoctorParam());
    // 获取控制面板药品、医师、药店和公司数量
    List<DrugModel> allDrug = drugMapper.getAllDrug("");
    List<DrugCompany> allCompany = companyMapper.getAllCompany("");
    List<Sale> allSale = saleMapper.getAllSale("");

    int doctorNumb = allDoctor.size();
    int drugNumb = allDrug.size();
    int companyNumb = allCompany.size();
    int saleNumb = allSale.size();
    int docLevel1 = 0, docLevel2 = 0, docLevel3 = 0;

    List<TreatTypeModel> allTreatType = doctorMapper.getAllTreatType();
    HashMap<String, Integer> treatMap = new HashMap<>();
    // 构建Map获取所有的诊治类型
    for (TreatTypeModel treat : allTreatType) {
      treatMap.put(treat.getName(), 0);
    }

    // 统计医生级别和诊治类型
    for (DoctorModel doc : allDoctor) {
      if (doc.getLevelId() == 1) {
        docLevel1++;
      } else if (doc.getLevelId() == 2) {
        docLevel2++;
      } else if (doc.getLevelId() == 3) {
        docLevel3++;
      }
      treatMap.put(doc.getTreatType(), treatMap.get(doc.getTreatType()) + 1);  // 修正了原文的括号错误
    }

    List<MaterialModel> allMaterial = materialService.getFirstMaterialWithPage();
    List<CompanyPolicyModel> allPolicy = companyPolicyService.getFirstPolicyWithPage();
    ObjectMapper objectMapper = new ObjectMapper();

    // 构建JSON响应
    ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.put("doctorNumb", doctorNumb);
    objectNode.put("drugNumb", drugNumb);
    objectNode.put("companyNumb", companyNumb);
    objectNode.put("saleNumb", saleNumb);

    ObjectNode docLevelNode = objectMapper.createObjectNode();
    docLevelNode.put("l1", docLevel1);
    docLevelNode.put("l2", docLevel2);
    docLevelNode.put("l3", docLevel3);
    objectNode.put("docLevel", docLevelNode);

    JsonNode treatNode = objectMapper.convertValue(treatMap, JsonNode.class);
    objectNode.put("treatMap", treatNode);

    JsonNode materialNode = objectMapper.valueToTree(allMaterial);
    JsonNode policyNode = objectMapper.valueToTree(allPolicy);
    objectNode.put("materials", materialNode);
    objectNode.put("policys", policyNode);

    return objectNode;
  }
}