<template>
  <div style="font-size: 12px; line-height:60px; display: flex">

    <div style="flex: 1;font-size: 18px">
      <span :class="collapseBtnClass" style="cursor:pointer" @click="collapse"></span>
        <el-breadcrumb separator-class="el-icon-arrow-right" style="display: inline-block;margin-left: 10px">
          <el-breadcrumb-item :to="{ path: '/home' }" >首页</el-breadcrumb-item>
<!--          <el-breadcrumb-item>{{currentPathName}}</el-breadcrumb-item>-->
        </el-breadcrumb>
    </div>

    <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
      <div style="display: inline-block">
<!--        <img :src="user.avatarUrl" alt=""-->
<!--             style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">-->
        <span>{{user.account}}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      </div>
      <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
<!--        <el-dropdown-item style="font-size: 14px; padding: 5px 0">-->
<!--          <router-link to="/password">修改密码</router-link>-->
<!--        </el-dropdown-item>-->
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <router-link to="/information">个人信息</router-link>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <span style="text-decoration: none" @click="logout">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>

</template>

<script>
export default {
  name: "Header",
  props:{
    collapseBtnClass:String,
    collapse:Boolean,
    user: Object
  },
  // watch:{
  //   'route':function(){
  //    this.currentPathName=localStorage.getItem("currentPathName")
  //   }
  // },
  // computed:{
  //   currentPathName(){
  //     return this.$store.state.currentPathName();
  //   }
  // },
  data(){
    return{
      // currentPathName:localStorage.getItem("currentPageName")
      // user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")):{},
    }
  },
  methods:{
    // collapse(){
    //   this.$emit("asideCollapse")
    // },
    created(){
      console.log(this.user)
    },
    logout(){
      // this.$store.commit("logout")
      localStorage.removeItem("user")
      // localStorage.removeItem("menus")
      this.$router.push("/login")
      this.$message.success("退出成功")
    },

  },
}
</script>

<style scoped>

</style>