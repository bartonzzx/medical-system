import {
  judgeDeleteResult,
  judgeAddResult,
  judgeQueryResult,
  judgeModifyResult,
} from "../../utils/app";
import request from "../../utils/request";
// import Qs from 'qs'

// 查询医保政策信息 API
export function getMedicalPolicyInfo(params) {
  return request({
    url: `/medical_policys`,
    method: "GET",
    params, // 使用 params 传递查询参数
  }).then((res) => judgeQueryResult(res));
}

// 新增医保政策 API
export function addMedicalPolicy(cityId, title, updateTime, message) {
  return request({
    url: "/medical_policys",
    method: "POST",
    data: {
      cityId, // 城市 ID
      title,  // 政策标题
      updateTime, // 更新时间
      message, // 政策内容
    },
  }).then((res) => judgeAddResult(res));
}

// 删除医保政策 API
export function deleteMedicalPolicy(id) {
  return request({
    url: `/medical_policys/${id}`, // 使用路径参数传递ID
    method: "DELETE",
    data: {
      id, // 同时使用请求体传递ID（冗余设计，取决于后端要求）
    },
  }).then((res) => {
    judgeDeleteResult(res);
  });
}

// 修改医保政策 API（从第二张图提取）
export function modifyMedicalPolicyInfo(id, cityId, title, updateTime, message) {
  return request({
    url: `/medical_policys/${id}`, // 路径参数传递ID
    method: "PUT",
    data: {
      cityId,  // 城市 ID
      title,   // 政策标题
      updateTime, // 更新时间
      message, // 政策内容
    },
  }).then((res) => {
    judgeModifyResult(res);
  });
}