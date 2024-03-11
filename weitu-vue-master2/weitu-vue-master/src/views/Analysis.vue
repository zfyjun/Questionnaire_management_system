<template>
  <div>
    <el-container>
      <el-header>
        <B style="margin-right: 100px"><i class="el-icon-s-claim" style="margin-right: 10px;font-size: 20px;color: deepskyblue"></i>{{'问卷名：'+this.listform.listname}}</B>
        <span>{{'开始时间：'+this.listform.btime}}</span>
        <span style="margin-left: 25px">{{'结束时间：'+this.listform.etime}}</span>
        <el-button size="mini" type="text" @click="returnlist()" style="margin-left: 20px">返回</el-button>
        <el-divider></el-divider>
      </el-header>
      <el-container>
        <el-main>
          <div v-loading="loading" style="padding: 10px">
            <el-card shadow="never" v-if="TableData[0]!=null">
              <el-collapse v-model="type[0]">
                <el-collapse-item name="1">
                  <div slot="title">
                    <B class="el-icon-s-help" style="font-size: 18px;color:deepskyblue;"></B>
                    <B style="color: black;font-size: 15px;margin-left: 5px" >选择类型问题（选项数据分析）：</B>
                  </div>
                  <el-card  v-for="(item,index) in this.TableData " style="margin-bottom: 20px;width: 670px" :id="'id'+index">
                    <el-collapse v-model="activeNames[index]" >
                      <el-collapse-item name="1">
                        <div slot="title">
                          <B style="font-size: 15px">问题：</B>
                          <span >{{item.question}}</span>
                        </div>
                        <div >
                          <el-card :id="'main'+index" style="width:300px;height: 200px;margin-top: 20px;display: inline-block" >
                          </el-card>
                          <el-card style="width:300px;min-height: 200px;margin-top: 20px;display: inline-block;margin-left: 20px" :id="'card'+index">
                          </el-card>
                        </div>
                      </el-collapse-item>
                    </el-collapse>
                  </el-card>
                </el-collapse-item>
              </el-collapse>
            </el-card>
            <el-card shadow="never" style="margin-top: 20px" v-if="simpleData[0]!=null">
              <el-collapse v-model="type[1]">
                <el-collapse-item name="1">
                  <div slot="title">
                    <B class="el-icon-s-marketing" style="font-size: 18px;color:deepskyblue;"></B>
                    <B style="color: black;font-size: 15px;margin-left: 5px" >简答类型问题(关键 词/字 提取分析)：</B>
                  </div>
                  <el-card  v-for="(item,index) in this.simpleData " style="margin-bottom: 20px;width: 670px" :id="'id2'+index">
                    <el-collapse v-model="activeNames2[index]" >
                      <el-collapse-item name="1">
                        <div slot="title">
                          <B style="font-size: 15px">问题：</B>
                          <span >{{item.question}}</span>
                        </div>
                        <div >
                          <el-card style="height: 300px" :id="'simple'+index">
                          </el-card>
                        </div>
                        <div>
                          <el-tooltip content="点击查看回答详情" placement="bottom" effect="light">
                            <el-button type="text" size="mini" style="margin-top: 10px;float: right;" @click="openlist(index,item.question)">
                              <i style="font-size: 13px;margin-right: 3px" class="el-icon-s-order" >
                              </i>查看详细数据
                            </el-button>
                          </el-tooltip>
                        </div>
                      </el-collapse-item>
                    </el-collapse>
                  </el-card>
                </el-collapse-item>
              </el-collapse>
            </el-card>
          </div>
        </el-main>
