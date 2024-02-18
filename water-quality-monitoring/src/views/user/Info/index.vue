<template>
  <div>
    <!-- 按钮 -->
    <el-button
      type="primary"
      icon="el-icon-plus"
      style="margin: 10px 0px"
      @click="showDialog"
      >添加</el-button
    >
    <!-- 表格 -->
    <el-table style="width: 100%" border :data="list">
      <el-table-column
        type="index"
        prop="prop"
        label="序号"
        width="80"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="userName"
        label="用户名"
        width="width"
      ></el-table-column>
      <el-table-column
        prop="phoneNumber"
        label="电话号码"
        width="width"
      ></el-table-column>
      <el-table-column
        prop="email"
        label="邮箱地址"
        width="width"
      ></el-table-column>
      <el-table-column prop="userId" label="用户编号" width="width">
        <!-- <template slot-scope="{ row, $index }">
          <img :src="row.logoUrl" style="width: 100px; height: 100px" />
        </template> -->
      </el-table-column>
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
            @click="updateUserInfo(row)"
            >修改状态</el-button
          >
          <!-- <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            @click="deleteTradeMark(row)"
            >删除</el-button
          > -->
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
      @current-change="getUserList"
      @size-change="handleSizeChange"
    ></el-pagination>

    <!-- 查看详细信息 -->
    <el-dialog title="用户信息" :visible.sync="dialogTableVisible">
      <el-form
        style="width: 80%"
        :model="userForm"
        :rules="rules"
        ref="ruleForm"
      >
        <el-form-item label="用户名" label-width="100px">
          <span>{{ userForm.userName }}</span>
        </el-form-item>
        <el-form-item label="状态" label-width="100px">
          <span>{{ userForm.userStatus }}</span>
          <!-- <textarea v-model.sync="userForm.userStatus"
            >{{ userForm.userStatus }}}</textarea
          > -->
          <!-- <span v-show="!userForm.userStatus">正常使用</span>
          <span v-show="userForm.userStatus">禁用状态</span> -->
        </el-form-item>
        <el-form-item label="用户编号" label-width="100px">
          <span>{{ userForm.userId }}</span>
        </el-form-item>
        <el-form-item label="电话号码" label-width="100px">
          <span>{{ userForm.phoneNumber }}</span>
        </el-form-item>
        <el-form-item label="最近登录时间" label-width="100px">
          <span>{{ userForm.loginTime }}</span>
        </el-form-item>
        <el-form-item label="最近登录IP" label-width="100px">
          <span>{{ userForm.loginIp }}</span>
        </el-form-item>
        <el-form-item label="绑定拐杖编号" label-width="100px">
          <span>{{ userForm.walkingStickId }}</span>
        </el-form-item>
        <!-- <el-form-item label="最近登录IP" label-width="100px">
          <span>{{ userForm.userPhone }}</span>
        </el-form-item> -->
      </el-form>
    </el-dialog>

    <!-- 添加用户信息 visible.sync控制对话框显示与隐藏-->
    <el-dialog title="用户信息" :visible.sync="dialogFormVisible">
      <el-form
        style="width: 80%"
        :model="userForm"
        :rules="rules"
        ref="ruleForm"
      >
        <el-form-item label="用户名" label-width="100px" prop="userName">
          <el-input v-model="userForm.userName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话号码" label-width="100px" prop="userPhone">
          <el-input v-model="userForm.userPhone" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱地址" label-width="100px" prop="userEmail">
          <el-input v-model="userForm.userEmail" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addOrUpdataTradeMark"
          >确 定</el-button
        >
      </div>
    </el-dialog>

    <!-- 修改用户状态 -->
    <el-dialog title="用户状态" :visible.sync="dialogStateVisible">
      <el-form
        style="width: 80%"
        :model="userForm"
        :rules="rules"
        ref="ruleForm"
      >
        <el-form-item label="当前用户状态" label-width="100px">
          <span>{{ uState }}</span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogStateVisible = false">取 消</el-button>
        <el-button type="primary" @click="changeUserState">解禁/禁用</el-button>
      </div>
    </el-dialog>
  </div>
