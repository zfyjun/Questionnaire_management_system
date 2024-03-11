<template>
<div class="wrapper">
<!--  margin: auto;-->
  <div style="width: 50%;margin-top: 30px;display: flex" class="login-box" v-if="registerflag==true">
    <div style="flex: 1;padding: 50px">
      <!--账号注册-->
      <el-form ref="form" :model="registerForm" :rules="rules" >
        <div style="display: flex ;margin-top: 10px">
          <el-form-item prop="account" style="flex: 2">
            <el-input  size="mini" v-model="registerForm.account" style="width: 197px;" placeholder="输入账户名">
            </el-input>
            <el-button size="mini" style="margin-left: 10px" type="primary" plain @click="confirmname(1)"><a>账户名验证</a></el-button>
          </el-form-item>
        </div>
        <el-form-item prop="password" style="margin-top:-10px">
          <el-input size="mini" show-password v-model="registerForm.password" style="width: 300px;" placeholder="输入密码" >
          </el-input>
        </el-form-item>

        <el-form-item prop="confirm" style="margin-top:-10px">
          <el-input size="mini"  show-password v-model="registerForm.confirm" style="width: 300px;" placeholder="确认密码">
          </el-input>
        </el-form-item>
        <el-form-item prop="age" style="margin-top:-10px">
          <el-input size="mini"  style="width: 300px" v-model="registerForm.email" placeholder="输入邮箱（可填）">
          </el-input>
        </el-form-item>
        <el-form-item prop="address" style="margin-top:-10px" >
          <el-input size="mini" v-model="registerForm.phone" style="width: 300px;" placeholder="输入电话号码（可填）">
          </el-input>
        </el-form-item>
        <div>
          <el-button type="primary" size="medium" style="width: 100%;background-color: #fcbe82;width: 300px;border:none;" @click="register">确认注册</el-button>
        </div>
      </el-form>
    </div>
  </div>
  <div v-if="registerflag==false">
    <el-image style="width: auto; height: auto;margin-left: 100px;margin-top: 100px" :src="require('../images/handshake.png')" ></el-image>
    <div><B style="color:#515151;font-size: 30px;margin-left: 110px">感谢您的注册</B></div>
  </div>
</div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Register",
  data() {
    return {
      registerflag:'',
      registerForm:{},
      rules:{
        account:[
          {required:true,message:'请输入账户名',trigger:'blur'},
        ],
        password:[
          {required:true,message:'请输入密码',trigger:'blur'},
        ],
        confirm:[
          {required:true,message:'请确认密码',trigger:'blur'},
        ],
        phone:[
          {required:true,message:'请输入手机号',trigger:'blur'},
        ],
        sex:[
          {required:true,message:'请选择性别',trigger:'blur'},
        ],
      }
    }
  },
  created() {
    this.load()
  },
  methods:{
    load(){
      if(JSON.parse(sessionStorage.getItem('stuId'))!=null){
        this.registerflag=true
      }
      else {
        this.registerflag=false
      }
    },
    checkpassword(){//密码一致性检验
      if(this.registerForm.password==this.registerForm.confirm){
        this.codes1=1
      }
      else {
        this.$message({
          type: "error",
          message: '两次密码不一致'
        })
        this.codes1=0
      }
      return this.codes1
    },
    Codes() {//表单验证
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.codes=1
        } else {
          this.codes=0
        }
      })
      return this.codes
    },
    confirmname(cflag){//验证用户名
      if(this.registerForm.account==null||this.registerForm.account==''){
        this.$refs['form'].validate('rules')
        return 0
      }
      else {
        request.post("/List/confirm",this.registerForm.account).then(res=>{
          if(res.code==='200'&&cflag==1){
            this.$message({
              type:"success",
              message:"该账户名可用"
            })
          }
          else if(res.code !='200'){
            this.$message({
              type:"error",
              message:res.msg
            })
          }
        })
        return 1
      }
    },
    register(){
      if (this.Codes() == 1) {//注册信息正确无误
        if(this.confirmname(0)==1){
          if(this.checkpassword()==1){
            request.post("/List/register", {
              form:this.registerForm,
              stuId:JSON.parse(sessionStorage.getItem('stuId'))
            }).then(res => {
              if (res.code === '200') {
                this.$message({
                  type: "success",
                  message: "注册成功"
                })
                this.registerForm=null
                sessionStorage.clear()
                this.load()
              } else if (res.code != '200') {
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            })
          }
        }
      }
      else {//防止不输入
        this.$refs['form'].validate('rules')
      }
   }
  }
}
</script>

<style scoped>
body {
  margin: 0;
}
.wrapper{
  background: url("../images/login3.jpg") ;
  width:100%;
  height:100%;
  position:absolute;
  background-size:100% 100%;
}

.login-bg img {
  width: 100%;
  height: 100%;
}

.login-box{
  width: 500px;
  height: 300px;
  margin-top: 1px;
  border-radius: 10px;
  position: absolute;
  z-index: 1;
}

</style>