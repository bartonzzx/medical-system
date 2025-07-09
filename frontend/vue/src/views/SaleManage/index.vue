<!-- 医药公司信息管理 -->
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
    <!-- 主体内容区域 -->
    <el-main>
      <el-switch
        active-text="销售地点地图展示"
        v-model="visualization"
        active-color="#13ce66"
        inactive-color="#bdc3c7"
      >
      </el-switch>
      <div v-show="!visualization">
        <div class="main-title">
          <h3>销售地点列表</h3>
          <!-- 按钮注释掉了，可根据实际需求恢复 -->
          <!-- <button
            class="new-add"
            @click="addFormVisible = true"
            v-if="hasRole"
          /> -->
        </div>
        <!-- 搜索 -->
        <el-row :gutter="20">
          <el-col :span="20" class="search-col">
            <keep-alive>
              <el-input
                placeholder="查询（输入要查询的药店名称）"
                size="small"
                v-model="keyword"
                @input="handleQuery"
              >
              </el-input>
            </keep-alive>
          </el-col>
        </el-row>
        <!-- 表格 -->
        <el-table
          stripe
          :default-sort="{ prop: 'date', order: 'descending' }"
          :data="tableData.list"
          highlight-current-row
        >
          <el-table-column prop="saleId" label="药店编号" sortable />
          <el-table-column prop="saleName" label="药店名称" />
          <el-table-column prop="salePhone" label="药店电话" />
          <el-table-column prop="address" label="药店地址" />
          <el-table-column label="操作" v-if="hasRole">
            <!-- 通过slot-scope拿到对应行的数据 -->
            <template slot-scope="scope">
              <button
                class="table-btn-delete"
                @click="
                  handleDeleteSalePlace(scope.row.saleId, scope.row.saleName)
                "
              >
              </button>
              <button
                class="table-btn-update"
                @click="
                  handleModifyFormVisible(
                    scope.row.saleId,
                    scope.row.saleName,
                    scope.row.salePhone
                  )
                "
              >
              </button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页 -->
        <div class="pagination">
          <pagination
            :current-page.sync="currentPage"
            :layout="'total, prev, pager, next, jumper'"
            :total="tableData.total"
            :page-size.sync="pageSize"
            @current-change="handleCurrentChange($event)"
            @update:page="currentPage = $event"
          ></pagination>
        </div>
      </div>
      <div v-show="visualization">
        <el-button
          type="primary"
          style="margin-top: 20px; margin-left: 20px; margin-bottom: 40px"
          @click="handleAdd"
        >
          新增地点
        </el-button>
        <div
          id="mapContainer"
          style="padding: 0px; margin: 0px; width: 100%; height: 700px"
        ></div>
      </div>
      <!-- 点击新增后的弹窗 -->
      <el-dialog
        title="新增销售地点"
        :visible.sync="addFormVisible"
        :modal-append-to-body="false"
        @close="handleAddClose"
      >
        <el-form
          :model="addForm"
          hide-required-asterisk
          ref="addForm"
          label-width="110px"
        >
          <el-form-item label="药店名称" prop="saleName" :rules="rules.nameRules">
            <el-input
              v-model.trim="addForm.saleName"
              autocomplete="off"
              required
            ></el-input>
          </el-form-item>
          <el-form-item label="药店电话" prop="salePhone" :rules="rules.phoneRules">
            <el-input
              v-model.number="addForm.salePhone"
              autocomplete="off"
              required
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addFormVisible = false">取消</el-button>
          <el-button
            type="primary"
            @click="handleAddSalePlace('addForm')"
          >
            确定
          </el-button>
        </div>
      </el-dialog>
      <!-- 点击修改后的弹窗 -->
      <el-dialog
        title="修改销售地点信息"
        :visible="modifyFormVisible"
        :modal-append-to-body="false"
        @close="handleModifyClose"
      >
        <el-form
          :model="modifyForm"
          hide-required-asterisk
          ref="modifyForm"
          label-width="110px"
        >
          <el-form-item label="药店编号">
            <el-input
              v-model="modifyForm.saleId"
              autocomplete="off"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="药店名称" prop="saleName" :rules="rules.nameRules">
            <el-input
              v-model.trim="modifyForm.saleName"
              autocomplete="off"
              required
            ></el-input>
          </el-form-item>
          <el-form-item label="药店电话" prop="salePhone" :rules="rules.phoneRules">
            <el-input
              v-model.number="modifyForm.salePhone"
              autocomplete="off"
              required
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="modifyFormVisible = false">取消</el-button>
          <el-button
            type="primary"
            @click="handleModifySalePlace('modifyForm')"
          >
            确定
          </el-button>
        </div>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script>
import Pagination from "../../components/Pagination";
import { mapGetters } from "vuex";
import rules from "../../utils/validator";
import AMapLoader from "@amap/amap-jsapi-loader";
import axios from "axios";

