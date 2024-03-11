<template>
<!--发布调查问卷表单-->
  <div style="padding:10px">
    <el-container>
      <el-header>
        <el-form :rules="rules" >
          <el-form-item style="margin-left: 15px" size="mini">
            <span><B slot="label"><i class="el-icon-s-order" style="font-size: 20px;color: deepskyblue"></i>调查问卷名称：</B></span>
            <a ><el-input v-model="form.listname" autocomplete="off" style="width:15%;" size="mini" placeholder="请输入调查问卷的名称" clearable></el-input></a>
            <span>
              <span style="margin-left: 50px"><B>开始时间：</B>
                <span v-if="form.btime!=null">{{form.btime}}</span>
                <span v-if="form.btime==null" style="color: orangered">未设定</span>
              </span>
              <span style="margin-left: 30px"><B>结束时间：</B>
                <span v-if="form.etime!=null">{{form.etime}}</span>
                <span v-if="form.etime==null" style="color: orangered">未设定</span>
              </span>
            </span>
            <el-tooltip content="点击编辑时间" placement="bottom" effect="light">
              <el-button type="text" style="margin-left: 30px" @click="timesflag=true" size="mini" >
                <i class="el-icon-edit" style="font-size: 18px"></i>
              </el-button>
            </el-tooltip>
            <el-tooltip content="点击完成编辑" placement="bottom" effect="light">
              <el-button type="primary" plain @click="builds" size="mini" style="margin-left: 200px">提 交</el-button>
            </el-tooltip>
            <el-button type="text" href="/listPublish" style="color: deepskyblue;margin-left: 10px"@click="removeListId">返回</el-button>
          </el-form-item>
        </el-form>
        <el-divider ></el-divider>
      </el-header>
      <el-main>
        <div v-for="(item, index) in list" style="display: flex" >
          <el-card class="box-card"  style="margin-top: 15px" >
            <el-collapse v-model="abletovisit[index]">
              <el-collapse-item name="1">
                <B slot="title" style="font-size: 15px" :id="'card'+index"><a>{{'问题编号：'+(index+1)}} </a></B>
                <el-divider></el-divider>
                <el-form >
                  <el-form-item style="margin-left: 15px" size="mini" label="调查问题：">
                    <a ><el-input v-model="list[index].question" autocomplete="off" style="width:90%;" type="textarea" size="mini" placeholder="请输入您需要调查的问题" ></el-input></a>
                  </el-form-item>
                  <el-form-item label="调查形式：" style="margin-left: 15px">
                    <el-select v-model="list[index].type" placeholder="请选择调查类型" size="mini" style="width: 25%">
                      <el-option label="简答" value="3"></el-option>
                      <el-option label="单选" value="1"></el-option>
                      <el-option label="多选" value="2"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-card style="margin-left: 15px;width: 80%" shadow="never" v-if="list[index].type!='3'">
                    <el-form-item style="margin-left: 15px" size="mini" v-for="(o,idx) in list[index].answer">
                      <span typeof="label">{{'回答选项编号：'+(idx+1)}}</span>
                      <a ><el-input v-model="list[index].answer[idx]" autocomplete="off" style="width:80%;" size="mini" placeholder="请输入该问题的回答" ></el-input></a>
                      <a style="margin: auto 0px;">
                        <el-tooltip content="点击在上方插入回答选项" placement="bottom" effect="light">
                          <el-button type="text" @click="addanswerup(index,idx)" style="color: deepskyblue;font-size: 15px;margin-left: 5px;margin-top: 5px"><i class="el-icon-upload2" ></i> </el-button>
                        </el-tooltip>
                        <el-tooltip content="点击移除该回答选项" placement="bottom" effect="light">
                          <el-button type="text" @click="removeselect(index,idx)" style="color: orangered;font-size: 15px;margin-left: 5px;margin-top: 5px"><i class="el-icon-delete" ></i> </el-button>
                        </el-tooltip>
                      </a>
                    </el-form-item>
                    <div>
                      <el-tooltip content="点击添加回答选项" placement="bottom" effect="light">
                        <el-button  style="margin-left: 0px;margin-bottom: -20px;margin-top: 5px;font-size: 15px" type="text" @click="addselect(index)">
                          <i class="el-icon-plus" style="font-size: 15px"></i>
                        </el-button>
                      </el-tooltip>
                    </div>
                  </el-card>
                </el-form >
              </el-collapse-item>
            </el-collapse>
          </el-card>
          <a style="margin: auto 0px;">
            <el-tooltip content="点击在上方新添问题" placement="bottom" effect="light">
              <el-button type="text" @click="addquestionup(index)" style="color: deepskyblue;font-size: 30px;margin-left: 15px;margin-top: 10px"><i class="el-icon-circle-plus" ></i> </el-button>
            </el-tooltip>
            <el-tooltip content="点击移除该问题" placement="bottom" effect="light">
              <el-button type="text" @click="removequestion(index)" style="color: orangered;font-size: 30px;margin-left: 5px;margin-top: 10px"><i class="el-icon-remove" ></i> </el-button>
            </el-tooltip>
          </a>
        </div>
        <div>
          <el-tooltip content="点击添加调查问题" placement="bottom" effect="light">
            <el-button  style="margin-left: 0px;margin-bottom: 10px;margin-top: 10px;font-size: 10px" @click="addquestion">
              <i class="el-icon-plus" style="font-size: 10px"></i>
            </el-button>
          </el-tooltip>
        </div>
      </el-main>
    </el-container>
    <!--   修改时间相关的对话框 -->
    <el-dialog :visible.sync="timesflag" style="margin: auto ;width: 95%">
      <B slot="title" class="el-icon-date"  style="font-size: 20px;color:deepskyblue ">
        <span style="color: black;margin-left: 5px">问卷时间编辑</span>
      </B>
      <span class="demonstration"></span>
      <span>开始时间：</span>
      <el-date-picker
          size="mini"
          v-model="form.btime"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="选择日期时间"
          default-time="12:00:00">
      </el-date-picker>
      <span style="margin-left: 20px">结束时间: </span>
      <span class="demonstration"></span>
      <el-date-picker
          size="mini"
          v-model="form.etime"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="选择日期时间"
          default-time="23:59:59">
      </el-date-picker>
      <el-button size="mini" @click="timesflag=false" style="margin-left: 15px" type="primary" plain>确 定</el-button>
    </el-dialog>
    <div id="topAnchor" ref="faultTree" class="wrap" @click="toTOP">
      <el-tooltip content="点击返回顶部" placement="bottom" effect="light">
        <el-button id="TOPUP"  herf="#topAnchor">
          <B class="el-icon-top" style="margin-left: -12px;margin-top:-6px;font-size: 30px"></B>
        </el-button>
      </el-tooltip>
    </div>
    <el-card  style="position: fixed;right: 10px;top:150px;width: 30%;height: auto;max-height: 500px" v-if="list[0]!=null">
      <el-container>
        <el-main style="height: auto;max-height: 450px">
          <div v-for="(item,index) in list" style="display: flex">
            <el-card style="margin-top: 10px;width: 90%" shadow="never">
              <B style="font-size: 13px">{{'问题编号：'+(index+1)}}<i class="el-icon-caret-right"></i></B>
              <a style="font-size: 10px;margin-left: 10px;word-break: normal;white-space: pre-wrap;word-wrap: break-word">{{item.question}}</a>
              <div>
                <B style="font-size: 13px">类型：</B>
                <a style="font-size: 10px;margin-left: 10px" v-if="item.type=='1'">单选</a>
                <a style="font-size: 10px;margin-left: 10px" v-if="item.type=='2'">多选</a>
                <a style="font-size: 10px;margin-left: 10px" v-if="item.type=='3'">简答</a>
              </div>
            </el-card>
            <el-button type="text" size="mini" style="margin-left: 5px" @click="scrollto(index)">详情</el-button>
          </div>
        </el-main>
      </el-container>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "ListEdit",
  data(){
    return{
      id:localStorage.getItem('id')?localStorage.getItem('id'):'0',//默认为0
      timesflag:false,
      list:[],//问题
      abletovisit:[],//控制最小化或者最大化
      form:{"listname":null,"btime":null,"etime":null},//保存名字时间等属性
      rules:{
        name:[
          {required:true,message:'请输入问卷名称',trigger:'blur'},
        ],
      }
    }
  },
  created() {
    console.log(this.id)
    if(this.id!=0&&this.id!=null){//编辑已有的
      this.getlist()
    }
  },
  methods:{
    removeListId(){
      if(this.id!=0&&this.id!=null){
        localStorage.removeItem('id')
        this.$router.push("/listPublish")
      }
      else {
        localStorage.removeItem('id')
        this.$router.push("/home")
      }
    },
    getlist(){
      request.get("/Plist/getlist", {
        params: {
          id: this.id
        }
      }).then(res => {
        if(res.code==='200'){
          this.$message({
            type:"success",
            message:"已成功加载问卷信息"
          })
          if(res.data.question!=null){
            this.list=JSON.parse(res.data.question)
          }
          this.form.listname=res.data.listname
          this.form.btime=res.data.btime
          this.form.etime=res.data.etime
        }
        else {
          this.$message({
            type:"error",
            message:res.msg
          })
        }
      })
    },
    scrollto(index){
      document.getElementById('card'+index).scrollIntoView({behavior:"smooth",block:"start"})
    },
    toTOP(){
      document.documentElement.scrollIntoView({behavior:"smooth",block:"start"})
    },
    removequestion(index){//移除某个问题
      this.list.splice(index,1)
      this.abletovisit.splice(index,1)
    },
    removeselect(index,idx){//移除某个问题的某个选项回答
      this.list[index].answer.splice(idx,1);
    },
    addquestion(){//添加问题
      let question={"question":null,"type":'3',"answer":[]};
      this.list.push(question);
      this.abletovisit.push('1')
    },
    addquestionup(index){//在上方添加问题
      this.abletovisit.splice(index,0,'1')
      let question={"question":null,"type":'3',"answer":[]}
      this.list.splice(index,0,question);
    },
    addselect(index){//添加某个问题的回答选项
      if(this.list[index].answer.length<6){
        this.list[index].answer.push(null)
      }
      else {
        this.$message({
          type:"error",
          message:"错误！一个问题最多添加6个回答"
        })
      }
    },
    addanswerup(index,idx){//添加某个问题的回答选项
      if(this.list[index].answer.length<6){
        this.list[index].answer.splice(idx,0,null)
      }
      else {
        this.$message({
          type:"error",
          message:"错误！一个问题最多添加6个回答"
        })
      }
    },
    builds(){//提交问题（前端处理）
      let flag=1
      if(this.form.listname==null||this.form.listname==''){
        this.$message({
          type:"error",
          message:"错误！ ”调查问卷名称“ 不能为空"
        })
        flag=0
      }
      else if(this.form.btime==null||this.form.etime==null){
        this.$message({
          type:"error",
          message:"错误！您尚未设置调查问卷 ”开始时间“ 或 ”结束时间“ "
        })
        flag=0
      }
      else {
        if(this.list.length==0){
          this.$message({
            type:"error",
            message:"错误！调查问卷内容为空"
          })
          flag=0
        }
        else {
          for(let i=0;i<this.list.length;i++){
            if(this.list[i].question==null||this.list[i].question==''){
              this.scrollto(i)
              this.$message({
                type:"error",
                message:"错误！调查问题: "+(i+1)+" 内容为空"
              })
              flag=0
              break
            }
            else if(this.list[i].type!='3'){
              if((this.list[i].answer.length<2)){
                this.scrollto(i)
                this.$message({
                  type:"error",
                  message:"错误！调查问题: "+(i+1)+" 回答选项不足2条"
                })
                flag=0
                break
              }
              else {
                for(let j=0;j<this.list[i].answer.length;j++){
                  if(this.list[i].answer[j]==null||this.list[i].answer[j]==''){
                    this.scrollto(i)
                    this.$message({
                      type:"error",
                      message:"错误！调查问题: "+(i+1)+" 回答选项: "+(j+1)+" 内容为空"
                    })
                    flag=0
                    break
                  }
                }
              }
            }
          }
        }
      }
      if(flag!=0){///成功验证
        this.question=[];
        for(let i=0;i<this.list.length;i++){
          if(this.list[i].type=='3'){//为简答的时候
            this.list[i].answer=[];
          }
          this.question[i]=this.list[i];
          if(i==this.list.length-1){
            this.send()
          }
        }
      }
    },
    send(){//提交问题，链接后端
      request.post("/Plist/newlist", {
        question:this.question,
        form:this.form,
        id:this.id
      }).then(res=>{
        if(res.code==='200'){
          this.$message({
            type:"success",
            message:"提交成功"
          })
          if(this.id==0){
            this.form.listname=null
            this.form.btime=null
            this.form.etime=null
          }
        }
        else if(res.code !='200'){
          this.$message({
            type:"error",
            message:res.msg
          })
        }
      })
    }
  }
}
</script>

<style scoped>
#TOPUP{
  position: fixed;
  right: 45px;
  bottom: 20px;
  width: 40px;
  height: 40px;
  z-index: 99999999;
  box-shadow: 0px 0px 4px 4px #ecefef;
  border-radius: 600px;
}
.box-card {
  width: 580px;
}
</style>