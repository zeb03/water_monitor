<template>
  <div>
    <div class="titleFont">今日水质监测详细数据</div>
    <el-row>
      <el-card style="margin-bottom: 20px">
        <el-form :inline="true" :model="Form">
          <el-form-item label="监测区域">
            <el-select
              placeholder="请选择"
              v-model="Form.area"
              @change="getWaterData"
            >
              <el-option
                v-for="item in areaslist"
                :label="item.name"
                :value="item.name"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <el-button
          type="primary"
          style="margin-bottom: 20px"
          icon="el-icon-plus"
          @click="showDialog"
          >添加数据</el-button
        >
        <el-table :data="reqData" border style="width: 100%">
          <el-table-column prop="station" label="监测站" width="150">
          </el-table-column>
          <el-table-column prop="ph" label="酸碱度" width="100">
          </el-table-column>
          <el-table-column prop="tds" label="溶解氧" width="100">
          </el-table-column>
          <el-table-column prop="temperature" label="水温" width="100">
          </el-table-column>
          <el-table-column prop="turbidity" label="浊度" width="100">
          </el-table-column>
          <el-table-column prop="conductivity" label="电导性" width="100">
          </el-table-column>
          <el-table-column prop="date" label="监测时间" width="200">
          </el-table-column>
          <!-- <el-table-column prop="target" label="检测指标" width="100">
            </el-table-column> -->
          <el-table-column prop="prop" label="操作">
            <template slot-scope="{ row, $index }">
              <el-button
                type="info"
                icon="el-icon-info"
                size="mini"
                @click="showInfo(row)"
                >查看</el-button
              >
              <el-button
                type="warning"
                icon="el-icon-info"
                size="mini"
                @click="showInfo(row)"
                >修改</el-button
              >
              <el-button
                type="danger"
                icon="el-icon-delete"
                size="mini"
                @click="deleteStick(row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-card>
        <div
          class="echart"
          id="sortchart"
          :style="{ float: 'left', width: '100%', height: '400px' }"
        ></div>
      </el-card>
    </el-row>
    <!-- 添加数据 -->
    <el-dialog title="水质信息" :visible.sync="dialogFormVisible">
      <!-- <el-form style="width: 80%" :model="wForm" :rules="rules" ref="ruleForm"> -->
      <el-form style="width: 80%" :model="wForm">
        <el-form-item label="监测区域" label-width="100px" prop="area">
          <el-input v-model="wForm.area" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="conductivity"
          label-width="100px"
          prop="conductivity"
        >
          <el-input v-model="wForm.conductivity" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="ph" label-width="100px" prop="ph">
          <el-input v-model="wForm.ph" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="station" label-width="100px" prop="station">
          <el-input v-model="wForm.station" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="tds" label-width="100px" prop="tds">
          <el-input v-model="wForm.tds" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="temperature"
          label-width="100px"
          prop="temperature"
        >
          <el-input v-model="wForm.temperature" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="turbidity" label-width="100px" prop="turbidity">
          <el-input v-model="wForm.turbidity" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addOrUpdata">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 修改数据 -->
    <el-dialog title="水质信息" :visible.sync="dialogStateVisible">
      <!-- <el-form style="width: 80%" :model="wForm" :rules="rules" ref="ruleForm"> -->
      <el-form style="width: 80%" :model="cform">
        <el-form-item label="监测区域" label-width="100px" prop="area">
          <el-select
            placeholder="请选择"
            v-model="Form.area"
            @change="getWaterData"
          >
            <el-option
              v-for="item in areaslist"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="conductivity"
          label-width="100px"
          prop="conductivity"
        >
          <el-input v-model="cform.conductivity" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="ph" label-width="100px" prop="ph">
          <el-input v-model="cform.ph" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="station" label-width="100px" prop="station">
          <el-input v-model="cform.station" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="tds" label-width="100px" prop="tds">
          <el-input v-model="cform.tds" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="temperature"
          label-width="100px"
          prop="temperature"
        >
          <el-input v-model="cform.temperature" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="turbidity" label-width="100px" prop="turbidity">
          <el-input v-model="cform.turbidity" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogStateVisible = false">取 消</el-button>
        <el-button type="primary" @click="changeState">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 放置表格 -->
    <!-- <el-table
        style="width: 100%"
        border
        :data="sList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          prop="prop"
          label="序号"
          width="80"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="walkingStickId"
          label="拐杖编号"
          width="140"
        ></el-table-column>
        <el-table-column
          prop="activationTime"
          label="上次登录时间"
          width="200"
        ></el-table-column>
        <el-table-column prop="sList" label="属性" width="width">
          <template slot-scope="{ row, $index }">
            <el-tag type="success" style="margin: 0px 5px"
              >IP:{{ row.province }}</el-tag
            >
            <el-tag type="success" style="margin: 0px 5px">{{
              row.operatingFrequency
            }}</el-tag>
            <el-tag type="success" style="margin: 0px 5px">{{
              row.bindingState
            }}</el-tag>
          </template></el-table-column
        >
        <el-table-column prop="prop" label="操作" width="300">
          <template slot-scope="{ row, $index }">
            <el-button
              type="info"
              icon="el-icon-info"
              size="mini"
              @click="showInfo(row)"
              >查看</el-button
            >
            <el-button
              type="warning"
              icon="el-icon-edit"
              size="mini"
              @click="showState(row)"
              >解绑</el-button
            >
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              @click="deleteStick(row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table> -->
    <!-- 查看详细信息 -->
    <!-- <el-dialog title="用户状态" :visible.sync="dialogInfoVisible">
        <el-form style="width: 80%" :model="sForm">
          <el-form-item label="当前用户状态" label-width="100px">
            <span>{{ sForm.bindingState }}</span>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogStateVisible = false">取消</el-button>
          <el-button type="primary" @click="changeState">解禁/禁用</el-button>
        </div>
      </el-dialog> -->
    <!-- 修改拐杖状态 -->
    <!-- <el-dialog title="用户状态" :visible.sync="dialogStateVisible">
        <el-form style="width: 80%" :model="sForm">
          <el-form-item label="当前用户状态" label-width="100px">
            <span>{{ sForm.bindingState }}</span>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogStateVisible = false">取消</el-button>
          <el-button type="primary" @click="changeState">解禁/禁用</el-button>
        </div>
      </el-dialog> -->
    <!-- <el-pagination
        style="text-align: center"
        :total="total"
        :current-page="page"
        :page-size="limit"
        :page-sizes="[3, 5, 10]"
        layout="prev,pager,next,jumper,->,sizes,total"
        @current-change="getStickList"
        @size-change="handleSizeChange"
      ></el-pagination> -->
  </div>
