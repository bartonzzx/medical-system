<template>

    <div>
        <!-- 头部开始 -->
        <div style="height:60px;background-color:#3c7fff;display:flex;align-items: center">
            <div style="width: 200px;display:flex;align-items:center;font-size: 20px;color:white"><el-icon><Shop /></el-icon>  智慧医疗系统</div>
            <div style="flex:1;"></div>
            <div style="width:150px;display:flex;align-items:center;padding-right: 10px;">
                <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt=""
                style="width: 40px;height:40px">
                <span style="color :white;margin-left: 5px;">小鹏</span>
            </div>
        </div>
        <!-- 头部结束 -->

        <!-- 下面部分开始 -->
        <div style="display :flex">

            <!-- 左边导航区域 -->
            <div style="width:200px; border-right:1px soild #ddd; min-height:calc(100vh - 60px)">
                <!-- 插入页面路由 -->
                <el-menu router :default-active="router.currentRoute.value.path" style="border: 0 "> 
                <el-menu-item index="/manager/home"><el-icon><House /></el-icon> 系统首页</el-menu-item>
                <el-menu-item index="/manager/data"><el-icon><DataAnalysis /></el-icon>数据统计</el-menu-item>
                <el-sub-menu index="1">
                <template #title>
                    <el-icon><User /></el-icon>
                    <span>用户管理</span>
                </template>
                    <el-menu-item index="1-1">管理员</el-menu-item>
                    <el-menu-item index="1-2">医生</el-menu-item>
                </el-sub-menu>
                <el-menu-item index="/manager/test"><el-icon><BellFilled /></el-icon>数据渲染</el-menu-item>
                </el-menu>
            </div>
            <!-- 左侧结束 -->

            <!-- 右侧主体区域开始 -->
            <div style="flex:1 ; width: 0; background-color: #f8f8ff;">
                <RouterView />
            </div>
            <!-- 右侧结束 -->
        </div>
        <!-- 下面部分结束 -->
    </div>
</template>

<script setup>
import { ElMenu, ElMenuItem, ElSubMenu, ElIcon} from 'element-plus';
import { DataAnalysis, User } from '@element-plus/icons-vue';
import { House } from '@element-plus/icons-vue';
import { RouterView } from 'vue-router';
import router from '@/router/index.js';

</script>
