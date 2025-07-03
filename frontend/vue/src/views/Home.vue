<template>
  <div>
      <el-input v-model="data.input" style="width: 240px" placeholder="Please input" />{{data.input}}
      <div style="margin-bottom: 20px">
        <router-link to="/test">Test</router-link>
      </div>

      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="router.push('/manager/test')">push 跳转 Test 页面</el-button>
      </div>
      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="router.push({path:'/manager/test',query:{id:2,string:'你好'}})">路由传递参数id=2,string=你好</el-button>
      </div>
      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="router.replace('/123213')">前往404</el-button>
      </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import router from '@/router/index.js';


const data = reactive({
  input:'',
  

})

</script>