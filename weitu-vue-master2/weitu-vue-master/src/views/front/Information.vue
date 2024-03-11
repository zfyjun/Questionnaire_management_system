<template>
  <!--  学员-->
  <div class="row">
    <div>
      <el-button icon="el-icon-back" @click="$router.push('/front/home')" size="mini" class="back">返回</el-button>
      <!--            -->
    </div>
    <el-row :gutter="0">
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>个人中心</span>
            </div>
          </template>
          <div style="width:70px;height: 70px;border-radius: 100px;display:inline-block; object-position: center;">
            <img
                style="width:100%;height: 100%;border-radius: 100px;margin-left: 170%"
                src="@/images/login1.jpg"
                class="image"
            >
          </div>
          <el-divider></el-divider>
          <div class="personal-relation">
            <div class="relation-item" style=" margin-top: -9px;">姓名:<span style="margin-left: 10px">{{this.form.name}}</span>
              <div style="float: right; padding-right:20px;"></div>
            </div>
          </div>
          <div class="personal-relation">
            <div class="relation-item">性别:<span style="margin-left: 10px">{{this.form.sex}}</span>
              <div style="float: right; padding-right:20px;"></div>
            </div>
          </div>
          <div class="personal-relation">
            <div class="relation-item">年龄:<span style="margin-left: 10px">{{this.form.age}}</span>
              <div style="float: right; padding-right:20px;"></div>
            </div>
          </div>
          <div class="personal-relation">
            <div class="relation-item">地址:<span style="margin-left: 10px">{{this.form.address}}</span>
              <div style="float: right; padding-right:20px;"></div>
            </div>
          </div>
          <div class="personal-relation">
            <div class="relation-item">籍贯:<span style="margin-left: 10px">{{this.form.birthplace}}</span>
              <div style="float: right; padding-right:20px;"></div>
            </div>
          </div>
          <div class="personal-relation">
            <div class="relation-item">电话:<span style="margin-left: 10px">{{this.form.phone}}</span>
              <div style="float: right; padding-right:20px;"></div>
            </div>
          </div>
          <div class="personal-relation">
            <div class="relation-item">邮箱:<span style="margin-left: 10px">{{this.form.email}}</span>
              <div style="float: right; padding-right:20px;"></div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-cards">
          <template #header>
            <div class="card-header">
              <span>修改个人信息</span>
            </div>
          </template>
          <!--填写个人信息-->
          <el-form ref="form" :model="form" label-width="100px">
            <el-form-item prop="name" label="姓 名">
              <el-input v-model="form.name" class="in"></el-input>
            </el-form-item>
            <el-form-item prop="sex" label="性 别">
              <el-radio-group v-model="form.sex">
                <el-radio label="男"></el-radio>
                <el-radio label="女"></el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="年 龄">
              <el-input v-model="form.age" class="in"></el-input>
            </el-form-item>
            <el-form-item prop="address" label="地 址">
              <el-input v-model="form.address" class="in"></el-input>
            </el-form-item>
            <el-form-item prop="birthplace" label="籍 贯">
              <el-input v-model="form.birthplace" class="in"></el-input>
            </el-form-item>
            <el-form-item label="电 话">
              <el-input v-model="form.phone" class="in"></el-input>
            </el-form-item>
            <el-form-item label="邮 箱">
              <el-input v-model="form.email" class="in"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="save" style="margin-left: 76%">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>

export default {
  name: "Information",
  data() {
    return {
      user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):{},
      form: {},
      tableData: [],
    }

  },
  //页面加载调用load()的方法
  created() {
    this.load()
  },
  methods: {
    //查询方法
    load() {
      this.request.post("/student/studentinfo/"+this.user.id).then(res => {
        this.form = res.data[0]
      })
    },
    save() {
      this.request.post("/student", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
  }
}
</script>

<style>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.in {
  width: 90%;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.row {
  background: url("../../images/login1.jpg") ;
  width:99.4%;
  height:87.7%;
  position:absolute;
  background-size:100% 100%;
  margin-left: -17%;
}

.back{
  font-size:16px;
  font-weight:bold;
  border-color:rgba(255,255,255,0);
  background-color: rgba(255,255,255,0);
  color: rgb(206,196,187);
}
.box-card {
  height: 97%;
  width: 80%;
  margin-left: 15%;
  margin-top: 10px;
}

.box-cards {
  height: 97%;
  width: 170%;
  margin-left: 7%;
  margin-top: 10px;
}

.personal-relation {
  font-size: 15px;
  padding: 0px 5px 0px;
  margin-right: 1px;
  margin-top: 0px;
  width: 100%
}

.relation-item {
  padding: 10px;
}

.information-bg {
  width: 100%;
  height: 100%;
  overflow: hidden;
  position: absolute;
  top: 0px;
  z-index: 0;
}

.information-bg img {
  width: 100%;
}

</style>
