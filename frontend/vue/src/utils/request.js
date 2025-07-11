import axios from "axios";
import { Message } from "element-ui";
import router from "../router/index";

axios.defaults.withCredentials = false;
// 创建 axios 实例
let service = axios.create({
  // baseURL: "http://localhost:8080/api", //远程服务器地址
  baseURL: "https://backend.barton.lat:10241/api",
  timeout: 5000, //请求超时时间
});

// request拦截器
service.interceptors.request.use(
  (config) => {
    if (localStorage.getItem("token")) {
      config.headers = {
        Authorization: localStorage.getItem("token"), //携带权限参数
      };
    }
    return config;
  },
  (error) => {
    Promise.reject(error);
  }
);

//response拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data;
    // code为10006代表token失效，需要重新登录
    if (res.code == 10006) {
      Message({
        type: "error",
        message: "登录已失效，请重新登录",
      });
      setTimeout(() => {
        localStorage.removeItem("token");
        localStorage.removeItem("userInfo")
        router.push("/user/login");
      }, 500);
    }
    return response;
  },
  (err) => {
    return Promise.reject(err);
  }
);

export default service;