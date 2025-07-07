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
      //打印调试
      console.log("已保存 userInfo = ", localStorage.getItem("userInfo"));
      this.$store.dispatch('app/setMenuList');
    }
  },
  
};
</script>