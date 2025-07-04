import axios from "axios";
import { Message } from "element-ui";
import router from "../router/index";

axios.default.withCredentials = true; // Allow cross-origin requests to include credentials
//创建 axious 实例
let service = axios.create({
  baseURL: "http://localhost:8080/api", // 远程服务器地址
  timeout: 5000, // Request timeout
});

//请求拦截器
service.interceptors.request.use(
  (config) => {
    // 在发送请求之前做些什么
    if (localStorage.getItem("token")) {
      config.headers = {
        Authorization: localStorage.getItem("token"),//携带权限参数
      }
    }
    return config;
  },
  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);

//响应拦截器
service.interceptors.response.use(
  (response) => {
    // 对响应数据做点什么
    const res = response.data;
    if (res.code === 10006) {
      Message({
        message: "登录已失效，请重新登录",
        type: "error",
      });
      setTimeout(() => {
        localStorage.removeItem("token");
        localStorage.removeItem("userInfo");
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