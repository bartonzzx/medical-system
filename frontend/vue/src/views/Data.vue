<template>
    <div>
        <div>
            <el-card>
            <el-input style="width: 240px; margin-right:5px" v-model="data.name" placeholder="请输入名称查询" prefix-icon="Search"></el-input>
            <el-button type="primary">查 询</el-button>
            <el-button type="success">重 置</el-button>
            </el-card>

            <el-card>
            <el-button type="primary">新 增</el-button>
            <el-button type="warning">批量删除</el-button>
            <el-button type="info">导入</el-button>
            <el-button type="defult">导出</el-button>
            </el-card>

            <el-card>
                <el-table :data="data.tableData" style="width: 100%">
                    <el-table-column prop="date" label="日期" />
                    <el-table-column prop="name" label="名称" />
                    <el-table-column prop="address" label="地址" />
                </el-table>

                <div style="margin-top: 10px">
                    <el-pagination
                        v-model:current-page="data.currentPage"
                        v-model:page-size="data.pageSize"
                        :page-size = "[5,10,15,20]"
                        background
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="data.total"
                        />
                </div>
            </el-card>

            <el-card>

            </el-card>
        </div>
    </div>

</template>

<script setup>
import { reactive } from "vue";
import {Search } from '@element-plus/icons-vue';
import router from '@/router/index.js';
import {ElButton} from 'element-plus';

const data = reactive({
    name: '',
    tableData : [
        {id : 1, date: '2025-01-01', name: '张三', address: '北京市朝阳区'},
        {id : 2, date: '2025-01-02', name: '李四', address: '上海市浦东新区'},
        {id : 3, date: '2025-01-03', name: '王五', address: '广州市天河区'},
        {id : 4, date: '2025-01-04', name: '赵六', address: '深圳市南山区'},
    ],
    currentPage: 1,
    pageSize: 10,
    total:47,
});

</script>
