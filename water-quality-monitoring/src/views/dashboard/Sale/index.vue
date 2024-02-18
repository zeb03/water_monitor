<template>
  <el-card class="box-card" style="margin: 10px 0px">
    <div slot="header" class="clearfix">
      <!--  @tab-click="handleClick" -->
      <!-- 头部左侧内容 -->
      <el-tabs class="tab" v-model="activeName">
        <el-tab-pane label="新增用户" name="sale"></el-tab-pane>
        <el-tab-pane label="访问量" name="visite"></el-tab-pane>
      </el-tabs>
      <!-- 头部右侧内容 -->
      <div class="right">
        <span @click="setDay">今日</span>
        <span @click="setWeek">本周</span>
        <span @click="setMonth">本月</span>
        <span @click="setYear">本年</span>
        <!--    v-model="value1" -->
        <el-date-picker
          v-model="date"
          class="date"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
        >
        </el-date-picker>
      </div>
    </div>
    <div>
      <el-row :gutter="10">
        <el-col :span="16">
          <!-- 容器 -->
          <div class="charts" ref="charts"></div>
        </el-col>
        <el-col :span="8" class="right">
          <h3>Top5{{ title }}排名</h3>
          <ul>
            <li>
              <span class="rindex">0</span>
              <span>扫码</span>
              <span class="rvalue">123456</span>
            </li>
            <li>
              <span class="rindex">1</span>
              <span>分享</span>
              <span class="rvalue">123456</span>
            </li>
            <li>
              <span class="rindex">2</span>
              <span>搜索</span>
              <span class="rvalue">123456</span>
            </li>
            <li>
              <span>3</span>
              <span>跳转</span>
              <span class="rvalue">123456</span>
            </li>
            <li>
              <span>4</span>
              <span>线下宣传</span>
              <span class="rvalue">123456</span>
            </li>
          </ul>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>

<script>
//引入echarts
import * as echarts from "echarts";
import dayjs from "dayjs";
import { mapState } from "vuex";
export default {
  name: "",
  data() {
    return {
      activeName: "sale",
      mycharts: null,
      //收集日历数据
      date: [],
    };
  },
  mounted() {
    //初始化echarts实例
    this.mycharts = echarts.init(this.$refs.charts);
    //配置数据
    this.mycharts.setOption({
      title: {
        text: this.title + "趋势",
      },
      tooltip: {
        trigger: "axis",
        axisPointer: {
          type: "shadow",
        },
      },
      grid: {
        left: "3%",
        right: "4%",
        bottom: "3%",
        containLabel: true,
      },
      xAxis: [
        {
          type: "category",
          data: [
            "一月",
            "二月",
            "三月",
            "四月",
            "五月",
            "六月",
            "七月",
            "八月",
            "九月",
            "十月",
            "十一月",
            "十二月",
          ],
          axisTick: {
            alignWithLabel: true,
          },
        },
      ],
      yAxis: [
        {
          data: [
            "234",
            "859",
            "124",
            "583",
            "368",
            "368",
            "368",
            "368",
            "368",
            "368",
            "368",
            "368",
          ],
          type: "value",
        },
      ],
      series: [
        {
          name: "Direct",
          type: "bar",
          barWidth: "60%",
          data: [],
          color: "#535B8F",
        },
      ],
    });

    //顶部是mounted：为什么第一次没有数据，没有数据因此不显示
  },
  computed: {
    //计算属性-标题
    title() {
      //重新设置配置项
      return this.activeName == "sale" ? "用户新增" : "访问量";
    },
    ...mapState({
      listState: (state) => state.home.list,
    }),
  },
  //监听属性
  watch: {
    title() {
      console.log("修改配置数据");
      //重新修改图标的配置数据
      //图标配置数据可以再次修改，如果有新的数值，新的数值，没有新的数值，还是用以前的
      this.mycharts.setOption({
        title: {
          text: this.title,
        },
        xAxis: {
          data:
            this.title == "用户新增"
              ? this.listState.orderFullYearAxis
              : this.listState.userFullYearAxis,
        },
        series: [
          {
            name: "Direct",
            type: "bar",
            barWidth: "60%",
            data:
              this.title == "用户新增"
                ? this.listState.orderFullYear
                : this.listState.userFullYear,
            color: "#535B8F",
          },
        ],
      });
    },
    listState() {
      this.mycharts.setOption({
        title: {
          text: this.title + "趋势",
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            data: this.listState.orderFullYearAxis,
            axisTick: {
              alignWithLabel: true,
            },
          },
        ],
        yAxis: [
          {
            type: "value",
          },
        ],
        series: [
          {
            name: "Direct",
            type: "bar",
            barWidth: "60%",
            data: this.listState.orderFullYear,
            color: "#535B8F",
          },
        ],
      });
    },
  },
  methods: {
    //本天
    setDay() {
      const day = dayjs().format("YYYY-MM-DD");
      this.date = [day, day];
    },
    //本周
    setWeek() {
      const start = dayjs().day(1).format("YYYY-MM-DD");
      const end = dayjs().day(7).format("YYYY-MM-DD");
      this.date = [start, end];
    },
    //本月
    setMonth() {
      const start = dayjs().startOf("month").format("YYYY-MM-DD");
      const end = dayjs().endOf("month").format("YYYY-MM-DD");
      this.date = [start, end];
    },
    //本年
    setYear() {
      const start = dayjs().startOf("year").format("YYYY-MM-DD");
      const end = dayjs().endOf("year").format("YYYY-MM-DD");
      this.date = [start, end];
    },
  },
};
</script>

<style scoped>
.clearfix {
  position: relative;
  display: flex;
  justify-content: space-between;
}
.tab {
  width: 100%;
}
.right {
  position: absolute;
  right: 0px;
}
.date {
  width: 250px;
  margin: 0px 20px;
}
.right span {
  margin: 0px 10px;
}
.charts {
  width: 100%;
  height: 300px;
}
ul {
  list-style: none;
  width: 100%;
  height: 300px;
  padding: 0px;
}
ul li {
  height: 8%;
  margin: 10px 0px;
}
.rindex {
  float: left;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: black;
  color: white;
  text-align: center;
}
.rvalue {
  float: right;
}
</style>
