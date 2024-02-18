<template>
  <div>
    <div class="title">
      <el-form :inline="true" :model="condition" class="demo-form-inline">
        <el-form-item label="水质指标">
          <el-select v-model="condition.target">
            <el-option label="酸碱度" value="shanghai"></el-option>
            <el-option label="溶解氧" value="beijing"></el-option>
            <el-option label="浊度" value="beijing"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="机器学习模型">
          <el-select v-model="condition.target">
            <el-option label="酸碱度" value="shanghai"></el-option>
            <el-option label="溶解氧" value="beijing"></el-option>
            <el-option label="浊度" value="beijing"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="onSubmit"
            >查询</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div class="titleFont">可用模型列表</div>
    <el-card>
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="type" label="模型类型" width="180">
        </el-table-column>
        <el-table-column prop="error" label="均方根误差" width="180">
        </el-table-column>
        <el-table-column prop="people" label="训练者"> </el-table-column>
        <el-table-column prop="date" label="训练日期"> </el-table-column>
      </el-table>
    </el-card>
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
      condition: {
        target: "",
        model: "",
        name: "张雪",
        xData: ["2020-02", "2020-03", "2020-04", "2020-05"], //横坐标数据
        yData: [30, 132, 80, 134], //纵坐标数据，与横坐标对应
      },
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
    };
  },
  mounted() {
    this.initEcharts();
  },
  methods: {
    onSubmit() {
      //向后端发请求
      console.log(this.condition);
    },
    initEcharts() {
      const option = {
        xAxis: {
          type: "category",
          data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            data: [150, 230, 224, 218, 135, 147, 260],
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