</template>
  
  <script>
import * as echarts from "echarts";
export default {
  name: "monitor",
  data() {
    return {
      wForm: {},
      dialogFormVisible: false,
      batchDeleteArr: [],
      sList: [],
      dialogStateVisible: false,
      dialogInfoVisible: false,
      cform: {},
      //当前页数
      page: 1,
      //一页展示的条数
      limit: 5,
      //总共数据条数
      total: 0,
      stickForm: {},
      tableData: [
        {
          station: "01",
          ph: "6.75",
          Do: "0.2",
          target: "合格",
        },
        {
          station: "02",
          ph: "7.55",
          Do: " 0.35",
          target: "合格",
        },
        {
          station: "03",
          ph: "6.83",
          Do: "0.38",
          target: "合格",
        },
        {
          station: "04",
          ph: "7.46",
          Do: "0.45",
          target: "合格",
        },
        {
          station: "01",
          ph: "6.75",
          Do: "0.2",
          target: "合格",
        },
        {
          station: "02",
          ph: "7.55",
          Do: " 0.35",
          target: "合格",
        },
        {
          station: "03",
          ph: "6.83",
          Do: "0.38",
          target: "合格",
        },
        {
          station: "04",
          ph: "7.46",
          Do: "0.45",
          target: "合格",
        },
      ],
      reqData: [],
      areaslist: [],
      Form: {},
      rules: {
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            min: 2,
            max: 10,
            message: "长度在 2 到 10 个字符",
            trigger: "blur",
          },
        ],
        logoUrl: [{ required: true, message: "请选择品牌图片" }],
      },
      sourceData: [],
      option: {
        dataset: [
          {
            dimensions: ["area", "day"],
            source: [],
          },
          {
            transform: {
              type: "sort",
              config: { dimension: "day", order: "desc" },
            },
          },
        ],
        xAxis: {
          type: "category",
          axisLabel: { interval: 0, rotate: 30 },
        },
        yAxis: {},
        series: {
          type: "bar",
          encode: { x: "area", y: "day" },
          datasetIndex: 1,
        },
      },
    };
  },
  mounted() {
    this.getsubstandardData();
    // this.initEcharts();
    this.getAreas();
  },
  watch: {
    stickForm() {
      this.getStickList();
    },
    deep: true,
  },
  methods: {
    // 展示添加数据弹窗
    showDialog() {
      this.dialogFormVisible = true;
      this.wForm = {
        area: "",
        conductivity: "",
        ph: "",
        station: "",
        tds: "",
        temperature: "",
        turbidity: "",
      };
    },
    async getAreas() {
      let res = await this.$API.water.reqAllAreas();
      this.areaslist = res.data;
    },
    async getWaterData() {
      let data = {
        area: this.Form.area,
        day: this.getNowFormatDate(),
      };
      let res = await this.$API.water.reqQualityDay(data);
      this.reqData = res.data;
      if (this.reqData.length == 0) {
        this.$message({
          message: "暂时没有数据！",
          type: "warning",
        });
      }
    },
    //传递绑定状态
    getStickForm(form) {
      this.stickForm = form;
      this.getStickList();
    },
    // 查看详细信息
    showInfo(row) {
      // console.log(row, "row");
      // this.sForm = { ...row };
      // this.dialogStateVisible = true;
      this.cform = { ...row };
      this.dialogStateVisible = true;
    },
    showState(row) {
      this.cform = { ...row };
      this.dialogStateVisible = true;
    },
    // 修改水质信息
    async changeState() {
      console.log(this.cform);
      // let res = await this.$API.stick.reqChangeState();
      // console.log(res);
      // this.getStickList();
      let res = await this.$API.water.changeWaterData(this.cform);
      if (res.code == 200) {
        if (res.code == 200) {
          this.$message({
            type: "success",
            message: "修改成功!",
          });
          //再次获取最新水质数据
          this.getWaterData();
        }
      }
    },
    async deleteStick(row) {
      //弹框
      this.$confirm("确认删除该用户?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          //当用户点击确定按钮的时候会出发
          //向服务器发请求
          let res = await this.$API.water.deleteWaterData(row.id);
          //如果删除成功
          if (res.code == 200) {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            //再次获取最新水质数据
            this.getWaterData();
          }
        })
        .catch(() => {
          //当用户点击取消按钮的时候会触发
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    handleSelectionChange(val) {
      this.batchDeleteArr = val;
    },
    // 批量删除
    async batchDelete() {
      let deleteData = this.batchDeleteArr.map((item) => {
        const { walkingStickId } = item;
        return walkingStickId;
      });
      this.$confirm("确认批量删除该用户?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          //当用户点击确定按钮的时候会出发
          //向服务器发请求
          let res = await this.$API.stick.reqDeleteSticks(deleteData);
          //如果删除成功
          if (res.code == 200) {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            //再次获取品牌列表数据
            this.getStickList(
              this.sList.length > 1 ? this.page : this.page - 1
            );
          }
        })
        .catch(() => {
          //当用户点击取消按钮的时候会触发
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    initEcharts() {
      const myChart = echarts.init(document.getElementById("sortchart")); // 图标初始化
      myChart.setOption(this.option); // 渲染页面
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    async addOrUpdata() {
      console.log("添加或者更新数据");
      let res = await this.$API.water.addWaterData(this.wForm);
      if (res.code == 200) {
        this.dialogFormVisible = false;
        this.$message({
          message: "添加成功",
          type: "success",
        });
        this.getWaterData();
      }
    },
    async getsubstandardData() {
      let res = await this.$API.water.reqSubstandardData();
      console.log(res);
      if (res.code == 200) {
        let temp = [];
        for (let i in res.data.areas) {
          temp[i] = [res.data.areas[i], res.data.days[i]];
        }
        console.log(temp);
        this.option.dataset[0].source = temp;
        this.initEcharts();
      }
    },
  },
};
</script>
  
<style>
.titleFont {
  text-align: left;
  font-weight: 600;
  font-size: 24px;
  margin-left: 20px;
  margin-bottom: 15px;
}
</style>