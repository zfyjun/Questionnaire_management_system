<template>
  <div >
    <el-container>
      <el-main width="75%" v-loading="loading">
        <div>
          <el-divider content-position="left" style="margin-bottom: 40px">
            <div class="form-toggle">
              <b :class="{'active':toggle==='work'}" @click="Toggle('work')">调查问卷</b>
              <b :class="{'active':toggle==='test'}" style="margin-left: 30px" @click="Toggle('test')">新生报到</b>
            </div>
          </el-divider>
        </div>

        <div style="width: 100%" v-if="toggle==='work'">
          <el-card style="min-width: 280px;width: auto;margin-left: 20px;margin-bottom: 20px;display: inline-block" v-for="(item,index) in list":id="'card'+index"  >
            <div slot="header" class="clearfix">
              <B style="font-size: 15px">问卷名：</B>
              <a>{{item.listname}}</a>
            </div>
            <div style="margin-top: -10px">
              <div><B style="font-size: 14px;color: deepskyblue">{{'已提交人数：'}}</B><B style="font-size: 14px;color: orangered">{{number[index]}}</B></div>
              <div>{{'开始时间：'+item.btime}}</div>
              <div>{{'结束时间：'+item.etime}}</div>
              <el-divider ></el-divider>
              <el-button type="text" size="mini" style="float:left;font-size: 10px;margin-top: -20px" @click="tableview(item.id,item.listname)">学生数据</el-button>
              <el-button style="float: right;font-size: 10px;margin-top: -20px" type="text" size="mini" @click="analysis(item.id)">总数据分析</el-button>
            </div>
          </el-card>
        </div>
        <div style="width: 100%" v-if="toggle==='test'">
          <el-card  style="width:auto;display: inline-block">
            <el-image style="width: auto; height: 300px;margin-left: 0px" :src="require('../images/45242.png')" ></el-image>
            <el-card style="width: auto">
              <div style="padding: 5px;">
                <span>重庆交通大学新生报到数据统计</span>
                <div class="bottom clearfix">
                  <el-button type="text" class="button" @click="helloword" >查看数据</el-button>
                </div>
              </div>
            </el-card>
          </el-card>
        </div>
      </el-main>
      <el-aside width="25%" >
        <el-card style="width: 99%;min-height: 600px;height: 99%" shadow="never" >
          <div v-if="toggle==='work'">
            <el-input v-model="search" placeholder="请输入问卷名称的相关信息进行查询" style="width:180px;margin-left: 0px" clearable size="mini"></el-input>
            <el-button @click="load" size="mini" style="margin-left: 5px">查询</el-button>
          </div>
          <el-table :data="list" border style="width: 100%;margin-top: 10px" size="mini"  v-loading="loading2" v-if="toggle==='work'">
            <el-table-column prop="listname" label="问卷名" style="width: auto" fixed></el-table-column>
            <el-table-column prop="btime" label="开始时间" width="150" sortable></el-table-column>
            <el-table-column prop="etime" label="结束时间" width="150" sortable></el-table-column>
            <el-table-column fixed="right" label="操作" width="50">
              <template slot-scope="scope">
                <el-tooltip content="点击移至问卷处" placement="bottom" effect="light">
                  <el-button @click="scrollto(scope.row.index)" type="text" size="small"><B class="el-icon-view" style="font-size: 15px"></B></el-button>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-aside>
    </el-container>



    <el-dialog :visible.sync="dialogTableVisible" style="max-heigh:80%;margin:auto" >
      <span slot="title"><B>当前问卷：</B>{{this.listmessage.name}}</span>
      <el-table :data="TabledData" size="mini"  stripe border style="width: 100%"  >
        <el-table-column property="sid" label="学号" style="width: auto" fixed></el-table-column>
        <el-table-column property="name" label="学生姓名" style="width: auto"></el-table-column>
        <el-table-column property="time" label="提交时间" format="yyyy-MM-dd HH:mm" value-format="yyyy-MM-dd HH:mm" style="width: auto" sortable></el-table-column>
        <el-table-column property="state" label="状态" width="80"  sortable></el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            width="60">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small" v-if="scope.row.state=='已参与'">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog :visible.sync="dialogFormVisible" style="max-heigh:90%;" top>
      <span slot="title"><B>当前学生：</B>{{this.listmessage.student}}</span>
      <el-card v-for="(item,index) in studentanswer" style="margin-top: 10px">
        <div slot="header">
          <B>{{'问题'+(index+1)+'：'}}</B><span>{{item.question}}</span>
        </div>
        <div>
          <span>类型：</span>
          <span v-if="item.type=='1'">单选</span>
          <span v-if="item.type=='2'">多选</span>
          <span v-if="item.type=='3'">简答</span>
        </div>
        <div v-if="item.type!=3" style="margin-top: 5px">
          <B style="margin-right: 5px;">选项: </B>
          <span v-for="(ite,idx) in item.answer" style="margin-right: 10px">{{ite+' ;'}}</span>
        </div>
        <div style="margin-top: 5px">
          <B style="margin-right: 5px;color: dodgerblue" ><B class="el-icon-edit" style="font-size: 16px"></B>学生回答: </B>
          <span style="font-size: 10px;margin-left: 10px;word-break: normal;white-space: pre-wrap;word-wrap: break-word">
            <span v-if="item.studentAnswer==null" style="color: orangered">未回答</span>
           <span v-for="(i,d) in item.studentAnswer">{{i}}</span>
          </span>
        </div>
      </el-card>
    </el-dialog>

  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "ListManagement",
  created() {
    this.load()
  },
  data(){
    return{
      search:'',
      list:[],
      number:[],
      dialogTableVisible:false,
      dialogFormVisible:false,
      TabledData:{},
      listmessage:{"id":null,"name":null,"student":null},
      loading:false,
      loading2:false,
      studentanswer:[],
      toggle:'work',
    }
  },
  methods:{
    load(){
      this.getlist()
    },
    scrollto(index){
      document.getElementById('card'+index).scrollIntoView({behavior:"smooth",block:"start"})
    },
    analysis(lid){
      localStorage.setItem("ListId",lid);
      this.$router.push("/analysis")
    },
    Toggle(active) {//切换作业与试卷
      this.toggle=active
    },
    tableview(id,name){//控制学生完成窗口
      this.listmessage.name=name
      this.listmessage.id=id
      this.loading=true
      request.get("/Plist/getdone",{
        params: {
          id: id
        }
      }).then(res => {
        if(res.code==='200'){
          this.TabledData=res.data
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
    handleClick(row){//查看选中学生的回答详情
      this.listmessage.student=row.name
      request.get("/Plist/getstudentanswer",{
        params: {
          lid:this.listmessage.id,
          uid:row.uid
        }
      }).then(res => {
        if(res.code==='200'){
          this.studentanswer=JSON.parse(res.data)
          this.dialogFormVisible=true
        }
        else {
          this.$message({
            type:"error",
            message:res.msg
          })
        }
      })
    },
    helloword(){
      const routes=this.$router.resolve({name:'HelloWorld'})
      window.open(routes.href,'_blank')
    },
    getlist(){
      this.loading=true
      this.loading2=true
      request.get("/Plist/getRelease", {
        params: {
          search: this.search
        }
      }).then(res => {
        if(res.code==='200'){
          this.list=res.data.list
          this.number=res.data.number
          if(this.list.length==0){
            this.$message({
              type:"warning",
              message:"未查询到相关信息"
            })
          }
          else {
            this.$message({
              type:"success",
              message:"已成功加载问卷列表"
            })
          }
          this.loading=false
          this.loading2=false
        }
        else {
          this.$message({
            type:"error",
            message:res.msg
          })
        }
      })
    },
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