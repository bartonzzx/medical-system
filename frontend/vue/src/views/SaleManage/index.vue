<!-- 医药公司信息管理：销售地点管理页面 -->
<template>
  <el-container>
    <!-- 头部区域 -->
    <el-header height="76px">
      <h2>销售地点管理</h2>
      <!-- 面包屑导航区域 -->
      <el-breadcrumb separator="">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>销售地点管理</el-breadcrumb-item>
      </el-breadcrumb>
    </el-header>

    <!-- 主体内容区域 -->
    <el-main>
      <!-- 地图/列表切换开关 -->
      <el-switch active-text="销售地点地图展示" v-model="visualization" active-color="#13ce66" inactive-color="#bdc3c7">
      </el-switch>

      <!-- 列表模式 -->
      <div v-show="!visualization">
        <div class="main-title">
          <h3>销售地点列表</h3>
          <!-- 新增按钮 (条件渲染) -->
          <!-- <button
            class="new-add"
            @click="addFormVisible = true"
            v-if="hasRole">新增</button> -->
        </div>

        <!-- 搜索区域 -->
        <el-row :gutter="20">
          <el-col :span="23" class="search-col">
            <keep-alive>
              <el-input placeholder="查询(输入要查询的药店名称)" size="small" v-model="keyword" @input="handelQuery">
              </el-input>
            </keep-alive>
          </el-col>
        </el-row>

        <!-- 销售地点表格 -->
        <el-table stripe :default-sort="{ prop: 'date', order: 'descending' }" :data="tableData.list"
          highlight-current-row>
          <el-table-column prop="saleId" label="药店编号" sortable />
          <el-table-column prop="saleName" label="药店名称" />
          <el-table-column prop="salePhone" label="药店电话" />
          <el-table-column prop="address" label="药店地址" />
          <el-table-column label="操作" v-if="hasRole">
            <template slot-scope="scope">
              <button class="table-btn-delete"
                @click="handleDeleteSalePlace(scope.row.saleId, scope.row.saleName)"></button>
              <button class="table-btn-update" @click="handleModifyFormVisible(
                scope.row.saleId,
                scope.row.saleName,
                scope.row.salePhone
              )">
              </button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页组件 -->
        <div class="pagination">
          <pagination :current-page.sync="currentPage" :layout="'total,prev,pager,next,jumper'" :total="tableData.total"
            :page-size.sync="pageSize" @currentChange="handleCurrentChange($event)" @update:page="currentPage = $event">
          </pagination>
        </div>
      </div>

      <!-- 地图模式 -->
      <div v-show="visualization">
        <el-button type="primary" style="margin-top:20px;margin-left:20px;margin-bottom:40px" @click="handleAdd">
          新增地点
        </el-button>
        <!-- 地图容器 -->
        <div id="mapContainer" style="padding:0px;margin:0px;width:100%!;height:700px">
        </div>
      </div>
    </el-main>

    <!-- 新增地点弹窗 -->
    <el-dialog title="新增销售地点" :visible.sync="addFormVisible" :modal-append-to-body="false" @close="handleAddClose">
      <el-form :model="addForm" hide-required-asterisk ref="addForm" label-width="110px">
        <el-form-item label="药店名称" prop="saleName" :rules="rules.nameRules">
          <el-input v-model.trim="addForm.saleName" autocomplete="off" required>
          </el-input>
        </el-form-item>
        <el-form-item label="药店电话" prop="salePhone" :rules="rules.phoneRules">
          <el-input v-model.number="addForm.salePhone" autocomplete="off" required>
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAddSalePlace('addForm')">
          确定
        </el-button>
      </div>
    </el-dialog>

    <!-- 修改地点弹窗 -->
    <el-dialog title="修改销售地点信息" :visible.sync="modifyFormVisible" :modal-append-to-body="false"
      @close="handleModifyClose">
      <el-form :model="modifyForm" hide-required-asterisk ref="modifyForm" label-width="110px">
        <el-form-item label="药店编号">
          <el-input v-model="modifyForm.saleId" autocomplete="off" disabled>
          </el-input>
        </el-form-item>
        <el-form-item label="药店名称" prop="saleName" :rules="rules.nameRules">
          <el-input v-model.trim="modifyForm.saleName" autocomplete="off" required>
          </el-input>
        </el-form-item>
        <el-form-item label="药店电话" prop="salePhone" :rules="rules.phoneRules">
          <el-input v-model.number="modifyForm.salePhone" autocomplete="off" required>
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleModifySalePlace('modifyForm')">
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
      pageSize: 5,
      keywordDefault: "",
      addFormVisible: false,
      addForm: {
        saleName: "",
        salePhone: "",
        lat: 0,
        lng: 0,
      },
      modifyFormVisible: false,
      modifyForm: {
        saleId: "",
        saleName: "",
        salePhone: "",
      },
      rules,
      visualization: false,
      map: null,
      addStatus: 0,
      markers: [],
      address: ''
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
      // console.log("here check lng and lat lng:", lng, "lat:", lat);
      let url = `https://restapi.amap.com/v3/geocode/regeo?key=a2889cfb76d1f1c2ef3d4b4d163a0020	&output=json&location=${lng},${lat}`;
      let that = this;
      axios
        .get(url)
        .then(function (res) {
          // console.log("here check res",res);
          that.addForm.lat = lat;
          that.addForm.lng = lng;
          that.address = res.data.regeocode.formatted_address;
          // console.log("here address", that.address);
          that.$message({
            showClose: true,
            message: "位置选择成功，请输入详细信息",
            type: "success",
          });
          that.addFormVisible = true;
        })
        .catch(function (error) {
          console.log(error);
        });
    },

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
      // console.log(this.mapData);
      list.forEach((element) => {
        var marker = new window.AMap.Marker({
          title: element.saleName,
          position: [element.lng, element.lat],
        });
        this.markers.push(marker);

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

    getSalePlaceInfo() {
      this.$store.dispatch("saleInfoManage/getSalePlaceInfo", {
        pn: this.currentPage,
        size: this.pageSize,
      });
    },

    getAllSalePlaceInfo() {
      this.$store.dispatch("saleInfoManage/getAllSalePlaceInfo", {
        pn: this.currentPage,
        size: this.pageSize,
      });
    },

    handleCurrentChange(event) {
      this.currentPage = event.page;
      if (this.keyword.length) {
        this.handelQuery(this.keyword);
      } else {
        this.getSalePlaceInfo();
      }
    },

    handelQuery(keyword) {
      this.$store.dispatch("saleInfoManage/getSalePlaceInfo", {
        pn: this.currentPage,
        size: this.pageSize,
        keyword,
      });
    },

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

    handleDeleteSalePlace(saleId, saleName) {
      this.$confirm(`确定要删除“${saleName}”的相关信息吗?`, "提示", {
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

    handleAddClose() {
      this.addForm = {};
      this.$refs.addForm.resetFields();
    },

    handleModifyClose() {
      this.$refs.modifyForm.resetFields();
    },

    loadMap() {
      let that = this;
      AMapLoader.load({
        key: "bd6b2dbb8bc5d1481aa43a68ae12b2a6",
        version: "2.0",
        plugins: [],
      })
        .then((AMap) => {
          this.map = new AMap.Map("mapContainer", {
            mapStyle: "amap://styles/whitesmoke",
            zoom: 11,
            center: [104.06707, 30.660842],
            plugins: ["AMap.Geocoder", "AMap.AutoComplete"],
          });

          this.map.on("click", function (e) {
            if (that.addStatus == 1) {
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
    setTimeout(() => {
      this.refreshMap();
    }, 3000);
  },
  computed: {
    ...mapGetters({
      tableData: "salePlaceInfo",
      mapData: "saleAllPlaceInfo",
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

.el-switch {
  margin-top: 20px;
  margin-left: 20px;
}
</style>