export default {
  name: "SaleManage",
  components: {
    Pagination,
  },
  data() {
    return {
      currentPage: 1,
      pageSize: 5, // 每页的数据条数
      keywordDefault: "",
      addFormVisible: false, // 控制新增销售地点页面的显示
      addForm: {
        saleName: "",
        salePhone: "",
        lat: 0,
        lng: 0,
      },
      modifyFormVisible: false, // 控制修改信息页面的显示
      modifyForm: {
        saleId: "",
        saleName: "",
        salePhone: "",
      },
      rules, // 封装好的表单验证
      visualization: false,
      map: null,
      addStatus: 0,
      markers: [],
      address: "",
    };
  },
  methods: {
    handleAdd() {
      this.addStatus = 1;
      this.$message({
        message: "请点击地图上的位置",
        type: "warning",
      });
    },
    creatLocation(lng, lat) {
      let url = `https://restapi.amap.com/v3/geocode/regeo?key=9a9c060f4f990c8a78bc351193f6343d&output=json&location=${lng},${lat}`;
      let that = this;
      axios
        .get(url)
        .then(function (res) {
          console.log(res);
          that.addForm.lat = lat;
          that.addForm.lng = lng;
          that.address = res.data.regeocode.formatted_address;
          that.dialogFormVisible = true;
          that.$message({
            showClose: true,
            message: "位置选择成功，请输入详细信息",
            type: "success",
          });
          that.addFormVisible = true;
        })
        .catch(function (error) {
          // 处理错误情况
          console.log(error);
        })
        .then(function () {
          // 总是会执行
        });
    },
    // 控制修改销售地点信息的表单弹出
    handleModifyFormVisible(saleId, saleName, salePhone) {
      this.modifyForm = {
        saleId,
        saleName,
        salePhone,
      };
      this.modifyFormVisible = true;
    },
    refreshMap() {
      let that = this;
      if (this.markers.length > 0) {
        this.map.remove(this.markers);
      }
      
      this.markers = [];
      var list = this.mapData.list;
      console.log(this.mapData);

      list.forEach((element) => {
        var marker = new window.AMap.Marker({
          title: element.saleName, // 自定义点标记覆盖物内容
          position: [element.lng, element.lat], // 基点位置
        });
        this.markers.push(marker);

        // 鼠标点击marker触发
        marker.on("click", function () {
          that.modifyForm = {
            saleId: element.saleId,
            saleName: element.saleName,
            salePhone: element.salePhone,
          };
          that.modifyFormVisible = true;
        });
        this.map.add(marker);
      });
    },
    // 切换分页及首次进入获取数据
    getSalePlaceInfo() {
      this.$store.dispatch("saleInfoManage/getSalePlaceInfo", {
        pn: this.currentPage,
        size: this.pageSize,
      });
    },
    // 切换分页及首次进入获取数据
    getAllSalePlaceInfo() {
      this.$store.dispatch("saleInfoManage/getAllSalePlaceInfo", {
        pn: this.currentPage,
        size: this.pageSize,
      });
    },
    // 当前页改变时触发,跳转其他页
    handleCurrentChange(event) {
      this.currentPage = event.page;
      if (this.keyword.length) {
        this.handleQuery(this.keyword);
      } else {
        this.getSalePlaceInfo();
      }
    },
    // 通过关键字查询数据
    handleQuery(keyword) {
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
            lng: this.addForm.lng,
            lat: this.addForm.lat,
            address: this.address,
            size: this.pageSize,
            pn: this.currentPage,
          });
          setTimeout(() => {
            this.refreshMap();
          }, 10000);
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
      this.$confirm("确定要删除“" + saleName + "”的相关信息吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
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
    // 修改销售地点信息
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
            isAll: this.visualization,
          });
          setTimeout(() => {
            this.refreshMap();
          }, 2000);
        } else {
          this.$message({
            message: "请检查输入的内容是否合规",
            type: "warning",
          });
          return false;
        }
      });
    },
    // 每次关闭表单的时候清除验证器和输入框内容
    handleAddClose() {
      this.addForm = {};
      this.$refs.addForm.resetFields();
    },
    handleModifyClose() {
      // this.$refs.modifyForm.clearValidate();
      this.$refs.modifyForm.resetFields();
    },
    loadMap() {
      let that = this;
      // 地图加载
      AMapLoader.load({
        key: "5d8f45224499593b8b0a3b1bca67a38d", // 申请好的Web端开发者Key，首次调用 load 时必填
        version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        plugins: [], // 需要使用的插件列表，如比例尺 'AMap.Scale' 等
      })
        .then((AMap) => {
          this.map = new AMap.Map("mapContainer", {
            pitch: 45, // 地图俯仰角度，有效范围 0 度 - 83 度
            // viewMode:'3D', // 地图模式
            mapStyle: "amap://styles/whitesmoke",
            // 设置地图缩放程度
            zoom: 11,
            center: [104.06707, 30.660842],
            plugins: ["AMap.Geocoder", "AMap.AutoComplete"],
          });
          this.map.on("click", function (e) {
            if (that.addStatus === 1) {
              that.creatLocation(e.lnglat.getLng(), e.lnglat.getLat());
            }
          });
        })
        .catch((e) => {
          console.log(e);
        });
    },
  },
  mounted() {
    this.getSalePlaceInfo();

    this.getAllSalePlaceInfo();


    this.loadMap();
    // 给地图3秒钟加载时间
    setTimeout(() => {
      this.refreshMap();
    }, 3000);
  },
  computed: {
    // 后端返回的数据
    ...mapGetters({
      tableData: "salePlaceInfo",
      mapData: "saleAllPlaceInfo",
    }),
    // 用户输入的关键字
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
.el-switch {
  margin-top: 20px;
  margin-left: 20px;
}
</style>