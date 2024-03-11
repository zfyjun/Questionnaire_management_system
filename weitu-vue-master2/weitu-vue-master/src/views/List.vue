<template>
  <div>
    <van-nav-bar
        title="学生相关信息统计"
        right-text="提交"
        @click-right="send"
    />
    <div style="margin-top: 20px; " v-if="this.flag==true">
      <el-form ref="form" :model="registerForm" :rules="rules">
        <van-cell style="font-size: 15px">
          亲爱的同学们：<br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          你好，为全面掌握学院学生的基本信息情况，进行大数据分析及预测，从而生成新生用户画像分析图表。
          现在需要花费你几分钟时间做一个简单的调查问卷，我们将对你填写的调查问卷内容严格保密，希望大家积极配合参与，谢谢大家的理解和支持！
        </van-cell>
        <el-form-item prop="stu_id">
          <van-cell prop="stu_id" value="1. 学号" required style="font-size: 17px;font-weight: bold;"/>
          <van-field v-model="registerForm.stu_id" placeholder="请输入学号" style="font-size: 17px"/>
        </el-form-item>
        <el-form-item prop="username">
          <van-cell prop="username" value="2. 姓名" required style="font-size: 17px;font-weight: bold;"/>
          <van-field v-model="registerForm.username" placeholder="请输入用户名" style="font-size: 17px"/>
        </el-form-item>
        <el-form-item prop="age">
          <van-cell prop="age" value="3. 年龄" required style="font-size: 17px;font-weight: bold;"/>
          <van-field v-model="registerForm.age" placeholder="请输入年龄（数字）" style="font-size: 17px"/>
        </el-form-item>
        <el-form-item prop="sex">
          <van-cell prop="sex" value="4. 性别" required style="font-size: 17px;font-weight: bold;"/>
          <van-radio-group v-model="registerForm.sex" direction="horizontal">
            <van-radio name="男" style="margin: 15px;" label="男">男</van-radio>
            <van-radio name="女" style="margin: 15px;" label="女">女</van-radio>
          </van-radio-group>
        </el-form-item>

      </el-form>
      <el-form :model="merchant"
               ref="merchant"
               :rules="rules">
        <el-form-item prop="province">
          <van-cell value="5. 地址" required style="font-size: 17px;font-weight: bold;"/>
          <el-select v-model="merchant.province"
                     placeholder="请选择省份（直辖市）"
                     @change="renderCity"
                     clearable
                     style="margin-left: 15px;width: 150px;font-size: 16px">
            <!--            clearable size="mini"-->
            <el-option
                v-for="item in provinceListAll"
                :key="item.name"
                :value="item.name">
            </el-option>
          </el-select>
          <!--          <div style="margin-left: 88px">-->
          <el-select v-model="merchant.city"
                     placeholder="请选择城市"
                     v-if="citySelectAble"
                     clearable
                     style="margin-left: 5px;width: 150px;font-size: 16px">
            <!--              clearable size="mini"-->
            <el-option
                v-for="item in cityListAll"
                :key="item.name"
                :value="item.name">
            </el-option>
          </el-select>
          <!--          </div>-->
        </el-form-item>
      </el-form>
      <!--      <div>-->
      <!--        <el-button size="mini" @click="send" style="margin-left: 200px">提交</el-button>-->
      <!--      </div>-->
    </div>
    <!--  提交后  -->
    <div style="padding: 10px;margin-top: 50px" v-if="this.flag==false">
      <el-image style="width: auto; height: auto;margin-left: 80px" :src="require('../images/thanks.png')"></el-image>
      <div><B style="color:deepskyblue;font-size: 30px;margin-left: 120px">谢谢参与</B></div>
      <div style="margin-left: 150px;margin-top:10px;color: deepskyblue" v-if="student.uid==null">
        <a href="/register" style="color: deepskyblue">立即注册</a>
      </div>
    </div>
  </div>
</template>

<script>

import request from "@/utils/request";
import {
  Toast,
  Button,
  Cell,
  CellGroup,
  Field,
  Picker,
  Popup,
  NavBar,
  Area,
  RadioGroup,
  Radio,
  Stepper
} from "vant";

const defaultMerchant = {
  id: null,
  storeName: '',    // 门店名称
  province: '',  // 所属省（直辖市）
  city: '',     // 所属市
};

// const handleChange = (value: number) => {
//   console.log(value)
// }

