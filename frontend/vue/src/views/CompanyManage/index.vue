<!-- 医药公司信息管理 -->
<template>
  <el-container>
    <!-- 头部区域 -->
    <el-header height="76px">
      <h2>医药公司信息管理</h2>
      <!-- 面包屑导航区域 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>医药公司信息管理</el-breadcrumb-item>
      </el-breadcrumb>
    </el-header>
    <!-- 主体内容区域 -->
    <el-main>
      <!-- header -->
      <div class="main-title">
        <h3>医药公司信息列表</h3>
        <button class="new-add" @click="addFormVisible = true" v-if="hasRole" />
      </div>
      <!-- 搜索 -->
      <el-row :gutter="20">
        <el-col :span="23" class="search-col">
          <keep-alive>
            <el-input placeholder="查询（输入要查询的公司名称）" size="small" v-model="keyword" @input="handelQuery">
            </el-input>
          </keep-alive>
        </el-col>
      </el-row>

      <!-- 表格 -->
      <el-table stripe :default-sort="{ prop: 'date', order: 'descending' }" :data="tableData.list"
        highlight-current-row>
        <el-table-column prop="companyId" label="医药公司编号" sortable>
        </el-table-column>
        <el-table-column prop="companyName" label="公司名称">
        </el-table-column>
        <el-table-column prop="companyPhone" label="公司电话">
        </el-table-column>
        <el-table-column prop="companyOperation" label="操作" v-if="hasRole">
          <!-- 通过slot-scope拿到对应行的数据 -->
          <template slot-scope="scope">
            <button class="table-btn-delete"
              @click="handleDeleteCompany(scope.row.companyId, scope.row.companyName)"></button>
            <button class="table-btn-update"
              @click="handleModifyFormVisible(scope.row.companyId, scope.row.companyName, scope.row.companyPhone)"></button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <div class="pagination">
        <pagination :current-page.sync="currentPage" :layout="'total,prev,pager,next,jumper'" :total="tableData.total"
          :page-size.sync="pageSize" @currentChange="handleCurrentChange($event)" @update:page="currentPage = $event">
        </pagination>
      </div>

      <!-- 点击新增后的弹窗 -->
      <el-dialog title="新增医药公司" :visible.sync="addFormVisible" :modal-append-to-body="false" @close="handleAddClose">
        <el-form :model="addForm" hide-required-asterisk ref="addForm" label-width="110px">
          <el-form-item label="公司名称" prop="companyName" :rules="rules.nameRules">
            <el-input v-model.trim="addForm.companyName" autocomplete="off" required></el-input>
          </el-form-item>
          <el-form-item label="公司电话" prop="companyPhone" :rules="rules.phoneRules">
            <el-input v-model.number="addForm.companyPhone" autocomplete="off" required></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addFormVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAddCompany('addForm')">确定</el-button>
        </div>
      </el-dialog>

      <!-- 点击修改后的弹窗 -->
      <el-dialog title="修改医药公司信息" :visible.sync="modifyFormVisible" :modal-append-to-body="false"
        @close="handleModifyClose">
        <el-form :model="modifyForm" hide-required-asterisk ref="modifyForm" label-width="110px">
          <el-form-item label="医药公司编号">
            <el-input v-model="modifyForm.companyId" autocomplete="off" disabled></el-input>
          </el-form-item>
          <el-form-item label="公司名称" prop="companyName" :rules="rules.nameRules">
            <el-input v-model.trim="modifyForm.companyName" autocomplete="off" required></el-input>
          </el-form-item>
          <el-form-item label="公司电话" prop="companyPhone" :rules="rules.phoneRules">
            <el-input v-model.number="modifyForm.companyPhone" autocomplete="off" required></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="modifyFormVisible = false">取消</el-button>
          <el-button type="primary" @click="handleModifyCompany('modifyForm')">确定</el-button>
        </div>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script>
import Pagination from "../../components/Pagination";
import { mapGetters } from "vuex";
import rules from "../../utils/validator";
export default {
  name: "CompanyManage",
  components: {
    Pagination,
  },
  data() {
    return {
      currentPage: 1,
      pageSize: 5, // 每页的数据条数
      keywordDefault: "",
      addFormVisible: false, // 控制新增公司页面的显示
      addForm: {
        companyName: "",
        companyPhone: "",
      },
      modifyFormVisible: false, // 控制修改信息页面的显示
      modifyForm: {
        companyId: "",
        companyName: "",
        companyPhone: "",
      },
      rules, // 封装好的表单验证
    };
  },
  methods: {
    // 切换分页及首次进入获取数据
    getCompanyInfo() {
      this.$store.dispatch("companyInfoManage/getCompanyInfo", {
        pn: this.currentPage,
        size: this.pageSize,
      });
    },
    //当前页改变时触发，跳转其他页
    handleCurrentChange(event) {
      this.currentPage = event.page;
      if (this.keyword.length) {
        this.handelQuery(this.keyword);
      } else {
        this.getCompanyInfo();
      }
    },

    // 通过关键字查询数据
    handelQuery(keyword) {
      this.$store.dispatch("companyInfoManage/getCompanyInfo", {
        pn: this.currentPage,
        size: this.pageSize,
        keyword,
      });
    },

    // 新增公司
    handleAddCompany(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.addFormVisible = false;
          this.$store.dispatch("companyInfoManage/addCompany", {
            companyName: this.addForm.companyName,
            companyPhone: this.addForm.companyPhone,
            size: this.pageSize,
          });
        } else {
          this.$message({
            message: "请检查输入的内容是否合规",
            type: "warning",
          });
          return false;
        }
      });
    },

    // 删除公司
    handleDeleteCompany(companyId, companyName) {
      this.$confirm(
        `确定要删除“${companyName}”的相关信息吗?该操作会同时删除对应的公司政策`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          this.$store.dispatch("companyInfoManage/deleteCompany", {
            companyId,
            pn: this.currentPage,
            size: this.pageSize,
            keyword: this.keyword,
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },

    // 控制修改公司信息的表单弹出
    handleModifyFormVisible(companyId, companyName, companyPhone) {
      this.modifyForm = {
        companyId,
        companyName,
        companyPhone,
      };
      this.modifyFormVisible = true;
    },

    // 修改公司信息
    handleModifyCompany(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.modifyFormVisible = false;
          this.$store.dispatch("companyInfoManage/modifyCompanyInfo", {
            companyId: this.modifyForm.companyId,
            companyName: this.modifyForm.companyName,
            companyPhone: this.modifyForm.companyPhone,
            pn: this.currentPage,
            size: this.pageSize,
            keyword: this.keyword,
          });
        } else {
          this.$message({
            message: "请检查输入的内容是否合规",
            type: "warning",
          });
          return false;
        }
      });
    },

    // 每次关闭表单的时候重置表单
    handleAddClose() {
      this.addForm = {};
      this.$refs.addForm.resetFields();
    },

    handleModifyClose() {
      this.$refs.modifyForm.clearValidate();
    }
  },

  // 当挂载时渲染默认数据
  mounted() {
    this.getCompanyInfo(); // 首次渲染
  },

  computed: {
    ...mapGetters({
      tableData: "companyInfo",
    }),
    // 后端返回的数据
    keyword: {
      get() {
        return this.keywordDefault;
      },
      set(val) {
        this.keywordDefault = val;
      },
    },
  },
};
</script>

<style lang="less" scoped>
@import "../../style/infoManage.less";
</style>