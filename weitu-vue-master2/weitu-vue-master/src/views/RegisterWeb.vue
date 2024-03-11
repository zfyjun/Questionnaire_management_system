<template>
  <div>
    <div class="login-bg">
      <img src="../images/login1.jpg" ref="img">
    </div>
    <div class="login-box">
      <div style="flex: 1;padding-left: 50px;padding-right: 50px;padding-top: 30px">
        <h2>注&nbsp;&nbsp;册</h2>
        <!--账号注册-->
        <el-form ref="form" :model="registerForm" :rules="rules">
          <!--          <div style="display: flex ;margin-top: 12px">-->
          <el-form-item prop="account" style="flex: 2;margin-top: 30px">

            <el-input prefix-icon="el-icon-user" size="mini" v-model="registerForm.account" style="width: 70%;"
                      placeholder="输入账户名"/>
            <!--                style="width: 197px;300px"-->
            <el-button size="mini" style="margin-left: 2px" type="primary" plain @click="confirmName(1)"><a>账户名验证</a>
            </el-button>
          </el-form-item>
          <!--          </div>-->
          <el-form-item prop="sid" style="margin-top:-8px">
            <el-input prefix-icon="el-icon-link" size="mini" style="width: 100%;" v-model="registerForm.sid"
                      placeholder="输入学号">
            </el-input>
          </el-form-item>
          <el-form-item prop="password" style="margin-top:-8px">
            <el-input prefix-icon="el-icon-lock" size="mini" show-password v-model="registerForm.password"
                      style="width: 100%;" placeholder="输入密码">
            </el-input>
          </el-form-item>
          <el-form-item prop="confirm" style="margin-top:-8px">
            <el-input prefix-icon="el-icon-lock" size="mini" show-password v-model="registerForm.confirm"
                      style="width: 100%;" placeholder="确认密码">
            </el-input>
          </el-form-item>
          <el-form-item prop="phone" style="margin-top:-8px">
            <el-input prefix-icon="el-icon-phone" size="mini" v-model="registerForm.phone" style="width: 100%;"
                      placeholder="输入电话号码（选填）">
            </el-input>
          </el-form-item>
          <el-form-item prop="email" style="margin-top:-8px">
            <el-input prefix-icon="el-icon-message" size="mini" style="width: 100%;" v-model="registerForm.email"
                      placeholder="输入邮箱（选填）">
            </el-input>
          </el-form-item>

          <div>
            <el-button type="primary" size="medium" style="width: 100%;background-color: green;" @click="register">
              确认注册
            </el-button>
          </div>
          <div style="margin: 20px 0 40px 34%;">
            <a href="/login" style="color: #aaa;">返回登录界面</a>
            <!--            -->
          </div>
        </el-form>

      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "RegisterWeb",
  data() {
    return {
      flag:0,
      codes: 0,
      registerForm: {},
      rules: {
        account: [
          {required: true, message: '请输入账户名', trigger: 'blur'},
        ],
        sid: [
          {required: true, message: '请输入学号', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
        confirm: [
          {required: true, message: '请确认密码', trigger: 'blur'},
        ],
        sex: [
          {required: true, message: '请选择性别', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    checkPassword() {//密码一致性检验
      if (this.registerForm.password == this.registerForm.confirm) {
        this.codes1 = 1
      } else {
        this.$message({
          type: "error",
          message: '两次密码不一致'
        })
        this.codes1 = 0
      }
      return this.codes1
    },
    Codes() {//表单验证
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.codes = 1
        } else {
          this.codes = 0
        }
      })
      return this.codes
    },
    confirmName(cflag) {//验证用户名
      if (this.registerForm.account == null || this.registerForm.account == '') {
        this.$refs['form'].validate('rules')
        return 0
      } else {
        request.post("/List/confirm", this.registerForm.account).then(res => {
          if (res.code === '200' && cflag == 1) {
            this.$message({
              type: "success",
              message: "该账户名可用"
            })
            return 1
          } else if (res.code != '200') {
            this.flag=0
            this.$message({
              type: "error",
              message: res.msg
            })
            return 0
          }
        })
      }
    },
    register() {
      if (this.Codes() == 1) {//注册信息正确无误
          if (this.checkPassword() == 1) {
            request.post("/List/register", {
              form: this.registerForm,
              stuId: this.registerForm.sid
            }).then(res => {
              if (res.code === '200') {
                this.$message({
                  type: "success",
                  message: "注册成功"
                })
                this.registerForm = null
                sessionStorage.clear()
                this.$router.push("/login")//注册成功！
              } else if (res.code != '200') {
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            })
          }
          this.flag=0
      } else {//防止不输入
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

.login-bg {
  width: 100%;
  height: 100%;
  overflow: hidden;
  position: absolute;
  top: 0px;
  z-index: 0;
}

.login-bg img {
  width: 100%;
}

.login-box {
  width: 33%;
  height: 75%;
  background-color: rgba(208, 219, 231, 0.3);
  margin: 5% auto 100px 32%;
  border-radius: 10px;
  z-index: 1;
  display: flex;
  position: absolute;
}

.login-box h2 {
  margin: 0 0 0;
  width: 100%;
  top: 10px;
  text-align: center;
  font-size: 24px;
  color: rgba(28, 25, 25, 0.6);
}
</style>