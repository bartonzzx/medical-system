<template>
  <el-container>
    <!-- 头部区域 -->
    <el-header height="76px">
      <h2>数据面板</h2>
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>数据面板</el-breadcrumb-item>
      </el-breadcrumb>
    </el-header>

    <!-- 主体内容 -->
    <el-main>
      <div class="dashboard-container">
        <!-- 数据指标卡片 -->
        <div class="number_container">
          <div class="square">
            <div icon-class="doc" class="icon" />
            <div class="title">
              <div class="subtitle">医师人数</div>
              <div class="number">{{ doctors }}</div>
            </div>
          </div>
          <div class="square">
            <div icon-class="bag" class="icon" />
            <div class="title">
              <div class="subtitle">药物种类</div>
              <div class="number">{{ drugs }}</div>
            </div>
          </div>
          <div class="square">
            <div icon-class="operation" class="icon" />
            <div class="title">
              <div class="subtitle">合作企业</div>
              <div class="number">{{ companies }}</div>
            </div>
          </div>
          <div class="square">
            <div icon-class="patient" class="icon" />
            <div class="title">
              <div class="subtitle">入驻药店</div>
              <div class="number">{{ sales }}</div>
            </div>
          </div>
        </div>

        <!-- 图表区域 -->
        <div class="father_chart">
          <div id="histogram" class="histogram"></div>

          <div class="chartcontainer">
            <div class="rectangle">
              <h1 style="color: gray">医院科室</h1>
              <div id="piechart" class="piechart"></div>
            </div>

            <div class="rectangle">
              <h1 style="color: gray">最新政策</h1>
              <div style="width: 100%">
                <el-table :data="materials" stripe style="width: 100%">
                  <el-table-column prop="notice" label="最新医保政策" width="450">
                    <template slot-scope="scope">
                      <div>{{ scope.row.notice }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column prop="date" width="100">
                    <template slot="header">
                      <a href="/#/manage/medical/policy" target="_blank">More&lt;&lt;</a>
                    </template>
                  </el-table-column>
                </el-table>
              </div>

              <div style="width: 100%; margin-top: 20px">
                <el-table :data="policys" stripe style="width: 100%">
                  <el-table-column prop="notice" label="最新医药公司政策" width="450">
                    <template slot-scope="scope">
                      <div>{{ scope.row.notice }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column prop="date" width="100">
                    <template slot="header">
                      <a href="/#/manage/company/policy" target="_blank">More&lt;&lt;</a>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import { mapGetters } from "vuex";
import * as echarts from "echarts";
import "echarts/extension/bmap/bmap";
import { getAllDashboardInfo } from "../../api/admin/dashboardApi"

export default {
  name: "Dashboard",
  computed: {
    ...mapGetters(["name"]),
  },
  data() {
    return {
      doctors: 0,
      drugs: 0,
      companies: 0,
      sales: 0,
      docLevelType: ["主任医师", "普通医师", "实习医师"],
      docLevel: [],
      picData: [],
      materials: [],
      policys: []
    };
  },
  mounted() {
    this.initData();
  },
  methods: {
    initData() {
      getAllDashboardInfo().then((res) => {
        let dashboardData = res.data.data.data;

        // 基础数据
        this.doctors = dashboardData.doctorNumb
        this.drugs = dashboardData.drugNumb
        this.companies = dashboardData.companyNumb
        this.sales = dashboardData.saleNumb

        // 医生职级数据
        this.docLevel = [
          dashboardData.docLevel.l1,
          dashboardData.docLevel.l2,
          dashboardData.docLevel.l3
        ];
        this.histogram();

        // 科室分布数据
        let treatMap = dashboardData.treatMap
        for (let item in treatMap) {
          this.picData.push({
            name: item,
            value: treatMap[item]
          });
        }
        this.piechart();

        // 政策数据
        let materialsList = dashboardData.materials
        materialsList.forEach(element => {
          if (element.message.length > 30) {
            element.message = element.message.substr(0, 29) + "...";
          }
          this.materials.push({
            notice: element.message,
            date: element.updateTime
          });
        });

        let policyList = dashboardData.policys
        policyList.forEach(element => {
          if (element.message.length > 30) {
            element.message = element.message.substr(0, 29) + "...";
          }
          this.policys.push({
            notice: element.message,
            date: element.updateTime
          });
        });
      });
    },
    histogram() {
      const chartDom = document.getElementById("histogram");
      const myChart = echarts.init(chartDom);

      const option = {
        title: {
          text: "医生职级",
          left: "60px",
          padding: [10, 10, 10, 0],
        },
        tooltip: {
          trigger: "axis",
          axisPointer: { type: "shadow" }
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true
        },
        xAxis: {
          type: "category",
          data: this.docLevelType
        },
        yAxis: { type: "value" },
        series: [{
          type: "bar",
          data: [
            { value: this.docLevel[0], itemStyle: { color: "#2ecc71" } },
            { value: this.docLevel[1], itemStyle: { color: "#70a1ff" } },
            { value: this.docLevel[2], itemStyle: { color: "#eccc68" } }
          ]
        }]
      };

      myChart.setOption(option);
    },
    piechart() {
      const pieChart = echarts.init(document.getElementById("piechart"));

      const option = {
        title: { text: "" },
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        legend: {
          orient: "vertical",
          left: "left"
        },
        series: [{
          name: "医师人数",
          type: "pie",
          radius: ["50%", "70%"],
          avoidLabelOverlap: false,
          label: { show: false, position: "center" },
          emphasis: {
            label: { show: true, fontSize: "30", fontWeight: "bold" }
          },
          labelLine: { show: false },
          data: this.picData
        }]
      };

      pieChart.setOption(option, true);
    }
  }
};
</script>

<style scoped>
/* 布局容器样式 */
.el-container {
  height: 100% !important;
}

.el-header {
  background-color: #fff;
  border-bottom: 1px solid #e6e9eb;
  padding: 15px 20px 0;
}

.el-header h2 {
  font-size: 20px;
  margin-bottom: 15px;
}

.el-main {
  box-sizing: border-box;
  margin: 20px 15px 10px 20px;
  background-color: #fff;
  border-top: 3px solid #e8ebed;
  padding: 0;
  /* overflow: hidden; */
}

/* 标题区域 */
.main-title {
  height: 35px;
  padding: 10px;
  border-bottom: 1px solid #e6e9eb;
  margin-bottom: 10px;
}

.main-title h3 {
  margin-top: 10px;
  float: left;
}

/*.new-add {
  float: right;
  background: url("../assets/add.png") no-repeat;
  background-size: 100% !important;
  height: 40px;
  width: 40px;
  margin-right: 20px;
}*/

/* 表格相关 */
.search-col {
  margin-left: 20px;
  margin-bottom: 5px;
}

.el-table {
  padding: 0 20px;
  border: 0;
}

.demo-table-expand {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.demo-table-expand .el-form-item {
  margin: 0;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 仪表盘样式 */
.dashboard-container {
  margin: 10px 10px 10px 20px;
  height: 800px;
}

.dashboard-text {
  font-size: 30px;
  line-height: 46px;
}

/* 数据卡片 */
.number_container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
  height: 100px;
}

.square {
  padding: 20px;
  width: 285px;
  height: 50px;
  background-color: white;
  margin: 10px;
  border-radius: 15px;
  box-shadow: 0px 12px 26px rgba(16, 30, 115, 0.06);
}

.icon {
  width: 50px;
  height: 50px;
  display: inline-block;
}

.title {
  display: inline-block;
  margin-left: 30px;
  vertical-align: top;
}

.subtitle {
  font-size: 18px;
  font-weight: 700;
  color: #25282b;
}

.number {
  margin-top: 12px;
  color: #336cfb;
  font-size: 22px;
  display: inline-block;
}

.variation {
  /* 修复大小写问题 */
  display: inline-block;
  margin-left: 15px;
  vertical-align: middle;
  color: #0deb48;
}

/* 图表容器 */
.chartcontainer {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.rectangle {
  padding: 20px;
  width: 590px;
  height: 500px;
  border-radius: 15px;
  background-color: white;
  box-shadow: 0px 12px 26px rgba(16, 30, 115, 0.06);
  margin: 10px;
}

.piechart {
  margin-top: 40px;
  width: 600px;
  height: 400px;
}

.map {
  margin-left: -10px;
  width: 600px;
  height: 400px;
}

/* 图表父容器 */
.father_chart {
  display: flex;
  justify-content: center;
}

.histogram {
  width: 1200px;
  height: 400px;
  margin: 0 auto;
  padding: 20px;
  border-radius: 15px;
  background-color: white;
  box-shadow: 0px 12px 26px rgba(16, 30, 115, 0.06);
}
</style>