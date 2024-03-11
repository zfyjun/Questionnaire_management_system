<template>
  <div class="wrapper">
    <vue-particles
        class="login-bg"
        color="#FFFFFF"
        :particleOpacity="0.7"
        :particlesNumber="100"
        shapeType="circle"
        :particleSize="4"
        linesColor="#FFFFFF"
        :linesWidth="1"
        :lineLinked="true"
        :lineOpacity="0.4"
        :linesDistance="150"
        :moveSpeed="3"
        :hoverEffect="true"
        hoverMode="grab"
        :clickEffect="true"
        clickMode="push"
    />
    <div class="login-box">

      <el-tabs :stretch="true" class="el-tabs__active-bar.is-top" v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="账号密码登录" name="first" >
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="account">
          <el-input size="medium" placeholder="请输入用户名" style="color:rgba(255,255,255,0);margin: 10px 0" prefix-icon="el-icon-user" v-model="user.account"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" placeholder="请输入密码" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item>
          <div style="display: flex" >
            <el-input prefix-icon="el-icon-key" placeholder="请输入验证码" v-model="user.validCode" style="width: 65%"/>
            <ValidCode @input="createValidCode"/>
          </div>
        </el-form-item>
        <el-form-item style="margin: 10px 0;text-align: center">
          <el-button  type="primary" size="small" autocomplete="off" @click="login" class="log-button">
            <span></span>
            <span></span>
            <span></span>
            <span></span>
            登录</el-button>
          <el-button  type="primary" size="small" autocomplete="off" @click="$router.push('/registerWeb')" class="register-button">
            <span></span>
            <span></span>
            <span></span>
            <span></span>
            注册</el-button>
        </el-form-item>
      </el-form>
        </el-tab-pane>

        <el-tab-pane label="手机验证码登录" name="second">
          <el-form :model="user" :rules="rules" ref="Form">
            <el-form-item prop="phone">
              <el-input size="medium" placeholder="请输入手机号"
                        style="color:rgba(255,255,255,0);
                        margin: 10px 0"
                        prefix-icon="el-icon-user"
                        v-model="user.phone"></el-input>
              <el-button :disabled="second>0" type="primary" @click="sendpcode">发送验证码<h8 v-if="second>0">{{'('+second+')'}}</h8></el-button>
            </el-form-item>
            <el-form-item prop="p_code">
              <el-input size="medium" placeholder="请输入短信验证码" style="color:rgba(255,255,255,0);margin: 10px 0" prefix-icon="el-icon-user" v-model="user.p_code"></el-input>
            </el-form-item>
            <el-form-item style="margin: 10px 0;text-align: center">
              <el-button  type="primary" size="small" autocomplete="off" @click="phonelogin" class="log-button">
                <span></span>
                <span></span>
                <span></span>
                <span></span>
                登录</el-button>
              <el-button  type="primary" size="small" autocomplete="off" @click="$router.push('/registerWeb')" class="register-button">
                <span></span>
                <span></span>
                <span></span>
                <span></span>
                注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import ValidCode from "@/components/ValidCode";

export default {
  name: "Login",
  components: {
    ValidCode,
  },
  data(){
    return{
      second:0,
      activeName: 'first',
      user:{},
      rules:{
        account:[
          {required:true,message:"请输入用户名",trigger:'blur'},
          {min:1,max:10,message: "长度在1到10个字符",trigger: 'blur'}
        ],
        password: [
          {required:true,message:"请输入密码",trigger:'blur'},
          {min:1,max:15,message: "长度在1到15个字符",trigger: 'blur'}
        ],
      },
      validCode: '',
    }
  },
  methods:{
    sendpcode(){
      if(this.user.phone!=null&&this.user.phone!=''){
        this.$refs['Form'].validate((valid)=>{
          if(valid){
            this.request.post("/Sms/sendsms",this.user).then(res=>{
              if(res.code==='200'){
                this.$message.success("验证码正在发送")
              }
            })
          }
        })
        this.second=60
        let interval=setInterval(()=>{//这个方法要人为关掉，否则会一直工作
          if(this.second>=1){
            this.second--
          }
          else {
            clearInterval(interval)
          }
        },1000)
      }
      else {
        this.$message.error("请输入电话号码")
      }
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    // 接收验证码组件提交的 4位验证码
    createValidCode(data) {
      this.validCode = data
    },
    phonelogin(){
      if(this.user.p_code!=null&&this.user.p_code!=''){
        this.request.post("/user/loginbyphone",{
          phone:this.user.phone,
          code:this.user.p_code
        }).then(res=>{
          console.log(res.code)
          if(res.code=='200'){
            if(res.data.type=='管理员'){
              this.$router.push("/home")
            }else{
              this.$router.push('/front/home')
            }
            this.user=res.data;
            localStorage.setItem("user",JSON.stringify(this.user))
            this.$message.success("登录成功")
          }
          else{
            this.$message({
              type:"error",
              message:res.msg
            })
          }
        })
      }
      else {
        this.$message({
          type:"error",
          message:"请输入短信验证码"
        })
      }
    },
    login(){
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  //表单校验合法
          if (!this.user.validCode) {
            this.$message.error("请填写验证码")
            return
          }
          if(this.user.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
            this.$message.error("验证码错误")
            return
          }
          this.request.post("/user/login",this.user).then(res=>{
            console.log(res.code)
            if(res.code=='200'&&res.data!=null){
              if(res.data.type=='管理员'){
                this.$router.push("/home")
              }else{
                this.$router.push('/front/home')
              }
              this.user=res.data;
              localStorage.setItem("user",JSON.stringify(this.user))
              this.$message.success("登录成功")
            }
            else if(res.code=='200'&&res.data==null){
              this.$message({
                type:"error",
                message:"用户或账户名错误"
              })
            }
          })
        }
      })

    }
  }
}
</script>


