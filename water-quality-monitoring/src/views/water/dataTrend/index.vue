<template>
  <div>
    <div class="title">
      <el-form :inline="true" :model="condition" class="demo-form-inline">
        <el-form-item label="水质指标">
          <el-select v-model="condition.indicator">
            <el-option
              v-for="item in indicatorData"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="监测区域">
          <el-select placeholder="请选择" v-model="condition.area">
            <el-option
              v-for="item in areaslist"
              :label="item.name"
              :value="item.name"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间">
          <el-input v-model="condition.period" placeholder=""></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="onSubmit"
            >查询</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div class="titleFont line">
      模型预计PH值:8.68
      <div
        class="echart"
        id="mychart"
        :style="{ float: 'left', width: '100%', height: '400px' }"
      ></div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  name: "Trend",
  data() {
    return {
      condition: {},
      tableData: [
        {
          type: "LSTM",
          error: "0.18",
          people: "admin",
          date: "2023-05-09",
        },
        {
          type: "SVM",
          error: "0.18",
          people: "admin",
          date: "2023-05-09",
        },
        {
          type: "RVM",
          error: "0.18",
          people: "admin",
          date: "2023-05-09",
        },
      ],
      indicatorData: [],
      areaslist: [],
      echartData: {},
    };
  },
  mounted() {
    // this.initEcharts();
    this.getIndicator();
    this.getAreas();
  },
  methods: {
    async getAreas() {
      let res = await this.$API.water.reqAllAreas();
      this.areaslist = res.data;
    },
    async onSubmit() {
      //向后端发请求
      console.log(this.condition);
      let res = await this.$API.water.reqWaterPlot(this.condition);
      console.log(res);
      this.echartData.x = res.data.datas;
      this.echartData.y = res.data.specWaterQualities;
      this.initEcharts();
    },
    initEcharts() {
      const option = {
        xAxis: {
          type: "category",
          data: this.echartData.x,
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            data: this.echartData.y,
            type: "line",
          },
        ],
      };

      const myChart = echarts.init(document.getElementById("mychart")); // 图标初始化
      myChart.setOption(option); // 渲染页面
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    async getIndicator() {
      let res = await this.$API.water.reqIndicator();
      console.log(res);
      if (res.code == 200) {
        this.indicatorData = res.data;
      }
    },
  },
};
</script>

<style scoped>
.titleFont {
  text-align: center;
  font-weight: 600;
  font-size: 24px;
  margin: 10px auto;
}
.line {
  margin-top: 20px;
  /* width: 100%;
  height: 200px; */
}
</style>