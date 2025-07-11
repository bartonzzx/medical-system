<template>
  <el-container>
    <el-header height="76px">
      <h2>城市信息管理</h2>
      <!-- 面包屑导航区域 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>城市信息管理</el-breadcrumb-item>
      </el-breadcrumb>
    </el-header>

    <el-main>
      <div class="main-title">
        <h3>城市列表</h3>
        <button class="new-add" @click="addFormVisible = true" v-if="hasRole"></button>
      </div>

      <!-- 搜索 -->
      <el-row :gutter="20">
        <el-col :span="23" class="search-col">
          <keep-alive>
            <el-input placeholder="查询(输入要查询的城市或省份)" size="small" v-model="keyword" @input="handelQuery"></el-input>
          </keep-alive>
        </el-col>
      </el-row>

      <!-- 表格 -->
      <el-table stripe :default-sort="{ prop: 'date', order: 'descending' }" :data="tableData.list"
        highlight-current-row>
        <el-table-column prop="cityNumber" label="城市编号" sortable />
        <el-table-column prop="province" label="所属省" />
        <el-table-column prop="city" label="城市名称" />
        <el-table-column prop="cityOperation" label="操作" v-if="hasRole">
          <template slot-scope="scope">
            <button class="table-btn-delete" @click="handleDeletecity(scope.row.cityId, scope.row.city)"></button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <pagination :current-page.sync="currentPage" :layout="'total,prev,pager,next,jumper'" :total="tableData.total"
          :page-size.sync="pageSize" @currentChange="handleCurrentChange($event)" @update:page="currentPage = $event">
        </pagination>
      </div>
    </el-main>

    <!-- 新增城市对话框 -->
    <el-dialog title="新增城市" :visible.sync="addFormVisible" :modal-append-to-body="false" @close="handleAddClose">
      <el-form :model="addForm" hide-required-asterisk ref="addForm">
        <el-form-item label="城市名称" :label-width="formLabelWidth" prop="cityNumber" :rules="rules.requiredRules">
          <el-cascader size="large" :options="options" v-model="addForm.cityNumber"></el-cascader>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddcity('addForm')">确定</el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
import Pagination from "../../components/Pagination";
import { mapGetters } from "vuex";
import rules from "../../utils/validator";
import { provinceAndCityData } from "element-china-area-data";

export default {
  name: "CityManage",
  components: { Pagination },
  data() {
    return {
      currentPage: 1,
      pageSize: 5,
      keywordDefault: "",
      addFormVisible: false,
      addForm: { cityNumber: [] },
      formLabelWidth: "110px",
      rules,
      options: provinceAndCityData
    };
  },
  computed: {
    ...mapGetters({ tableData: "cityInfo" }),
    keyword: {
      get() { return this.keywordDefault; },
      set(val) { this.keywordDefault = val; }
    }
  },
  methods: {
    // 获取城市信息
    getCityInfo() {
      this.$store.dispatch("cityInfoManage/getCityInfo", {
        pn: this.currentPage,
        size: this.pageSize
      });
    },

    // 分页处理
    handleCurrentChange(event) {
      this.currentPage = event.page;
      this.keyword ? this.handelQuery(this.keyword) : this.getCityInfo();
    },

    // 关键词搜索
    handelQuery(keyword) {
      this.$store.dispatch("cityInfoManage/getCityInfo", {
        pn: this.currentPage,
        size: this.pageSize,
        keyword
      });
    },

    // 新增城市
    handleAddcity(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.addFormVisible = false;
          this.$store.dispatch("cityInfoManage/addCity", {
            cityNumber: this.addForm.cityNumber[1],
            size: this.pageSize
          });
        } else {
          this.$message({ message: "请检查输入的内容是否合规", type: "warning" });
          return false;
        }
      });
    },

    // 删除城市
    handleDeletecity(cityId, cityName) {
      this.$confirm(`确定要删除${cityName}的相关信息吗?该操作会同时删除该城市相关的医保政策`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$store.dispatch("cityInfoManage/deleteCity", {
            cityId,
            pn: this.currentPage,
            size: this.pageSize,
            keyword: this.keyword
          });
        })
        .catch(() => {
          this.$message({ type: "info", message: "已取消删除" });
        });
    },

    // 重置表单
    handleAddClose() {
      this.$refs.addForm.resetFields();
    }
  },
  mounted() {
    this.getCityInfo(); // 首次渲染
  }
};
</script>

<style lang="less" scoped>
@import "../../style/infoManage.less";
</style>