<style scoped >
#particles-js {
  width: 100%;
  height: calc(100% - 100px);
  position: absolute;
}
.wrapper{
  background: url("../assets/bg1.jpg") ;
  width:100%;
  height:100%;
  position:absolute;
  background-size:100% 100%;
}
.login-box {
  margin: 150px auto;
  width: 400px;
  /*height: 300px;*/
  padding: 20px;
  background-color: rgb(0, 0, 0 ,.3);
  /*background-color: rgb(255, 255, 255 ,.2);*/
  box-sizing: border-box;
  box-shadow: 0 15px 25px rgba(0, 0, 0, .6);
  /*box-shadow: 0 15px 25px rgba(255, 255, 255, .1);*/
  border-radius: 10px;
}
.login-box h2 {
  margin: 0 0 30px;
  text-align: center;
  font-size: 24px;
  color: white;
}
/deep/ .el-input__inner  {
  width: 100%;
  /*padding: 10px 0;*/
  font-size: 16px;
  color: #ffffff;
  /*margin-bottom: 30px;*/
  border: none;
  border-bottom: 1px solid #fff;
  outline: none;
  background: transparent !important;
}
/deep/ .el-button--primary{
  position: relative;
  display: inline-block;
  padding: 10px 20px;
  color: #0a2a85;
  background-color: white;
  font-size: 16px;
  text-decoration: none;
  text-transform: uppercase;
  overflow: hidden;
  transition: .5s;
  margin-top: 40px;
  letter-spacing: 4px
}
/deep/ .el-button--primary:hover {
  background: #5ec8f5;
  color: #fff;
  border-radius: 5px;
  box-shadow: 0 0 5px #5ec8f5,
  0 0 25px #5ec8f5,
  0 0 50px #5ec8f5,
  0 0 100px #5ec8f5;
}

.login-box span {
  position: absolute;
  display: block;
}

.login-box span:nth-child(1) {
  top: 0;
  left: -100%;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg, transparent, #03e9f4);
  animation: btn-anim1 1s linear infinite;
}

@keyframes btn-anim1 {
  0% {
    left: -100%;
  }
  50%, 100% {
    left: 100%;
  }
}

.login-box span:nth-child(2) {
  top: -100%;
  right: 0;
  width: 2px;
  height: 100%;
  background: linear-gradient(180deg, transparent, #03e9f4);
  animation: btn-anim2 1s linear infinite;
  animation-delay: .25s
}

@keyframes btn-anim2 {
  0% {
    top: -100%;
  }
  50%, 100% {
    top: 100%;
  }
}

.login-box span:nth-child(3) {
  bottom: 0;
  right: -100%;
  width: 100%;
  height: 2px;
  background: linear-gradient(270deg, transparent, #03e9f4);
  animation: btn-anim3 1s linear infinite;
  animation-delay: .5s
}

@keyframes btn-anim3 {
  0% {
    right: -100%;
  }
  50%, 100% {
    right: 100%;
  }
}

.login-box span:nth-child(4) {
  bottom: -100%;
  left: 0;
  width: 2px;
  height: 100%;
  background: linear-gradient(360deg, transparent, #03e9f4);
  animation: btn-anim4 1s linear infinite;
  animation-delay: .75s
}

@keyframes btn-anim4 {
  0% {
    bottom: -100%;
  }
  50%, 100% {
    bottom: 100%;
  }
}

/deep/#tab-first.is-active,
/deep/#tab-second.is-active{
  color: rgb(0, 144, 255);
}
/deep/ #tab-first,
/deep/ #tab-second{
  color: white;
  font-weight: 550;
  font-size: 15px;
  opacity: 1;
  box-shadow: 0 0 0 0;
}
</style>