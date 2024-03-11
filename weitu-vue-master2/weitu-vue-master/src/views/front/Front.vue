<template>
  <div>
<!--    <div>前台</div>-->
<!--    头部-->
    <div style="display: flex; height: 60px;padding-top: 10px; line-height: 60px; border-bottom: 1px solid #eee">
      <div style="width: 300px; display: flex;padding-left: 30px;">
        <div style="width: 60px">
          <img src="../../assets/logo.png" alt="" style="width: 50px; position: relative; top: 5px; right: 0">
        </div>
        <div style="flex: 1">欢迎来到新生问卷管理系统</div>
      </div>
      <div style="flex: 1;">
        <el-menu :default-active="'1'" class="el-menu-demo" mode="horizontal" router>
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/studentList">
            <el-badge :value="Mnumber">
              <span>问卷调查</span>
            </el-badge>
          </el-menu-item>
          <el-menu-item index="/front/List" v-if="student.name==null||student.name==''" >
              <span>新生报道</span>
          </el-menu-item>
        </el-menu>
      </div>


      <el-dropdown style="padding-left:50px; width: 150px; text-align: right">
        <div style="display: inline-block">
          <span>{{user.account}}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
        </div>
        <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">

          <el-dropdown-item style="font-size: 14px; padding: 5px 0">
            <router-link to="/front/information">个人信息</router-link>
          </el-dropdown-item>
          <el-dropdown-item style="font-size: 14px; padding: 5px 0">
            <span style="text-decoration: none" @click="logout">退出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

    </div>
    <div style="width: 1000px; margin: 0 auto;padding-top: 10px">
      <router-view/>
    </div>
  </div>
</template>

<script>
export default {
  name: "Front",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},

      Mnumber:0,
      list:[],
      ids:[],
      student: {},
      exam:{}
    }
  },
  created() {
    this.getstate()
    this.request.get("/userList").then(res => {
      this.list=res.data.filter(v => v.uid == this.user.id && v.state ==='未参与')
      for (let i=0; i<this.list.length; i++){
        this.ids[i]=this.list[i].lid
      }
    })

    this.setnum()
  },
  methods: {
    load(){

    },
    setnum(){
      this.request.get("/List").then(res=>{
        this.exam=res.data
        for (let j=0;j<this.ids.length;j++){
          for(let k=0;k<this.exam.length;k++){
            if(this.ids[j]==this.exam[k].id){
              let dateNow = new Date()//获取当前时间
              let dateExamBegin = new Date(this.exam[k].btime)//考试开始时间格式转化
              let dateExamEnd = new Date(this.exam[k].etime)//考试结束时间格式转化
              if(dateNow < dateExamEnd && dateExamBegin < dateNow){
                this.Mnumber++
              }
            }
          }
        }
      })
    },

    logout() {
        localStorage.removeItem("user")
        this.$router.push("/login")
        this.$message.success("退出成功")
    },
    getstate(){
      this.request.post("/student/studentinfo/"+this.user.id).then(res=>{
        this.student=res.data[0]
      })
    }
  }
}
</script>

<style>
.item{
  display: inline-block;
  width: 100px;

  text-align: center;
  cursor: pointer
}
.item a {
  color: 	#1E90FF;
}
.item:hover{
  background-color: 	LightPink;
}
</style>
