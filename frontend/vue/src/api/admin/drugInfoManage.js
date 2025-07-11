import {
  judgeDeleteResult,
  judgeAddResult,
  judgeQueryResult,
  judgeModifyResult,
} from "../../utils/app";
import request from "../../utils/request";

// 查询药品信息
export function getDrugInfo(pn, size, keyword = "") {
  return request({
    url: `/drugs/${pn}/${size}`,
    method: "GET",
    params: { name: keyword },
  }).then((res) => judgeQueryResult(res));
}

// 新增药品
export function addDrug(drugName, drugInfo, drugEffect, drugImg, saleIds, drugPublisher) {
  return request({
    url: "/drugs",
    method: "POST",
    data: {
      drugName,
      drugInfo,
      drugEffect,
      drugImg,
      saleIds,  // 药店ID数组
      drugPublisher
    },
  }).then((res) => judgeAddResult(res));
}

// 删除药品
export function deleteDrug(drugId) {
  return request({
    url: `/drugs/${drugId}`,
    method: "DELETE",
  }).then((res) => judgeDeleteResult(res));
}

// 修改药品信息
export function modifyDrugInfo(drugId, drugName, drugInfo, drugEffect, drugImg, saleIds) {
  return request({
    url: `/drugs/${drugId}`,
    method: "PUT",
    data: {
      drugName,
      drugInfo,
      drugEffect,
      drugImg,
      saleIds
    },
  }).then((res) => judgeModifyResult(res));
}