<!--    分割    -->
        <el-aside style="width: 25%">
          <el-card style="max-height: 500px;height:auto ;position: fixed;width: 25%" >
            <el-card shadow="never" v-if="TableData[0]!=null">
              <div slot="header" style="height: 10px">
                <B style="font-size: 13px;color: black"><i class="el-icon-caret-bottom" style="font-size: 15px"></i>选择类</B>
              </div>
              <div v-for="(item,index) in TableData" style="margin-bottom: 0px">
                <el-tooltip content="移至问题详情" placement="bottom" effect="light">
                  <el-button type="text" @click="scrollto('id',index)">
                    <B style="color: #030303">{{'问题'+(index+1)+'：'}}</B><span style="word-break: normal;white-space: pre-wrap;word-wrap: break-word">{{item.question}}</span>
                    <B class="el-icon-view"></B>
                  </el-button>
                </el-tooltip>
              </div>
            </el-card>
            <el-card shadow="never" style="margin-top: 10px" v-if="simpleData[0]!=null">
              <div slot="header" style="height: 10px">
                <B style="font-size: 13px;color: black"><i class="el-icon-caret-bottom" style="font-size: 15px"></i>简答类</B>
              </div>
              <div v-for="(item,index) in simpleData" style="margin-bottom: 0px">
                <el-tooltip content="移至问题详情" placement="bottom" effect="light">
                  <el-button type="text" @click="scrollto('id2',index)">
                    <B style="color: #030303">{{'问题'+(index+1)+'：'}}</B><span style="word-break: normal;white-space: pre-wrap;word-wrap: break-word">{{item.question}}</span>
                    <B class="el-icon-view"></B>
                  </el-button>
                </el-tooltip>
              </div>
            </el-card>
            <div>
              <el-tooltip content="返回顶部" placement="bottom" effect="light">
                <el-button type="text">
                  <B class="el-icon-top" style="font-size: 20px" @click="toTOP"></B>
                </el-button>
              </el-tooltip>
            </div>
          </el-card>
        </el-aside>
      </el-container>
    </el-container>

    <el-dialog :visible.sync="dialogTableVisible">
      <B slot="title">{{questionname}}</B>
      <div style="width: 90%;margin: auto">
        <el-input size="mini" placeholder="输入关键词查询" style="width: 50%" v-model="search"></el-input>
        <el-button size="mini" style="margin-left: 5px" @click="searchlist" >查询</el-button>
      </div>
      <el-table :data="answerdata" size="mini"  border stripe style="width: 90%;margin: auto;margin-top: 10px;max-height: 500px" v-loading="loading2">
        <el-table-column property="answer" label="完整回答：" width="auto"></el-table-column>
      </el-table>


    </el-dialog>


  </div>
</template>

<script>
import * as echarts from 'echarts';
import request from "@/utils/request";

