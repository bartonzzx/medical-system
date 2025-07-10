import Vue from "vue";
import VueRouter from "vue-router";
import LR from "../views/LR.vue"
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";


Vue.use(VueRouter);

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};

export const constantRoutes = [
  {
    path: "/user/",
    name: "LR",
    component: LR,
    meta: { title: "登录&注册" },
    children: [
      {
        path: "login",
        name: "Login",
        component: Login,
        meta: { title: "登录" },
      },
      {
        path: "register",
        name: "Register",
        component: Register,
        meta: { title: "注册" },
      },
    ],
  },
  {
    path: "/",
    redirect: "/home",
  },
];

const router = new VueRouter({
  mode: "hash",
  base: "/", // 明确设置基础路径
  routes: constantRoutes
});

const whiteList = ['/user/login', '/user/register'];

// 判断登录状态
router.beforeEach((to, from, next) => {
  document.title = to.meta.title;
  const token = localStorage.getItem("token");
  if (to.path == "/user/login" && token) {
    next("/");
  } else if (to.path !== "/user/login" && !token) {
    if (whiteList.includes(to.path)) {
      next();
    }
    else {
      next("/user/login");
    }
  } else {
    next();
  }
});

export default router;