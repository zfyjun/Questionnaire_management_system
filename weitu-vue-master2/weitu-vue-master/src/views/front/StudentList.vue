<template>
  <div>
    <div>
      <el-divider content-position="left" style="margin-bottom: 40px">
        <div class="form-toggle">
          <b :class="{'active':listState==='未参与'}" @click="changeListState('未参与')">未参与</b>
          <b :class="{'active':listState==='已参与'}" style="margin-left: 30px" @click="changeListState('已参与')">已参与</b>
        </div>
      </el-divider>
    </div>
      <div v-for="item in tableData" :key="item.id" style="display: inline-block;margin-top: 20px">
        <el-card v-if="item.state===listState" style="margin-top: 20px;width: 350px;margin-left: 50px">
<!--          &&list.find(v=> v.id == item.lid).isDelete==0-->
          <div style="margin: 10px 0; font-size: 20px">
            <span style="font-size: 14px">{{list.find(v=> v.id == item.lid).listname}}</span>
            <template v-if="listState=='未参与'">
                <el-button slot="reference" style="float: right;margin-left: 5px;color: orange" type="text" @click="write(item.lid)" >填写</el-button>
            </template>
          </div>
        </el-card>
      </div>
  </div>
</template>

<script>
export default {
  name: "StudentList",
  data(){
    return {
      tableData:[],
      form:{},
      listState:'未参与',
      user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):{},
      list:{},
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/List/StudentList/" + this.user.id).then(res => {
        this.tableData = res.data
      })

      this.request.get("/List").then(res => {
        this.list = res.data
      })
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
    write(id){

      this.$router.push('/front/paper?examId='+id);
    },
    changeListState(active){
      this.listState=active
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