<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="18">
        <el-card
          ><div style="font-weight: 800; font-size: 20px">水质总览</div>
          <el-col :span="6">
            <div class="box" id="sortchart"></div>
          </el-col>
          <el-col :span="18">
            <div style="font-weight: 800; font-size: 30px; margin-bottom: 10px">
              水质优秀
            </div>
            <div class="box_right">当前设备3台 连续工作300小时</div>
          </el-col>
          <el-col :span="24">
            <div>水质分析趋势图</div>
            <div class="box_line" id="lineTrand"></div>
          </el-col>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="margin-bottom: 10px">
          <div style="font-weight: 800; font-size: 20px">水质分析</div>
          <div id="route" class="box_route"></div>
          <div>
            <el-table
              :data="tableData"
              style="width: 100%; margin-bottom: 20px"
            >
              <el-table-column prop="date" label="日期" width>
              </el-table-column>
              <el-table-column prop="name" label="姓名" width>
              </el-table-column>
              <el-table-column prop="address" label="地址"> </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from "echarts";
import Search from "./Search";
import Category from "./Category";
export default {
  name: "",
  components: {
    Search,
    Category,
  },
  data() {
    return {
      option: {
        ringColor: [
          {
            offset: 0,
            color: "#02d6fc", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "#367bec", // 100% 处的颜色
          },
        ],
        title: [
          //标题组件，数组里的一个对象表示一个标题组件
          {
            text: "机组效率",
            left: "center",
            bottom: "5%",
            textStyle: { color: "#fff" },
          },
        ],
        series: [
          //系列
          {
            // name: "机组效率",
            type: "pie", //pie类型的图实现环形图
            radius: ["60%", "90%"], //数组的话，表示内圆和外圆的半径大小，相对于宽高中较小的那一个。
            center: ["50%", "50%"], //圆心坐标
            avoidLabelOverlap: false, //是否启用防止标签重叠策略
            startAngle: 90, //第一个数据开始绘制的角度，以正交直角坐标系为标准
            label: {
              //每个数据的标签
              show: true, //设置为true则显示第一个数据
              position: "center", //位置居中
              formatter: "{d}%", //{d}表示数据在总数据中的百分比
              fontSize: 20,
              fontWeight: "bold",
            },
            color: ["#eee"], //系列的颜色
            emphasis: {
              //高亮，即鼠标经过时的样式
              scale: false, //表示不放大item
            },
            labelLine: {
              show: true,
            },
            data: [
              {
                value: 80,
                name: "",
                itemStyle: {
                  normal: {
                    color: {
                      // 完成的圆环的颜色
                      colorStops: [
                        {
                          offset: 0,
                          color: "#02d6fc", // 0% 处的颜色
                        },
                        {
                          offset: 1,
                          color: "#367bec", // 100% 处的颜色
                        },
                      ],
                    },
                    label: {
                      show: false,
                    },
                    labelLine: {
                      show: false,
                    },
                  },
                },
              },
              {
                value: 20,
                name: "",
                emphasis: {
                  label: {
                    show: false, //这个数据高亮时不显示label，就不会显示替遮住第一个数据的label值了
                  },
                },
              },
            ],
          },
        ],
      },
      option_line: {
        xAxis: {
          //隐藏x轴
          show: false,
          type: "category",
        },
        yAxis: {
          //隐藏y轴
          show: true,
        },
        //系列
        series: [
          {
            smooth: true,
            type: "line",
            data: [10, 7, 33, 12, 48, 9, 29, 10, 44],
            //拐点的样式的设置
            itemStyle: {
              opacity: 0,
            },
            //线条的样式
            lineStyle: {
              color: "#0098FA",
            },
            //填充颜色设置
            areaStyle: {
              color: {
                type: "linear",
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: "#0098FA", // 0% 处的颜色
                  },
                  {
                    offset: 1,
                    color: "#fff", // 100% 处的颜色
                  },
                ],
                global: false, // 缺省为 false
              },
            },
          },
        ],
        //布局调试
        grid: {
          left: -5,
          top: 0,
          right: 0,
          bottom: 0,
        },
      },
      option_route: {
        tooltip: {
          trigger: "item",
        },
        legend: {
          top: "5%",
          left: "center",
        },
        series: [
          {
            name: "Access From",
            type: "pie",
            radius: ["40%", "70%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 40,
                fontWeight: "bold",
              },
            },
            labelLine: {
              show: false,
            },
            data: [
              { value: 93, name: "优秀" },
              { value: 5, name: "中等" },
              { value: 2, name: "较差" },
            ],
          },
        ],
      },
      option_test: {
        xAxis: {
          // show: false,
          type: "category",
          data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
        },
        yAxis: {
          // show: false,
          type: "value",
        },
        grid: {
          // 让图表占满容器
          top: "0px",
          left: "0px",
          right: "0px",
          bottom: "0px",
        },

        series: [
          {
            data: [820, 932, 901, 934, 1290, 1330, 1320],
            type: "line",
            smooth: true,
            lineStyle: {
              color: {
                type: "linear",
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: "#325dbb", // 0% 处的颜色
                  },
                  {
                    offset: 1,
                    color: "#62cad2", // 100% 处的颜色
                  },
                ],
                global: false, // 缺省为 false
              },
            },
            areaStyle: {
              color: {
                type: "linear",
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: "#ebf4fe", // 0% 处的颜色
                  },
                  {
                    offset: 1,
                    color: "#fff", // 100% 处的颜色
                  },
                ],
                global: false, // 缺省为 false
              },
            },
          },
        ],
      },
      tableData: [
        {
          date: "2022-05-22",
          name: "珠江",
          address: "荔湾至越秀段",
        },
        {
          date: "2022-05-22",
          name: "珠江",
          address: "东江",
        },
        // {
        //   date: "2016-05-04",
        //   name: "王小虎",
        //   address: "上海市普陀区金沙江路 1518 弄",
        // },
      ],
    };
  },
  mounted() {
    this.initEcharts();
    this.initLineTrand();
    this.initRoute();
  },
  methods: {
    initEcharts() {
      const myChart = echarts.init(document.getElementById("sortchart")); // 图标初始化
      myChart.setOption(this.option); // 渲染页面
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    initLineTrand() {
      const myChart = echarts.init(document.getElementById("lineTrand")); // 图标初始化
      myChart.setOption(this.option_test); // 渲染页面
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    initRoute() {
      const myChart = echarts.init(document.getElementById("route")); // 图标初始化
      myChart.setOption(this.option_route); // 渲染页面
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
  },
};
</script>

<style scoped>
.box {
  width: 200px;
  height: 200px;
  display: inline;
  /* background-color: pink; */
}
.box_right {
  display: inline;
}
.echart_total {
  width: 150px;
  height: 150px;
}
.box_line {
  width: 100%;
  height: 300px;
  margin-top: 15px;
}
.box_route {
  width: 100%;
  height: 350px;
}
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
</style>
