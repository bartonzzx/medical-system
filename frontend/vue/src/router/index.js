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
  //   component: () => import("../views/Home/index.vue"),
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
    next("/user/login");
  } else {
    next();
  }
});
// router.beforeEach((to, from, next) => {
//   // 设置页面标题
//   if (to.meta && to.meta.title) {
//     document.title = to.meta.title;
//   }
  
//   const token = localStorage.getItem("token");
  
//   // 定义不需要登录验证的页面
//   const publicPages = ["/user/login", "/dev"];
//   const isPublicPage = publicPages.includes(to.path);
  
//   // 如果要去登录页面但已经有token，重定向到首页
//   if (to.path === "/user/login" && token) {
//     next("/");
//   } 
//   // 如果要去需要登录的页面但没有token，重定向到登录页
//   else if (!isPublicPage && !token) {
//     next("/user/login");
//   } 
//   // 其他情况正常放行
//   else {
//     next();
//   }
// });

export default router;