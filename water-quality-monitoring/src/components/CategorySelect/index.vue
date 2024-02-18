<template>
  <div>
    <!-- inline：代表行内表单 -->
    <el-form :inline="true" :model="uForm" class="demo-form-inline">
      <el-form-item label="监测区域">
        <el-select
          placeholder="请选择"
          v-model="uForm.area"
          @change="chosedBind"
        >
          <el-option
            v-for="item in areaslist"
            :label="item.name"
            :value="item.name"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="监测船">
        <el-select placeholder="请选择" v-model="uForm.station">
          <el-option
            v-for="item in shiplist"
            :label="item.name"
            :value="item.name"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="时间">
        <div class="block">
          <el-date-picker
            v-model="uForm.date"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
  
  <script>
export default {
  name: "CategorySelect",
  data() {
    return {
      bind: "",
      uForm: {},
      areaslist: [],
      shiplist: [],
    };
  },
  mounted() {
    this.getAreas();
  },
  methods: {
    chosedBind() {
      console.log("准备获取监测船数据");
      this.getShips();
    },
    async onSubmit() {
      // 传递筛选条件到父组件
      this.$emit("getScreeningCondition", this.uForm);
    },
    async getAreas() {
      let res = await this.$API.water.reqAllAreas();
      this.areaslist = res.data;
    },
    async getShips() {
      let res = await this.$API.water.reqStationList(this.uForm.area);
      console.log(res);
      this.shiplist = res.data;
    },
  },
};
</script>
  
  <style>
</style>