</template>

  <script>
export default {
  name: "Info",
  data() {
    return {
      //当前页数
      page: 1,
      //一页展示的条数
      limit: 5,
      //总共数据条数
      total: 0,
      //列表展示的数据，请求回来的所有数据
      list: [
        // {
        //   userName: "tx666",
        //   userPhone: "18128746979",
        //   userId: "548756860-2784",
        //   userEmail: "2589208035@qq.com",
        //   time: "2023/3/24 22:48",
        //   IP: "192.168.10.11",
        // },
        // {
        //   userName: "tx",
        //   userPhone: "18128746979",
        //   userId: "548756860-2784",
        //   userEmail: "2589208035@qq.com",
        //   time: "2023/3/24 22:48",
        //   IP: "192.168.10.11",
        // },
      ],
      dialogFormVisible: false,
      dialogTableVisible: false,
      dialogStateVisible: false,
      //可修改||可添加需要填写的数据
      userForm: {
        userName: "",
        userPhone: "",
        userId: "",
        userEmail: "",
      },
      uState: "",
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
    };
  },
  watch: {
    "userForm.userStatus"(noOld, old) {
      return (this.uState = noOld == 0 ? "正常" : "禁用");
    },
    deep: true,
  },
  mounted() {
    this.getUserList();
  },

  methods: {
    async getUserList(pager = 1) {
      this.page = pager;
      const { page, limit } = this;
      // 发请求
      let res = await this.$API.userInfo.reqUserList(this.page, this.limit);
      console.log(res.data.result);
      if (res.code == 200) {
        this.total = res.data.total;
        this.list = res.data.result;
      }
    },
    handleSizeChange(limit) {
      this.limit = limit;
      this.getUserList(this.page);
    },
    showDialog() {
      this.dialogFormVisible = true;
      this.userForm = {
        userName: "",
        userPhone: "",
        userId: "",
        userEmail: "",
      };
    },

    // 查看用户的详细信息
    checkUserInfo(row) {
      this.dialogTableVisible = true;
      console.log(row);
      //将该行信息赋值给表单进行展示
      this.userForm = { ...row };
      // 用该值请求详细信息
      // this.userForm.userId;
      // console.log(this.reqId);
    },
    //修改平台信息
    async updateUserInfo(row) {
      console.log("row", row);
      //弹出弹窗
      this.dialogStateVisible = true;
      //将该行信息赋值给表单进行展示
      this.userForm = { ...row };
    },

    // 修改用户状态
    async changeUserState() {
      let data = {
        userId: this.userForm.userId,
        userStatus: this.userForm.userStatus == 0 ? 1 : 0,
      };
      // let res = await this.$API.userInfo.requpdateUserStatus(
      //   this.userForm.userId,
      //   this.userForm.userStatus
      // );
      let res = await this.$API.userInfo.requpdateUserStatus(data);
      console.log(res);
      if (res.code == 200) {
        this.getUserList();
        this.$message({
          type: "success",
          message: "修改成功",
        });
      }
    },
    //添加或修改品牌
    addOrUpdataTradeMark() {
      this.$refs.ruleForm.validate(async (success) => {
        if (success) {
          this.dialogFormVisible = false;
          //发请求
          let res = await this.$API.trademark.reqAddOrUpdataTradeMark(
            this.userForm
          );
          if (res.code == 200) {
            //弹出信息
            this.$message({
              type: "success",
              message: this.userForm.id ? "修改品牌成功" : "添加品牌成功",
            });
            this.getUserList(this.userForm.id ? this.page : 1);
          }
        } else {
          console.log("error sumbit!");
          return false;
        }
      });
    },
    //删除操作
    deleteTradeMark(row) {
      //弹框
      this.$confirm("确认删除该用户?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          //当用户点击确定按钮的时候会出发
          //向服务器发请求
          let result = await this.$API.trademark.reqDeleteTradeMark(row.id);
          //如果删除成功
          if (result.code == 200) {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            //再次获取品牌列表数据
            this.getUserList(this.list.length > 1 ? this.page : this.page - 1);
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
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>