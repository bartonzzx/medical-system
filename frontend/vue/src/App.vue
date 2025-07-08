<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: "app",
  mounted(){
  if(localStorage.getItem('userInfo')){
    this.$store.dispatch('app/setMenuList').then(() => {
      // 检查当前路由是否有效，路由重定向逻辑
      if(this.$route.name === null) {
        this.$router.replace('/') // 跳转到默认页面
      }
    });
  }
},
  
};
</script>