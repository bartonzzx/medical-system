<template>
  <!-- 必备材料管理 -->
  <el-container>
    <!-- 头部区域 -->
    <el-header height="76px">
      <h2 v-if="hasRole">必备材料管理</h2>
      <h2 v-else>必备材料查询</h2>

      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item v-if="hasRole">必备材料管理</el-breadcrumb-item>
        <el-breadcrumb-item v-else>必备材料查询</el-breadcrumb-item>
      </el-breadcrumb>
    </el-header>

    <!-- 主体内容区域 -->
    <el-main>
      <!-- 列表标题区域 -->
      <div class="main-title">
        <h3>必备材料列表</h3>
        <button class="new-add" @click="addFormVisible = true" v-if="hasRole"></button>
      </div>

      <!-- 搜索区域 -->
      <el-row :gutter="20">
        <el-col :span="23" class="search-col">
          <el-input placeholder="查询（输入要查询的关键字）" clearable size="small" v-model="keyword" @input="handelQuery" />
        </el-col>
      </el-row>

      <!-- 表格区域 -->
      <el-table :data="tableData.list" stripe :default-sort="{ prop: 'date', order: 'descending' }" max-height="375"
        highlight-current-row>
        <el-table-column prop="id" label="材料编号" sortable />
        <el-table-column prop="title" label="材料标题" />
        <el-table-column prop="message" label="材料内容" min-width="400" />
        <el-table-column label="操作" v-if="hasRole">
          <template slot-scope="scope">
            <button class="table-btn-delete" @click="handleDeleteMaterial(scope.row.id, scope.row.title)"></button>
            <button class="table-btn-update"
              @click="handleModifyFormVisible(scope.row.id, scope.row.message, scope.row.title)"></button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <div class="pagination">
        <pagination :current-page.sync="currentPage" :layout="'total,prev,pager,next,jumper'" :total="tableData.total"
          :page-size.sync="pageSize" @currentChange="handleCurrentChange($event)" @update:page="currentPage = $event" />
      </div>
    </el-main>

    <!-- 新增材料弹窗 -->
    <el-dialog title="新增必备材料" :visible.sync="addFormVisible" :modal-append-to-body="false" @close="handleAddClose">
      <el-form :model="addForm" hide-required-asterisk ref="addForm">
        <el-form-item label="材料标题" label-width="110px" prop="title" :rules="rules.nameRules">
          <el-input v-model.trim="addForm.title" autocomplete="off" required />
        </el-form-item>
        <el-form-item label="材料内容" label-width="110px" prop="message" :rules="rules.infoRules">
          <el-input v-model.trim="addForm.message" autocomplete="off" required />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddMaterial('addForm')">
          确定
        </el-button>
      </div>
    </el-dialog>

    <!-- 修改材料弹窗 -->
    <el-dialog title="修改必备材料信息" :visible.sync="modifyFormVisible" :modal-append-to-body="false"
      @close="handleModifyClose">
      <el-form :model="modifyForm" hide-required-asterisk ref="modifyForm" label-width="110px">
        <el-form-item label="材料标题" prop="title" :rules="rules.nameRules">
          <el-input v-model.trim="modifyForm.title" autocomplete="off" required />
        </el-form-item>
        <el-form-item label="材料内容" prop="message" :rules="rules.infoRules">
          <el-input v-model.trim="modifyForm.message" autocomplete="off" required />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleModifyMeterial('modifyForm')">
          确定
        </el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
import Pagination from "../../components/Pagination";
import { mapGetters } from "vuex";
import rules from "../../utils/validator";

export default {
  name: "",
  components: { Pagination },
  data() {
    return {
      currentPage: 1,
      pageSize: 5,
      keywordDefault: "",
      addFormVisible: false,
      addForm: { title: "", message: "" },
      modifyFormVisible: false,
      modifyForm: { id: "", message: "", title: "" },
      rules
    };
  },
  methods: {
    // 获取数据 & 分页处理
    getMaterialInfo() {
      this.$store.dispatch("materialInfoManage/getMaterialInfo", {
        pn: this.currentPage,
        size: this.pageSize
      });
    },

    handleCurrentChange(event) {
      this.currentPage = event.page;
      if (this.keyword.length) {
        this.handelQuery(this.keyword);
      } else {
        this.getMaterialInfo();
      }
    },

    handelQuery(keyword) {
      this.$store.dispatch("materialInfoManage/getMaterialInfo", {
        pn: this.currentPage,
        size: this.pageSize,
        keyword
      });
    },

    // 新增物料
    handleAddMaterial(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.addFormVisible = false;
          this.$store.dispatch("materialInfoManage/addMaterial", {
            title: this.addForm.title,
            message: this.addForm.message,
            size: this.pageSize
          });
          this.currentPage += 1;
        } else {
          this.$message({
            message: "请检查输入的内容是否合规",
            type: "warning"
          });
          return false;
        }
      });
    },

    // 删除物料
    handleDeleteMaterial(id, title) {
      this.$confirm(`确定要删除"${title}"的相关信息吗？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$store.dispatch("materialInfoManage/deleteMaterial", {
            id,
            pn: this.currentPage,
            size: this.pageSize,
            keyword: this.keyword
          });
        })
        .catch(() => {
          this.$message({ type: "info", message: "已取消删除" });
        });
    },

    // 修改控制
    handleModifyFormVisible(id, message, title) {
      this.modifyForm = { id, message, title };
      this.modifyFormVisible = true;
    },

    handleModifyMeterial(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.modifyFormVisible = false;
          this.$store.dispatch("materialInfoManage/modifyMaterial", {
            id: this.modifyForm.id,
            message: this.modifyForm.message,
            title: this.modifyForm.title,
            pn: this.currentPage,
            size: this.pageSize,
            keyword: this.keyword
          });
        } else {
          this.$message({
            message: "请检查输入的内容是否合规",
            type: "warning"
          });
          return false;
        }
      });
    },

    // 表单重置
    handleAddClose() {
      this.$refs.addForm.resetFields();
    },
    handleModifyClose() {
      this.$refs.modifyForm.clearValidate();
    }
  },
  mounted() {
    this.getMaterialInfo(); // 首次渲染获取数据
  },
  computed: {
    ...mapGetters({ tableData: "materialInfo" }), // Vuex 数据映射
    keyword: {
      get() { return this.keywordDefault; },
      set(val) { this.keywordDefault = val; }
    }
  }
};
</script>

<style lang="less" scoped>
@import "../../style/infoManage.less";
</style>