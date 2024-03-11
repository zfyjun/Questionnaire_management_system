<template>
  <div>

    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-search" v-model="address"></el-input>
      <el-input style="width: 200px" placeholder="请输入学号" suffix-icon="el-icon-search" v-model="stuid"></el-input>
      <!--      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference" >批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange"  >
      <el-table-column type="selection" width="55" class="table-style"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="姓名"></el-table-column>
      <el-table-column prop="age" label="年龄"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column prop="birthPlace" label="籍贯"></el-table-column>
      <el-table-column prop="phone" label="电话"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="sex" label="性别"></el-table-column>
      <el-table-column prop="stuid" label="学号"></el-table-column>
      <el-table-column prop="date" label="报道日期"></el-table-column>
      <el-table-column label="操作"  width="280" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
          </template>
        </el-table-column>


    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>

      <el-dialog title="学生信息" :visible.sync="dialogFormVisible" width="30%" >
        <el-form>
        <el-form-item label="姓名">
          <el-input v-model="form.name" ></el-input>
        </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model="form.age" ></el-input>
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="form.address" ></el-input>
          </el-form-item>
          <el-form-item label="籍贯">
            <el-input v-model="form.birthplace" ></el-input>
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="form.phone" ></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" ></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-input v-model="form.sex" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="学号">
            <el-input v-model="form.stuid" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>


    </div>



  </div>
</template>

<script>
export default {
  name: "Student",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      name: "",
      stuid:"",
      address: "",
      birthplace:"",
      sex:"",
      form: {},
      dialogFormVisible: false,
      menuDialogVis: false,
      multipleSelection: [],
      courses:[]
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/student/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          address: this.address,
          name:this.name,
          stuid:this.stuid,
        }
      }).then(res => {
        this.tableData = res.data.records
        console.log(res)
        this.total = res.data.total
      })
    },
    save() {
      this.request.post("/student", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/student/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/student/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.address = ""
      this.stuid = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    //
    async selectMenu(role) {


      this.roleId = role.id
      this.roleFlag = role.flag

      // 请求菜单数据
      // this.request.get("/menu").then(res => {
      //   this.menuData = res.data
      //
      //   // 把后台返回的菜单数据处理成 id数组
      //   this.expends = this.menuData.map(v => v.id)
      // })

      // this.request.get("/role/roleMenu/" + this.roleId).then(res => {
      //   this.checks = res.data
      //
      //   this.request.get("/menu/ids").then(r=>{
      //     const ids=r.data
      //     ids.forEach(id=>{
      //       if(!this.checks.includes(id)){
      //         this.$refs.tree.setChecked(id,false)
      //       }
      //     })
      //   })
      //
      //   // this.ids.forEach(id => {
      //   //   if (!this.checks.includes(id)) {
      //   //     // 可能会报错：Uncaught (in promise) TypeError: Cannot read properties of undefined (reading 'setChecked')
      //   //     this.$nextTick(() => {
      //   //       this.$refs.tree.setChecked(id, false)
      //   //     })
      //   //   }
      //   // })
      //
      //   this.menuDialogVis = true
      // })


    },
    saveRoleMenu() {
      // this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
      //   if (res.code === '200') {
      //     this.$message.success("绑定成功")
      //     this.menuDialogVis = false
      //
      //     // 操作管理员角色后需要重新登录
      //     if (this.roleFlag === 'ROLE_ROOT') {
      //       // this.$store.commit("logout")
      //       this.$router.push("/login")
      //     }
      //
      //   } else {
      //     this.$message.error(res.msg)
      //   }
      // })
    },


  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
.table-style{
  background-color: #095fcc;
}

</style>
