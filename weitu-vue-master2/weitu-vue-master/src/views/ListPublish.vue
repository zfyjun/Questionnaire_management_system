<template>
  <div v-loading="loading">
    <div>
      <el-divider content-position="left" style="margin-bottom: 40px">
        <div class="form-toggle">
          <b :class="{'active':listState==='未发布'}" @click="changeListState('未发布')">未发布</b>
          <b :class="{'active':listState==='已发布'}" style="margin-left: 30px" @click="changeListState('已发布')">已发布</b>
        </div>
      </el-divider>
    </div>
      <div v-for="item in tableData" :key="item.id" style="display: inline-block;margin-top: 20px">
        <el-card v-if="item.state===listState&& item.isDelete==0" style="margin-top: 20px;width: 350px;margin-left: 50px">
          <div style="margin: 10px 0; font-size: 20px">
            <span style="font-size: 14px">{{item.listname}}</span>
            <template>
              <el-popconfirm
                  class="ml-5"
                  confirm-button-text='确定'
                  cancel-button-text='我再想想'
                  icon="el-icon-info"
                  icon-color="red"
                  title="您确定删除这些数据吗？"
                  @confirm="deletelist(item.id)"
              >
                <el-button slot="reference" style="float: right;margin-left: 5px;color: red" type="text" ><i class="el-icon-delete-solid" style="font-size: 15px"></i></el-button>
              </el-popconfirm>
            </template>

            <template v-if="listState=='未发布'">
              <el-popconfirm
                  class="ml-5"
                  confirm-button-text='确定'
                  cancel-button-text='我再想想'
                  icon="el-icon-info"
                  icon-color="red"
                  title="您确定发布这份调查问卷吗？"
                  @confirm="publish(item.id)"
              >
              <el-button slot="reference" style="float: right;margin-left: 5px;color: orange" type="text" >发布</el-button>
              </el-popconfirm>
            </template>

            <el-tooltip v-if="listState=='已发布'" content="点击查看分配为获取改调查问卷的学生" placement="bottom" effect="light">
              <el-button style="float: right;margin-left: 5px" type="text" @click="addstudent(item.id)">学生分配</el-button>
            </el-tooltip>

            <el-tooltip v-if="listState=='未发布'" content="点击进入编辑界面" placement="bottom" effect="light">
              <el-button style="float: right;margin-left: 5px" type="text" @click="editlist(item.id)">编辑</el-button>
            </el-tooltip>
          </div>
        </el-card>
      </div>
    <div>
      <el-dialog title="未获取学生名单" :visible.sync="dialogTableVisible">
        <el-table ref="multipleTable" :data="gridData" tooltip-effect="dark" style="width: 100%"
                  @selection-change="handleSelectionChange"
                  border stripe>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="name" label="学生姓名" width="auto">
          </el-table-column>
          <el-table-column prop="stuid" label="学生学号" width="auto" ></el-table-column>
        </el-table>
        <el-button @click="comfrim" size="mini" style="margin-top: 10px">确 定</el-button>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "ListPublish",
  data(){
    return {
      tableData:[],
      form:{},
      listState:'未发布',
      dialogTableVisible:false,
      gridData:[],
      loading:false,
      multipleSelection:[],
      lid:0
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/List").then(res => {
        this.tableData = res.data
        console.log(res.data)
      })
    },
    editlist(id){//编辑表单
      localStorage.setItem("id",id)
      this.$router.push("/listEdit")
    },
    handleSelectionChange(val) {
      this.multipleSelection=val
    },
    addstudent(lid){//分配学生
      this.loading=true
      this.lid=lid
      this.request.get("/Plist/getstudent",{
        params: {
          lid: lid
        }
      }).then(res => {
        if(res.code==='200'){
          this.gridData=res.data
          this.dialogTableVisible=true
        }
        else {
          this.$message({
            type:"error",
            message:res.msg
          })
        }
        this.loading=false
      })
    },
    publish(id) {
      this.form=JSON.parse(id);
      this.save();
    },
    deletelist(id){
      this.form=JSON.parse(id);
      this.del();
    },
    save() {
      this.request.post("/List", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    del(){
      this.request.post("/List/DeleteFalse", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    changeListState(active){
      this.listState=active
    },
    comfrim(){//确定
      let ids = this.multipleSelection.map(v => v.id) //把一个对象数组变为传id的数组,类似于[{}, {}, {}] => [1,2,3]
      if(this.multipleSelection[0]!=null){
        this.request.post("/Plist/setstudentstate", {
          lid:this.lid,
          list:ids
        }).then(res => {
          if (res.code === '200') {
            this.$message.success("分配成功")
            this.addstudent(this.lid)
            this.multipleSelection=[]
          } else {
            this.$message.error("分配失败")
          }
        })
      }
      else {
        this.$message.warning("请选择需要分配的学生")
      }
    }
  }
}
</script>

<style scoped>
.form-toggle{
  margin: 5px 0;
  text-align: center
}
.form-toggle b{
  font-size: 15px;
  cursor: pointer;
}
.active{
  color: deepskyblue;
}
</style>