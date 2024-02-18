<template>
  <div>
    <el-form :inline="true" :model="Form">
      <el-form-item label="监测区域">
        <el-select
          placeholder="请选择"
          v-model="Form.area"
          @change="getStationData"
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
      icon="el-icon-plus"
      style="margin: 10px 5px"
      @click="dialogArea = true"
      >添加流域</el-button
    >
    <el-button
      type="primary"
      icon="el-icon-plus"
      style="margin: 10px 5px"
      @click="dialogShip = true"
      >添加检测船</el-button
    >
    <el-table style="width: 100%" border :data="list">
      <el-table-column
        type="index"
        prop="prop"
        label="序号"
        width="80"
        align="center"
      ></el-table-column>
      <el-table-column prop="area" label="流域" width="width"></el-table-column>
      <el-table-column
        prop="name"
        label="检样船编号"
        width="width"
      ></el-table-column>
      <el-table-column prop="prop" label="操作" width="width">
        <template slot-scope="{ row, $index }">
          <el-button
            type="info"
            icon="el-icon-info"
            size="mini"
            @click="checkUserInfo(row)"
            >查看</el-button
          >
          <el-button
            type="warning"
            icon="el-icon-edit"
            size="mini"
            @click="updateStationBind(row)"
            >修改绑定流域</el-button
          >
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            @click="deleteShip(row)"
            >删除船</el-button
          >
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            @click="deleteShip(row)"
            >删除流域</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="用户状态" :visible.sync="dialogBinding">
      <el-form style="width: 80%" :model="shipForm">
        <el-form-item label="当前监测流域" label-width="100px">
          <el-input v-model="shipForm.area" placeholder=""></el-input>
        </el-form-item>
        <el-button type="primary" @click="changeBind">确定</el-button>
      </el-form>
    </el-dialog>
    <el-dialog title="新增监测船" :visible.sync="dialogShip">
      <el-form style="width: 80%" :model="addShipForm">
        <el-form-item label="监测流域" label-width="100px">
          <el-input v-model="addShipForm.area" placeholder=""></el-input>
        </el-form-item>
        <el-form-item label="监测船名" label-width="100px">
          <el-input v-model="addShipForm.name" placeholder=""></el-input>
        </el-form-item>
        <el-button type="primary" @click="addShip">确定</el-button>
      </el-form>
    </el-dialog>
    <el-dialog title="新增监测区域" :visible.sync="dialogArea">
      <el-form style="width: 80%" :model="addShipForm">
        <el-form-item label="监测流域" label-width="100px">
          <el-input v-model="addAreaForm.area" placeholder=""></el-input>
        </el-form-item>
        <el-button type="primary" @click="addArea">确定</el-button>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      areaslist: [],
      Form: {},
      list: [],
      dialogBinding: false,
      dialogShip: false,
      dialogArea: false,
      shipForm: {},
      addShipForm: {},
      addAreaForm: {},
    };
  },
  mounted() {
    this.getAreas();
  },
  methods: {
    async getStationData() {
      let res = await this.$API.station.reqStationList(this.Form.area);
      console.log(res);
      this.list = res.data;
    },
    async getAreas() {
      let res = await this.$API.water.reqAllAreas();
      this.areaslist = res.data;
    },
    updateStationBind(row) {
      this.dialogBinding = true;
      console.log(row);
      this.shipForm = { ...row };
      console.log(this.shipForm);
    },
    async changeBind() {
      this.dialogBinding = false;
      let data = {
        area: this.shipForm.area,
        id: this.shipForm.id,
        name: this.shipForm.name,
      };
      console.log(data);
      let res = await this.$API.station.reqChangeShipInfo(data);
      if (res.code == 200) {
        this.getStationData();
      }
    },
    async deleteShip(row) {
      //弹框
      this.$confirm("确认删除该用户?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          //当用户点击确定按钮的时候会出发
          //向服务器发请求
          let res = await this.$API.station.reqDeleteShip(row.id);
          //如果删除成功
          if (res.code == 200) {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            //再次获取品牌列表数据
            this.getStationData();
          } else if (res.code == 500) {
            this.$message({
              type: "info",
              message: res.msg,
            });
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
    async addShip() {
      let res = await this.$API.station.reqAddShip(this.addShipForm);
      console.log(res);
      if (res.code == 200) {
        this.dialogShip = false;
      }
    },
    async addArea() {
      let res = await this.$API.station.reqAddArea(this.addAreaForm);
      console.log(res);
      this.dialogArea = false;
    },
  },
};
</script>

<style lang="scss" scoped>
</style>