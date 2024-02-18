<template>
  <div>
    <el-card style="margin: 20px 0">
      <CategorySelect
        @getScreeningCondition="getScreeningCondition"
      ></CategorySelect>
    </el-card>
    <el-card>
      <el-button
        type="danger"
        icon="el-icon-delete"
        style="margin-bottom: 5px; float: right"
        :disabled="batchDeleteArr.length === 0"
        @click="batchDelete"
        >批量删除</el-button
      >
      <!-- 放置表格 -->
      <el-table
        style="width: 100%"
        border
        stripe
        :data="historyData"
        :default-sort="{ prop: 'date', order: 'descending' }"
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
          prop="date"
          sortable
          label="监测时间"
          width="200"
        ></el-table-column>
        <el-table-column
          prop="station"
          label="监测船"
          width="150"
        ></el-table-column>
        <el-table-column label="水质数据">
          <el-table-column
            prop="ph"
            label="酸碱度(PH)"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="tds"
            label="溶解氧(DO mg/L)"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="turbidity"
            label="浊度(NTU)"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="temperature"
            label="水温(摄氏度)"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="conductivity"
            label="导电性"
            width="100"
          ></el-table-column>
        </el-table-column>
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
              icon="el-icon-edit"
              size="mini"
              @click="showState(row)"
              >编辑</el-button
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
      <!-- 查看详细信息 -->
      <el-dialog title="用户状态" :visible.sync="dialogInfoVisible">
        <el-form style="width: 80%" :model="sForm">
          <el-form-item label="当前用户状态" label-width="100px">
            <span>{{ sForm.bindingState }}</span>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogStateVisible = false">取消</el-button>
          <el-button type="primary" @click="changeState">解禁/禁用</el-button>
        </div>
      </el-dialog>
      <!-- 修改拐杖状态 -->
      <el-dialog title="用户状态" :visible.sync="dialogStateVisible">
        <el-form style="width: 80%" :model="sForm">
          <el-form-item label="当前用户状态" label-width="100px">
            <span>{{ sForm.bindingState }}</span>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogStateVisible = false">取消</el-button>
          <el-button type="primary" @click="changeState">解禁/禁用</el-button>
        </div>
      </el-dialog>
      <el-pagination
        style="text-align: center"
        :total="total"
        :current-page="page"
        :page-size="limit"
        :page-sizes="[3, 5, 10]"
        layout="prev,pager,next,jumper,->,sizes,total"
        @current-change="getWaterDataList"
        @size-change="handleSizeChange"
      ></el-pagination>
    </el-card>
  </div>
</template>
    
    <script>
export default {
  name: "monitor",
  data() {
    return {
      batchDeleteArr: [],
      sList: [],
      dialogStateVisible: false,
      dialogInfoVisible: false,
      sForm: {},
      //当前页数
      page: 1,
      //一页展示的条数
      limit: 5,
      //总共数据条数
      total: 0,
      historyData: [],
      ScreeningCondition: {},
    };
  },
  watch: {
    ScreeningCondition() {
      this.getWaterDataList();
    },
    deep: true,
  },
  methods: {
    //传递绑定状态
    getScreeningCondition(form) {
      this.ScreeningCondition = form;
      this.getWaterDataList();
    },
    async getWaterDataList(pager = 1) {
      this.page = pager;
      const { page, limit } = this;
      // let res = await this.$API.stick.reqStickList(page, limit, this.stickForm);
      // station, startDate, endDate, pageSize, pageNum
      let data = {
        area: this.ScreeningCondition.area,
        pageSize: limit,
        pageNum: page,
        endDate: this.ScreeningCondition.date[1],
        stardDate: this.ScreeningCondition.date[0],
        station: this.ScreeningCondition.station,
      };
      // let res = await this.$API.water.reqHistoricalWaterQualityData(
      //   this.ScreeningCondition.station,
      //   this.ScreeningCondition.date[0],
      //   this.ScreeningCondition.date[1],
      //   limit,
      //   page
      // );
      let res = await this.$API.water.reqHistoricalWaterQualityData(data);
      this.historyData = res.data.records;
      this.total = res.data.total;
    },
    handleSizeChange(limit) {
      this.limit = limit;
      this.getWaterDataList(this.page);
    },
    showInfo(row) {
      console.log(row, "row");
      this.sForm = { ...row };
      this.dialogStateVisible = true;
    },
    showState(row) {
      this.sForm = { ...row };
      this.dialogStateVisible = true;
    },
    async changeState() {
      let data = {
        bindingState: this.sForm.bindingState == "已绑定" ? "未绑定" : "已绑定",
        walkingStickId: this.sForm.walkingStickId,
      };
      let res = await this.$API.stick.reqChangeState(data);
      console.log(res);
      this.getWaterDataList();
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
          let res = await this.$API.stick.reqDeleteStick(row.walkingStickId);
          //如果删除成功
          if (res.code == 200) {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            //再次获取品牌列表数据
            this.getWaterDataList(
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
            this.getWaterDataList(
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
  },
};
</script>
    
    <style>
</style>