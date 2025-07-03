import { createRouter, createWebHistory } from 'vue-router'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path:'/',redirect: '/manager/home', },
    {
      path:'/manager',
      name:'manager',
      component: () => import('../views/Manager.vue'),
      children:[
        {
          path: 'home',//注意嵌套的不用加斜杠
          name: 'home',
          meta: {
            title: '首页' //title添加标题
          },
          component: () => import('../views/Home.vue'),
        },
        {
          path:'test',
          name:'test',
          meta: {
            title: '测试页面'
          },
          component: () => import('../views/Test.vue'),
        },
        {
          path:'data',
          name:'data',
          meta: {
            title: '数据管理'
          },
          component: () => import('../views/Data.vue'),
        },
      ]
    },
    {
      path:'/404',
      name:'NotFound',
      meta: {
        title: '404 Not Found'
      },
      component: () => import('../views/404.vue'),
    },
    {
      path: '/:pathMatch(.*)*', // 捕获所有未匹配的路径
      redirect: '/404', // 重定向到 404 页面
      meta: {
        title: '404 Not Found'
      },
    },
  ],
})
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title
  next() // 确保一定要调用 next() 方法来继续路由导航
})

export default router
