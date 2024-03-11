<template>
<!--  :default-openeds="opens"-->

  <el-menu  style="min-height: 650px;height: 100%;overflow-x: hidden"
           background-color="rgb(48,65,86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >


<!--    //侧边栏背景颜色-->
    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px;">
      <b style="color: white; margin-left: 5px">
<!--        <span slot="title">后台管理系统</span>-->
      </b>

      <el-menu-item index="/home">
        <i class="el-icon-monitor"></i>
        <span slot="title">首页</span>
      </el-menu-item>
      <el-menu-item index="/student">
        <i class="el-icon-user-solid"></i>
        <span slot="title">管理学生信息</span>
      </el-menu-item>
      <el-menu-item index="/file">
        <i class="el-icon-user-solid"></i>
        <span slot="title">文件管理</span>
      </el-menu-item>
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-user-solid"></i>
          <span slot="title">问卷管理</span>
        </template>

        <el-menu-item index="/listEdit">
          <i class="el-icon-success"></i>
          <span slot="title">问卷编辑</span>
        </el-menu-item>

        <el-menu-item index="/listPublish">
          <i class="el-icon-success"></i>
          <span slot="title">问卷发布</span>
        </el-menu-item>

        <el-menu-item index="/listManagement">
          <i class="el-icon-success"></i>
          <span slot="title">问卷统计</span>
        </el-menu-item>

        <el-menu-item index="/isDelete">
          <i class="el-icon-success"></i>
          <span slot="title">问卷恢复</span>
        </el-menu-item>


      </el-submenu>


    </div>


<!--    <div v-for="item in menus" :key="item.id">-->
<!--      <div v-if="item.path">-->
<!--        <el-menu-item :index="item.path">-->
<!--            <i :class="item.icon"></i>-->
<!--            <span slot="title">{{ item.name }}</span>-->
<!--        </el-menu-item>-->
<!--      </div>-->
<!--      <div v-else>-->
<!--        <el-submenu :index="item.id+''">-->
<!--          <template slot="title">-->
<!--            <i :class="item.icon"></i>-->
<!--            <span slot="title">{{ item.name }}</span>-->
<!--          </template>-->
<!--          <div v-for="subItem in item.children" :key="subItem.id">-->
<!--            <el-menu-item :index="subItem.path">-->
<!--              <i :class="subItem.icon"></i>-->
<!--              <span slot="title">{{ subItem.name }}</span>-->
<!--            </el-menu-item>-->
<!--          </div>-->

<!--        </el-submenu>-->
<!--      </div>-->
<!--    </div>-->
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props:{
    isCollapse:Boolean,
    logoTextShow: Boolean,
  },
  methods:{
    // handleSelect(index){
    //   console.log(index)
    //   alert(this.$router.name)
    // }
  },
  data() {
    return {
      // menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
      // opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : []
    }
  },
}
</script>

<style scoped>

</style>