export default {
  name: "List",
  components: {
    [Button.name]: Button,
    [Cell.name]: Cell,
    [CellGroup.name]: CellGroup,
    [Field.name]: Field,
    [Picker.name]: Picker,
    [Popup.name]: Popup,
    [NavBar.name]: NavBar,
    [Area.name]: Area,
    [RadioGroup.name]: RadioGroup,
    [Radio.name]: Radio,
    [Stepper.name]: Stepper,
  },
  data() {
    return {
      onClickLeft() {
        Toast('返回');
      },
      // onClickRight() {
      //   Toast('提交');
      // },
      columns: [], //选择器中的值
      // registerForm.age: false,//控制弹出层是否弹出的值
      ageId: -1,
      ageValue: "",
      // radio: '1',
      flag: JSON.parse(sessionStorage.getItem('listflag')),
      registerForm: {},
      merchant: Object.assign({}, defaultMerchant),
      modal1: true,
      isEdit: true,
      rules: {    // 用于表单验证的规则
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        age: [
          {required: true, message: '请输入年龄', trigger: 'blur'},
        ],
        stu_id: [
          {required: true, message: '请输入学号', trigger: 'blur'},
        ],
        phone: [
          {required: true, message: '请输入手机号', trigger: 'blur'},
        ],
        sex: [
          {required: true, message: '请选择性别', trigger: 'blur'},
        ],
        province: [
          {required: true, message: '请输入所属省份（或者直辖市）', trigger: 'blur'}
        ],
      },
      provinceListAll: [],      // 全国34个省份（包括直辖市）列表
      cityListAll: [],        // 全国各个城市列表
      citySelectAble: false,    // 城市是否可选
      student:{},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },

  methods: {
    // handleChange(value) {
    //   console.log(value);
    // },
    getAllProvinceAndCityList() {     // 获取全国所有省份和城市的列表
      var that = this;
      this.$http.get('https://restapi.amap.com/v3/config/district', {// 高德地图的API
        params: {
          key: '95cbcc898acc8b2128c1d9bf2550d3be',   // 这个是申请的 Key，读者自行写入
          keywords: '中国',
          subdistrict: 3,
          extensions: 'base'
        }
      }).then((response) => {
        // console.log(response.data)
        that.provinceListAll = response.data.districts[0].districts;
        for (let i = 0; i < response.data.districts[0].districts.length; i++) {
          for (let j = 0; j < response.data.districts[0].districts[i].districts.length; j++) {
            that.cityListAll.push(response.data.districts[0].districts[i].districts[j]);
          }
        }
      });
    },
    renderCity() {    // 根据选择的省份，展示该省份相对应的城市列表，未选择省份时，不能选择城市
      if (this.merchant.province.length > 0) {
        this.cityListAll = this.provinceListAll.filter(item => item.name == this.merchant.province)[0].districts;
        this.citySelectAble = true;
      } else if (!this.merchant.province) {
        this.citySelectAble = false;
        this.merchant.city = '';     // 清空省份选择时，清空之前选择的城市能进行重新选择城市
      }
    },
    Codes() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.codes = 1
        } else {
          this.codes = 0
        }
      })
      return this.codes
    },
    send() {
      if (this.Codes() == 1) {//注册信息正确无误
        request.post("/List/sendlist", {
          table: this.registerForm,
          address: this.merchant
        }).then(res => {
          if (res.code === '200') {
            this.$message({
              type: "success",
              message: "提交成功"
            })
            sessionStorage.setItem('listflag', JSON.stringify(false))
            sessionStorage.setItem('stuId', JSON.stringify(this.registerForm.stu_id))
            this.load()
          } else if (res.code != '200') {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
        })
      } else {//防止不输入
        this.$refs['form'].validate('rules')
      }
    },
    load() {//加载
      if (JSON.parse(sessionStorage.getItem('listflag')) == null) {
        this.flag = true;
      } else {
        this.flag = JSON.parse(sessionStorage.getItem('listflag'))
      }
      this.getAllProvinceAndCityList();
    },
    onConfirm(v, i) {
      Toast(`当前值：${v}, 当前索引：${i}`);
      this.ageId = i;
      this.ageValue = v;
      // this.setData({showPicker: false});
      this.registerForm.age = false;
    },
    onCancel() {
      Toast('取消');
      this.registerForm.age = false;
    },
    getList() {
      for (let i = 0; i < 151; i++) {
        this.columns.push(i)
      }
    },
    getuserId(){
      this.request.post("/student/studentinfo/"+this.user.id).then(res=>{
        this.student=res.data[0]
        console.log(res.data)
        if(res.code==='200')
        this.registerForm.stu_id=this.student.stuid
      })
    }
  },
  watch: {   // watch 监听 merchant.province，当城市选择后，再次换省份时，能清空已选择的城市
    'merchant.province': function (newValue, oldValue) {
      this.merchant.city = '';
    }
  },
  created() {    // 页面加载时调用的函数
    this.getuserId()
    this.load();
    this.getList();
  },

  showPopup(v) {      //点击选择，打开弹出层（选择器）
    this.registerForm.age = true;
  },
  onClose() {     //点击空白处开闭弹出层（选择器）及选择器左上角的取消
    this.registerForm.age = false;
  },
}
</script>

<style scoped>
/deep/ .van-nav-bar {
  background-color: #009bbe;
}

/deep/ .van-nav-bar__title {
  font-size: 20px;
  font-weight: bold;
  color: #ffffff;
}

/deep/ .van-nav-bar__text {
  font-size: 18px;
  color: #ffffff;
}

/deep/ .el-form-item__error {
  margin-left: 15px;
}


/*/deep/ .van-icon {*/
/*  color: #000000;*/
/*}*/
</style>