export default {
  name: "Analysis",
  created() {
    this.load()
    this.sconde();
  },
  data(){
    return{
      dialogTableVisible:false,
      simpleData:[],//简答题
      TableData:[],//选择题
      type:['1','1'],
      length:0,
      id:localStorage.getItem("ListId")?localStorage.getItem("ListId"):0,
      activeNames:[],//控制选择
      activeNames2:[],//控制简答
      answerlist:[],
      name:[],
      value:[],
      listform:{},
      answerdata:[],
      loading:false,
      loading2:false,
      questionname:null,
      index:null,
      search:'',
      second:60

    }
  },
  methods:{
    sconde(){//每60秒请求一次数据
      let interval=setInterval(()=>{//这个方法要人为关掉，否则会一直工作
        if(this.second>=1){
          this.second--
        }
        else {
          this.load()
          this.second=60
        }
      },1000)
    },
    load(){
      this.getlist()
      this.getanalysis()
      this.getsimplelist()
    },
    searchlist(){
      this.loading2=true
      request.get("/Analysis/getsimple",{
        params: {
          lid: this.id,
          index:this.index,
          question:this.questionname,
          search:this.search
        }
      }).then(res => {
        if(res.code==='200'){
          this.answerdata=res.data
          this.loading2=false
          this.search='';
        }
        else {
          this.$message({
            type:"error",
            message:res.msg
          })
        }
      })
    },
    openlist(index,question){
      this.questionname=question
      this.index=index
      this.loading=true
      request.get("/Analysis/getsimple",{
        params: {
          lid: this.id,
          index:index,
          question:question,
          search:this.search
        }
      }).then(res => {
        if(res.code==='200'){
          this.answerdata=res.data
          this.loading=false
          this.dialogTableVisible=true
        }
        else {
          this.$message({
            type:"error",
            message:res.msg
          })
        }
      })
    },
    toTOP(){
      document.documentElement.scrollIntoView({behavior:"smooth",block:"start"})
    },
    scrollto(name,index){
      document.getElementById(name+index).scrollIntoView({behavior:"smooth",block:"start"})
    },
    getlist(){
      request.get("/Analysis/getlistbyid",{
        params: {
          lid: this.id,
        }
      }).then(res => {
        if(res.code==='200'){
          this.listform=res.data
        }
        else {
          this.$message({
            type:"error",
            message:res.msg
          })
        }
      })
    },
    getsimplelist(){
      request.get("/Analysis/getanalysis",{
        params: {
          lid: this.id,
          flag:2
        }
      }).then(res => {
        if(res.code==='200'){
          this.simpleData=res.data
          console.log(this.simpleData)
          for(let i=0;i<res.data.length;i++){
            this.activeNames2.push('1');
          }
          setTimeout(()=>{
            this.start3(); //初始化品质异常单饼图
          },100)
        }
      })
    },
    getanalysis(){
      request.get("/Analysis/getanalysis",{
        params: {
          lid: this.id,
          flag:1
        }
      }).then(res => {
        if(res.code==='200'){
          this.TableData=res.data
          setTimeout(()=>{
            this.start1(); //初始化品质异常单饼图
          },100)
        }
      })
    },
    returnlist(){
      localStorage.removeItem("ListId")
      this.$router.push("/listManagement")
    },
    start1() {//页面元素创建好再触发
      var myChart =[]
      var jsons={
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '20%',
          left: 'center'
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: ['20%', '30%'],
            center: ['50%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 1,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 10,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: []
          },
        ]
      }
      var option = []
      /////////////////////////////////////////
      this.loading=true
      for(let i=0;i<this.TableData.length;i++){
        this.activeNames.push('1')
        option.push(jsons)
        option[i].series[0].data=this.TableData[i].answerlist
        myChart.push(echarts.init(document.getElementById('main'+i)))
        myChart[i].setOption(option[i]);
      }
      this.loading=false
      setTimeout(()=>{
        this.start2(); //初始化品质异常单饼图
      },100)
    },
    start2(){
      var myChart2 = [];
      var option2=[]
      var json={
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [],
            type: 'bar',
            showBackground: true,
            backgroundStyle: {
              color: 'rgba(180, 180, 180, 0.2)'
            }
          }
        ]
      };
      console.log("显示")
      for(let i=0;i<this.TableData.length;i++){
        option2.push(json)
        this.answerlist=this.TableData[i].answerlist
        for(let j=0;j<this.answerlist.length;j++){
          this.name.push(this.answerlist[j].name)
          this.value.push(this.answerlist[j].value)
        }
        option2[i].xAxis.data=this.name
        option2[i].series[0].data=this.value
        myChart2.push(echarts.init(document.getElementById('card'+i)))
        myChart2[i].setOption(option2[i]);
        this.name=[]
        this.value=[]
      }
    },
    start3(){
      var myChart3 = [];
      var json3={
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: 'bottom'
        },
        toolbox: {
          show: true,
          feature: {
            mark: { show: true },
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true }
          }
        },
        series: [
          {
            name: 'Nightingale Chart',
            type: 'pie',
            radius: [20, 100],
            center: ['50%', '50%'],
            roseType: 'area',
            itemStyle: {
              borderRadius: 3
            },
            data: []
          }
        ]
      }
      var option3 = [];
      for(let i=0;i<this.simpleData.length;i++){
        option3.push(json3)
        option3[i].series[0].data=this.simpleData[i].answerlist
        myChart3.push(echarts.init(document.getElementById('simple'+i)))
        myChart3[i].setOption(option3[i]);
      }

    },
  },
  mounted() {
    this.start1()
  }
}
</script>

<style scoped>

</style>