import Vue from "vue";
import VueRouter from "vue-router";
import Login from "../views/Login.vue";
import Layout from "../layout/index.vue";

Vue.use(VueRouter);

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};

export const constantRoutes = [
  // development mode
  // {
  //   path: "/dev",
  //   name: "Dev",
  //   component: () => import("../views/agent/index.vue"),
  //   meta: { title: "dev" },
  // },
  {
    path: "/user/login",
    name: "Login",
    component: Login,
    meta: { title: "登录" },  
  },
  {
    path:"/",
    redirect: "/home",
  },
  {
    path: "/",
    component: Layout,
    children: [
      {
        path: "home",
        name: "Home",
        component: () => import("../views/Home/index.vue"),
        meta: { title: "首页" },
      },
      {
        path: "city-manage",
        name: "CityManage",
        component: () => import("../views/CityManage/index.vue"),
        meta: { title: "城市管理" },
      },
      {
        path: "company-manage",
        name: "CompanyManage",
        component: () => import("../views/CompanyManage/index.vue"),
        meta: { title: "公司管理" },
      },
      {
        path: "company-policy",
        name: "CompanyPolicy",
        component: () => import("../views/CompanyPolicy/index.vue"),
        meta: { title: "公司政策" },
      },
      {
        path: "doctor-manage",
        name: "DoctorManage",
        component: () => import("../views/DoctorManage/index.vue"),
        meta: { title: "医生管理" },
      },
      {
        path: "drug-manage",
        name: "DrugManage",
        component: () => import("../views/DrugManage/index.vue"),
        meta: { title: "药品管理" },
      },
      {
        path: "matarial-manage",
        name: "MatarialManage",
        component: () => import("../views/MatarialManage/index.vue"),
        meta: { title: "材料管理" },
      },
      {
        path: "medical-policy",
        name: "MedicalPolicy",
        component: () => import("../views/MedicalPolicy/index.vue"),
        meta: { title: "医疗政策" },
      },
      {
        path: "sale-manage",
        name: "SaleManage",
        component: () => import("../views/SaleManage/index.vue"),
        meta: { title: "销售管理" },
      },
    ],
  },
  {
    path:"/user/register",
    component: () => import("../views/Register.vue"),
    name: "Register",
    meta: { title: "用户注册" },
  },
];

const router = new VueRouter({
  //  TODO: 替换为history模式路由 
  mode: "hash",
  routes: constantRoutes,
});

// 这是路由守卫，判断登录状态
router.beforeEach((to, from, next) => {
  document.title = to.meta.title;
  const token = localStorage.getItem("token");
  if (to.path == "/user/login" && token) {
    next("/");
  } else if (to.path !== "/user/login" && !token) {
    if(to.path == "/user/register"){
      next();
    }else{
      next("/user/login");
    }
  } else {
    next();
  }
});

export default router;