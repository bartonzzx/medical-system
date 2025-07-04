import Vue from "vue";
import VueRouter from "vue-router";
import Login from "../views/Login.vue";
import Home from "../views/Home/index.vue";
import Layout from "../layout/index.vue"

Vue.use(VueRouter);

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};

export const constantRoutes = [
  {
    path: "/user/login",
    name: "Login",
    component: Login,
    meta: { title: "登录" },
  },
  {
    path: "/",
    redirect: "/home",
  },
  {
    path: "/home",
    name: "Home",
    component: Layout,
    meta: { title: "首页" },
    children: [
      {
        path: "/home",
        name: "Home",
        component: () => import("../views/Home/index.vue"),
        meta: { title: "首页" },
      },
    ] 
  },

];

const router = new VueRouter({
  mode: "hash",
  routes: constantRoutes,
});

// 这是路由守卫，判断登录状态
router.beforeEach((to, from, next) => {
  document.title = to.meta.title;
  const token = localStorage.getItem("token");
  if (to.path === "/user/login" && token) {
    next("/");
  } else if (to.path!== "/user/login" &&!token) {
    next("/user/login");
  } else {
    next();
  }
});

export default router;