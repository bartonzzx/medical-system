import {
  getDoctorInfo,
  getDoctorLevelAndType,
  addDoctor,
  deleteDoctor,
  modifyDoctor,
} from "../../api/admin/doctorInfoManage.js";

const state = {
  doctorInfo: {},
  doctorLevelAndType: {},
};

const mutations = {
  GET_DOCTOR_INFO(state, payload) {
    state.doctorInfo = payload;
  },
  GET_DOCTOR_LEVEL_AND_TYPE(state, payload) {
    state.doctorLevelAndType = payload;
  },
};

const actions = {
  // 医生信息分页，关键字查询
  getDoctorInfo({ commit }, { pn, size, keyword }) {
    getDoctorInfo(pn, size, keyword).then((res) => {
      if (res) {
        commit("GET_DOCTOR_INFO", res.data.data.doctorInfo);
      }
    });
  },

  // 查询医生级别及诊治部位
  getDoctorLevelAndType({ commit }) {
    getDoctorLevelAndType().then((res) => {
      commit("GET_DOCTOR_LEVEL_AND_TYPE", res.data.data);
    });
  },

  // 新增医生
  addDoctor(
    { dispatch },
    { age, levelId, name, phoneNumber, pwd, sex, typeId, size }
  ) {
    return addDoctor(age, levelId, name, phoneNumber, pwd, sex, typeId).then(
      (res) => {
        if (res.data.code == 10001) {
          return false;
        } else {
          // 新增后跳转到最后一页
          dispatch("getDoctorInfo", { pn: res.data.data.pages, size });
          return true;
        }
      }
    );
  },

  // 删除医生
  deleteDoctor({ dispatch }, { id, pn, size, keyword }) {
    deleteDoctor(id).then(() => {
      dispatch("getDoctorInfo", { pn, size, keyword });
    });
  },

  // 修改医生信息
  modifyDoctor(
    { dispatch },
    {
      accountId,
      age,
      levelId,
      name,
      phoneNumber,
      pwd,
      sex,
      typeId,
      id,
      pn,
      size,
      keyword,
    }
  ) {
    return modifyDoctor(
      accountId,
      age,
      levelId,
      name,
      phoneNumber,
      pwd,
      sex,
      typeId,
      id
    ).then((res) => {
      if (res.data.code == 10001) {
        return false;
      } else {
        dispatch("getDoctorInfo", { pn, size, keyword });
        return true;
      }
    });
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};