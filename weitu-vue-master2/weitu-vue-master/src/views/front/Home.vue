<template>
<!--  <div>前台主页</div>-->
  <div class="index">
    <!-- 跑马灯  -->
    <div>
      <el-carousel :interval="4000" >
        <el-carousel-item v-for="item in imgList" :key="item.id">
          <img :src="item.idView" class="image">
        </el-carousel-item>
      </el-carousel>
    </div>
    <div class="demo-collapse">
      <el-collapse v-model="activeName" >
        <el-collapse-item title="公告"  name="0">
          <div>
           通知：重大喜讯，新生问卷管理系统正式投入使用！！！欢迎广大同学进行使用，并为我们提出宝贵的意见！！！
          </div>
          <div>
           通知：新生问卷管理系统已进行全面升级！！！添加新功能：是否填写问卷！！！
          </div>
        </el-collapse-item>
        <el-collapse-item title="项目介绍"  name="1">
          <div>
            本项目基于是对某学院学生相关数据（人数占比、能力评估、性别占比、住址、迁徙行为习惯等）进行收集，进行大数据分析及预测，并进行数据可视化展示。
          </div>
          <div>
            This project is based on collecting data related to students (proportion of students, ability assessment, gender ratio, address, migration behavior, etc.), conducting big data analysis and prediction, and visualizing and displaying data.
          </div>
        </el-collapse-item>
        <el-collapse-item title="核心组件" name="2">
          <div>
            通过中软国际合作单位以及日常产品开发中所提供的技术架构，可视化管理模板，第三方通用Api等，为项目管理与数据智能化应用提供支撑和保障。
          </div>
          <div>
            Through the technical architecture, visual management templates, third-party general APIs, etc. provided by Chinasoft international cooperation units and daily product development, it provides support and guarantee for project management and data intelligent applications.
          </div>
        </el-collapse-item>
        <el-collapse-item title="开发人员" name="3">
          <div>
            张三，李四，王五
          </div>
          <div>
            Zhang San, Li Si, Wang Wu
          </div>
          <!--        <div>-->
          <!--          Easy to identify: the interface should be straightforward, which helps-->
          <!--          the users to identify and frees them from memorizing and recalling.-->
          <!--        </div>-->
        </el-collapse-item>
        <!--      <el-collapse-item title="Controllability" name="4">-->
        <!--        <div>-->
        <!--          Decision making: giving advices about operations is acceptable, but do-->
        <!--          not make decisions for the users;-->
        <!--        </div>-->
        <!--        <div>-->
        <!--          Controlled consequences: users should be granted the freedom to-->
        <!--          operate, including canceling, aborting or terminating current-->
        <!--          operation.-->
        <!--        </div>-->
        <!--      </el-collapse-item>-->
      </el-collapse>
    </div>
  </div>

</template>


<script>
export default {
  name: "Home",
  data() {
    return {
      activeName:"0",
      imgList: [
        {id:0,idView:require('@/assets/pbg1.jpg')},
        {id:1,idView:require('@/assets/pbg2.jpg')},
        {id:2,idView:require('@/assets/pbg3.jpg')},
        {id:3,idView:require('@/assets/pbg4.jpg')},
      ]
    }
  }
}
</script>

<style scoped>
/deep/ .el-carousel__container{
  height: 300px;
}
.index{
  background: #ffffff;
  width: 100%;
  height: 100%;
  /*text-align: center;*/
  /*margin:0 auto;*/
  /*margin-left: 142px;*/
  /*width: 80.6%;*/

}
.image{
  width: 100%;
  height: 100%;
}
.demo-collapse{
  width: 100%;
  margin: auto auto;

}
/deep/ .el-collapse-item__header{
  font-size: 16px;
}
</style>