<template>
  <el-container>
    <!-- 头部区域 -->
    <el-header height="76px">
      <h2>销售地点管理</h2>
      <!-- 面包屑导航区域 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>销售地点管理</el-breadcrumb-item>
      </el-breadcrumb>
    </el-header>

    <el-main>
      <div class="main-title">
        <h3>销售地点列表</h3>
        <button class="new-add" @click="addFormVisible = true" v-if="hasRole" />
      </div>

      <!-- 搜索 -->
      <el-row :gutter="20">
        <el-col :span="23" class="search-col">
          <keep-alive>
            <el-input placeholder="查询 (输入要查询的药店名称)" size="small" v-model="keyword" @input="handelQuery"></el-input>
          </keep-alive>
        </el-col>
      </el-row>

      <!-- 表格 -->
      <el-table stripe :default-sort="{ prop: 'date', order: 'descending' }" :data="tableData.list"
        highlight-current-row>
        <el-table-column prop="saleId" label="药店编号" sortable />
        <el-table-column prop="saleName" label="药店名称" />
        <el-table-column prop="salePhone" label="药店电话" />
        <el-table-column label="操作" v-if="hasRole">
          <template slot-scope="scope">
            <button class="table-btn-delete"
              @click="handleDeleteSalePlace(scope.row.saleId, scope.row.saleName)"></button>
            <button class="table-btn-update" @click="
              handleModifyFormVisible(
                scope.row.saleId,
                scope.row.saleName,
                scope.row.salePhone
              )
              " />
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <pagination :current-page.sync="currentPage" :layout="'total,prev,pager,next,jumper'" :total="tableData.total"
          :page-size.sync="pageSize" @currentChange="handleCurrentChange($event)" @update:page="currentPage = $event">
        </pagination>
      </div>
    </el-main>

    <!-- 新增弹窗 -->
    <el-dialog title="新增销售地点" :visible.sync="addFormVisible" :modal-append-to-body="false" @close="handleAddClose">
      <el-form :model="addForm" hide-required-asterisk ref="addForm" label-width="110px">
        <el-form-item label="药店名称" prop="saleName" :rules="rules.nameRules">
          <el-input v-model.trim="addForm.saleName" autocomplete="off" required></el-input>
        </el-form-item>
        <el-form-item label="药店电话" prop="salePhone" :rules="rules.phoneRules">
          <el-input v-model.number="addForm.salePhone" autocomplete="off" required></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddSalePlace('addForm')">确定</el-button>
      </div>
    </el-dialog>

    <!-- 修改弹窗 -->
    <el-dialog title="修改销售地点信息" :visible.sync="modifyFormVisible" :modal-append-to-body="false"
      @close="handleModifyClose">
      <el-form :model="modifyForm" hide-required-asterisk ref="modifyForm" label-width="110px">
        <el-form-item label="药店编号">
          <el-input v-model="modifyForm.saleId" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="药店名称" prop="saleName" :rules="rules.nameRules">
          <el-input v-model.trim="modifyForm.saleName" autocomplete="off" required></el-input>
        </el-form-item>
        <el-form-item label="药店电话" prop="salePhone" :rules="rules.phoneRules">
          <el-input v-model.number="modifyForm.salePhone" autocomplete="off" required></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleModifySalePlace('modifyForm')">确定</el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
import Pagination from "../../components/Pagination";
import { mapGetters } from "vuex";
import rules from "../../utils/validator";

export default {
  name: "SaleManage",
  components: {
    Pagination,
  },
  data() {
    return {
      currentPage: 1,
      pageSize: 5,
      keywordDefault: "",
      addFormVisible: false,
      addForm: {
        saleName: "",
        salePhone: "",
      },
      modifyFormVisible: false,
      modifyForm: {
        saleId: "",
        saleName: "",
        salePhone: "",
      },
      rules,
    };
  },
  methods: {
    // 获取销售地点信息
    getSalePlaceInfo() {
      this.$store.dispatch("saleInfoManage/getSalePlaceInfo", {
        pn: this.currentPage,
        size: this.pageSize,
      });
    },

    // 分页处理
    handleCurrentChange(event) {
      this.currentPage = event.page;
      if (this.keyword.length) {
        this.handelQuery(this.keyword);
      } else {
        this.getSalePlaceInfo();
      }
    },

    // 关键字查询
    handelQuery(keyword) {
      this.$store.dispatch("saleInfoManage/getSalePlaceInfo", {
        pn: this.currentPage,
        size: this.pageSize,
        keyword,
      });
    },

    // 新增销售地点
    handleAddSalePlace(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.addFormVisible = false;
          this.$store.dispatch("saleInfoManage/addSalePlace", {
            saleName: this.addForm.saleName,
            salePhone: this.addForm.salePhone,
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

    // 删除销售地点
    handleDeleteSalePlace(saleId, saleName) {
      this.$confirm(
        `确定要删除"${saleName}"的相关信息吗?`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          this.$store.dispatch("saleInfoManage/deleteSalePlace", {
            saleId,
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

    // 打开修改表单
    handleModifyFormVisible(saleId, saleName, salePhone) {
      this.modifyForm = {
        saleId,
        saleName,
        salePhone,
      };
      this.modifyFormVisible = true;
    },

    // 修改销售信息
    handleModifySalePlace(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.modifyFormVisible = false;
          this.$store.dispatch("saleInfoManage/modifySalePlaceInfo", {
            saleId: this.modifyForm.saleId,
            saleName: this.modifyForm.saleName,
            salePhone: this.modifyForm.salePhone,
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

    // 关闭表单处理
    handleAddClose() {
      this.addForm = {};
      this.$refs.addForm.resetFields();
    },
    handleModifyClose() {
      this.$refs.modifyForm.resetFields();
    },
  },
  mounted() {
    this.getSalePlaceInfo();
  },
  computed: {
    ...mapGetters({
      tableData: "salePlaceInfo",
    }),
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