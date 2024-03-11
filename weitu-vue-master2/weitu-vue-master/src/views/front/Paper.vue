<template>
  <div style="margin-bottom: 100px">
    <div style="margin: 20px 0">
      <span style="font-size: 20px">{{exam.listname}}</span>
      <span style="font-size: 14px; margin-left: 20px">开始时间：{{exam.btime}}</span>
      <span style="font-size: 14px; margin-left: 20px">结束时间：{{exam.etime}}</span>
    </div>
    <div v-if="isBegin==1 && isEnd==0">
      <el-card>
        <div v-for="(item,index) in question" :key="question.id">
          <div>
            <span v-if="item.type==1">（单选题）</span>
            <span v-if="item.type==2">（多选题）</span>
            <span v-if="item.type==3">（简答题）</span>
            <span>{{index+1}}.</span>{{item.question}}
          </div>
          <div v-if="item.type==1">
            <div v-for="answerq in item.answer" :key="item.answer.id">
              <span><el-radio v-model="question[index].studentAnswer" :label=answerq>{{ answerq }}</el-radio></span>
            </div>
          </div>

          <div v-if="item.type==2">
            <el-checkbox-group v-model="question[index].studentAnswer">
              <div v-for="answerq in item.answer" :key="item.answer.id">
                <el-checkbox  :label="answerq">{{ answerq }}</el-checkbox>
              </div>
            </el-checkbox-group>
          </div>

          <div v-if="item.type==3" style="margin: 10px">
            <el-input type="textarea" v-model="question[index].studentAnswer"></el-input>
          </div>
        </div>
      </el-card>
      <div style="margin: 20px; text-align: center">
        <el-button size="medium" type="primary" @click="submitPaper">提交</el-button>
        <el-button size="medium" type="primary" @click="$router.push('/front/studentList')">取消</el-button>
      </div>
    </div>

    <div v-if="isBegin==0">
      <el-card>问卷尚未开始！</el-card>
    </div>
    <div v-if="isEnd==1">
      <el-card>问卷已经结束！</el-card>
    </div>
  </div>
</template>


<script>
export default {
  name:"Paper",
  data(){
    return {
      tableData: [],
      state:"",
      examId:this.$route.query.examId,
      user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):{},
      exam:{},
      question:[],
      isBegin:0,
      isEnd:0,
    }
  },
  created(){
    this.request.get("/List/"+this.examId).then(res=>{
      this.exam=res.data
      var dateNow = new Date()//获取当前时间
      var dateExamBegin = new Date(this.exam.btime)//考试开始时间格式转化
      var dateExamEnd = new Date(this.exam.etime)//考试结束时间格式转化
      if(dateNow > dateExamEnd){
        this.isEnd=1
      }else{
        this.isEnd=0
      }
      if(dateNow > dateExamBegin){
        this.isBegin=1
      }else{
        this.isBegin=0
      }

      this.question = JSON.parse(res.data.question);
      for(let i=0;i<this.question.length;i++){
        this.$set(this.question[i],"studentAnswer",[])
      }
    })
  },

  methods:{
    submitPaper(){
      this.state="已参与"
      this.request.post("/userList/update",{
        uid:this.user.id,
        lid:this.examId,
        answer:JSON.stringify(this.question),
        state:this.state
      }).then(res=>{
        if(res.code==='200'){
          this.$message.success("提交成功")
          this.$router.push("/front/studentList")
        }
      })
    }
  }
}
</script>