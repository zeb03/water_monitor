<template>
  <div>
    <el-card style="margin: 20px 0">
      <el-button
        type="primary"
        icon="el-icon-plus"
        @click="dialogAddVisible = true"
        >添加管理员</el-button
      >
    </el-card>
    <el-card>
      <!-- 放置表格 -->
      <el-table style="width: 100%" border :data="list">
        <el-table-column
          type="index"
          prop="prop"
          label="序号"
          width="80"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="adminName"
          label="姓名"
          width="140"
        ></el-table-column>
        <el-table-column
          prop="adminId"
          label="工号"
          width="200"
        ></el-table-column>
        <el-table-column
          prop="phoneNumber"
          label="电话号码"
          width="200"
        ></el-table-column>
        <el-table-column
          prop="loginTime"
          label="上次登录时间"
          width="width"
        ></el-table-column>
        <el-table-column prop="prop" label="操作" width="300">
          <template slot-scope="{ row, $index }">
            <el-button
              type="info"
              icon="el-icon-info"
              size="mini"
              @click="checkAdministratorInfo(row)"
              >查看</el-button
            >
            <el-button
              type="warning"
              icon="el-icon-edit"
              size="mini"
              @click="updateAdministratorInfo(row)"
              >编辑</el-button
            >
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              @click="delectAdmin(row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页器 -->
      <el-pagination
        style="text-align: center"
        :total="total"
        :current-page="page"
        :page-size="limit"
        :page-sizes="[3, 5, 10]"
        layout="prev,pager,next,jumper,->,sizes,total"
        @current-change="getAdministratorList"
        @size-change="handleSizeChange"
      ></el-pagination>
      <!-- 查看管理员的详细信息 -->
      <el-dialog :title="aList.adminName" :visible.sync="dialogTableVisible">
        <el-form style="width: 80%" :model="aList" label-position="left">
          <el-form-item label="工号" label-width="100px">
            <span>{{ aList.adminId }}</span>
          </el-form-item>
          <el-form-item label="长号" label-width="100px">
            <span>{{ aList.phoneNumber }}</span>
          </el-form-item>
          <el-form-item label="创建时间" label-width="100px">
            <span>{{ aList.adminCreateData }}</span>
          </el-form-item>
          <el-form-item label="登录IP" label-width="100px">
            <span>{{ aList.loginIp }}</span>
          </el-form-item>
          <el-form-item label="工作邮箱" label-width="100px">
            <span>{{ aList.email }}</span>
          </el-form-item>
          <el-form-item label="最近登录IP" label-width="100px">
            <span>{{ aList.loginIp }}</span>
          </el-form-item>
        </el-form>
      </el-dialog>
      <!-- 修改管理员信息 -->
      <el-dialog title="管理员信息" :visible.sync="dialogStateVisible">
        <el-form style="width: 80%" :model="aList" label-position="left">
          <el-form-item label="用户名" label-width="100px" prop="userName">
            <el-input v-model="aList.adminName" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="电话号码" label-width="100px" prop="userPhone">
            <el-input
              v-model="aList.phoneNumber"
              auto-complete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="工作邮箱" label-width="100px" prop="userEmail">
            <el-input v-model="aList.email" auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button
            @click="
              {
                dialogStateVisible = false;
                aList = {};
              }
            "
            >取 消</el-button
          >
          <el-button type="primary" @click="changeAdminList">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 添加管理员 -->
      <el-dialog title="管理员信息" :visible.sync="dialogAddVisible">
        <el-form style="width: 80%" :model="aForm" label-position="left">
          <el-form-item label="用户名" label-width="100px" prop="userName">
            <el-input v-model="aForm.adminName" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="电话号码" label-width="100px" prop="userPhone">
            <el-input
              v-model="aForm.phoneNumber"
              auto-complete="off"
            ></el-input> </el-form-item
          ><el-form-item label="密码" label-width="100px" prop="userName">
            <el-input v-model="aForm.password" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="工作邮箱" label-width="100px" prop="userEmail">
            <el-input v-model="aForm.email" auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogStateVisible = false">取 消</el-button>
          <!-- <el-button @click="changeAdminList">取 消</el-button> -->
          <el-button type="primary" @click="addAdministrator">确 定</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Root",
  data() {
    return {
      //当前页数
      page: 1,
      //一页展示的条数
      limit: 5,
      //总共数据条数
      total: 0,
      // 表格数据列表
      list: [],
      // 行数据
      aList: {},
      aForm: {},
      //详细弹窗
      dialogTableVisible: false,
      //修改弹窗
      dialogStateVisible: false,
      dialogAddVisible: false,
    };
  },
  mounted() {
    this.getAdministratorList();
  },
  methods: {
    //请求管理员列表
    async getAdministratorList(pager = 1) {
      this.page = pager;
      const { page, limit } = this;
      // 发请求
      let res = await this.$API.userInfo.reqAdministratorList(
        this.page,
        this.limit
      );
      console.log(res.data.result);
      if (res.code == 200) {
        this.total = res.data.total;
        this.list = res.data.result;
      }
    },
    // 改变分页
    handleSizeChange(limit) {
      this.limit = limit;
      this.getAdministratorList(this.page);
    },

    // 查看管理员详细信息
    checkAdministratorInfo(row) {
      //弹出弹窗
      this.dialogTableVisible = true;
      //将该行信息赋值给表单进行展示
      this.aList = { ...row };
    },

    //修改管理员信息
    async updateAdministratorInfo(row) {
      console.log("row", row);
      //弹出弹窗
      this.dialogStateVisible = true;
      //将该行信息赋值给表单进行展示
      this.aList = { ...row };
    },
    //确定修改管理员的信息
    async changeAdminList() {
      let data = {
        adminId: this.aList.adminId,
        adminName: this.aList.adminName,
        email: this.aList.email,
        phoneNumber: this.aList.phoneNumber,
      };
      let res = await this.$API.userInfo.requpdateAdministratorInfo(data);
      console.log(res);
      if (res.code == 200) {
        this.getAdministratorList();
        this.aList = [];
        this.$message({
          type: "success",
          message: "修改成功",
        });
      }
    },
    //delect
    delectAdmin(row) {
      //弹框
      this.$confirm("确认删除该用户?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          //当用户点击确定按钮的时候会出发
          //向服务器发请求
          let result = await this.$API.userInfo.reqDeleteAdmin(row.adminId);
          //如果删除成功
          if (result.code == 200) {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            this.getAdministratorList();
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
    //添加管理员
    async addAdministrator() {
      // "adminName": "string",
      // "age": 0,
      // "email": "string",
      // "password": "string",
      // "phoneNumber": "string",
      // "sex": "string"
      let res = await this.$API.userInfo.reqAddAdmin(this.aForm);
      console.log(res);
      if (res.code == 200) {
        this.getAdministratorList();
        this.$message({
          type: "success",
          message: "添加成功!",
        });
        this.dialogAddVisible = false;
      }
    },
  },
};
</script>

<style>
</style>