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
      this.$store.dispatch('app/getMenuList');
    }
  },
  
};
</script>