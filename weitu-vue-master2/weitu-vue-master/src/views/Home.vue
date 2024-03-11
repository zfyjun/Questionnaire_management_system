<template>
  <div>
    <div class="index">
      <!-- 跑马灯  -->
      <div>
        <el-carousel :interval="4000" type="card" height="300px" >
          <el-carousel-item v-for="item in imgList" :key="item.id">
            <img :src="item.idView" class="image">
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
    <div class="demo-collapse">
      <el-collapse v-model="activeName" accordion>
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
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>

<script>
export default {
  name: "Home",
  data(){
    return {
      imgList: [
        {id:0,idView:require('@/assets/picture3.jpg')},
        {id:1,idView:require('@/assets/p1.jpg')},
        {id:2,idView:require('@/assets/p2.jpg')},
        {id:3,idView:require('@/assets/p3.jpg')},
      ]
    };
  },
  methods: {
  },
}
</script>

<style scoped>
.index{
  background: #ffffff;
  width: 100%;
  height: 100%;
  text-align: center;
  /*margin:0 auto;*/
  /*margin-left: 142px;*/
  /*width: 80.6%;*/

}
.image{
  width: 520px;
  height:290px;
}
/deep/ .el-collapse-item__header{
  font-size: 19px;
}
/deep/ .el-collapse-item__content{
  font-size: 15px;
}
.demo-collapse{
  width: 92%;
  margin: auto auto;
}
</style>