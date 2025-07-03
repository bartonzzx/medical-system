<template>
    <div style="text-align: center;">
       <img src="@/assets/404.png" alt="404 Image">
       <div>
        <el-button type="primary" @click="goHome">返回首页</el-button>
       </div>
    </div>
</template>

<script setup>

import router from '@/router/index.js'; // 导入路由实例

const goHome=()=>{
    // 使用路由跳转到首页
    router.push('/manager/home');
}

</script>
