<template>
<div>
  <!--搜索框-->
  <div style="padding: 10px 0">
    <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name" class="ml-5"></el-input>
    <el-button class="ml-5" type="primary" @click="search">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
  </div>
  <!--功能按钮-->
  <div style="margin: 10px 0">
    <el-upload action="http://39.107.95.5:9090/file/upload" :show-file-list="false" :on-success="handleFileUploadSuccess" style="display: inline">
      <el-button type="primary" class="ml-5">新增<i class="el-icon-top"></i></el-button>
    </el-upload>
    <!--          批量删除弹窗确认框-->
  <el-popconfirm
      class="ml-5"
      confirm-button-text='确定'
      cancel-button-text='我再想想'
      icon="el-icon-info"
      icon-color="red"
      title="您确定批量删除这些数据吗？"
      @confirm="delBatch"
  >
    <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
  </el-popconfirm>

  </div>

  <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="ID" width="140"></el-table-column>
    <el-table-column prop="name" label="文件名称"></el-table-column>
    <el-table-column prop="type" label="文件类型"></el-table-column>
    <el-table-column prop="size" label="文件大小(kb)"></el-table-column>
    <el-table-column label="下载">
      <template slot-scope="scope">
        <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
      </template>
    </el-table-column>
    <!--          表格里面的操作按钮-->
    <el-table-column label="操作">
      <template slot-scope="scope">
        <!--              删除弹出确认框-->
        <el-popconfirm
            class="ml-5"
            confirm-button-text='确定'
            cancel-button-text='我再想想'
            icon="el-icon-info"
            icon-color="red"
            title="您确定删除吗？"
            @confirm="del(scope.row.id)"
        >
          <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
        </el-popconfirm>
      </template>
    </el-table-column>

  </el-table>

<!--  分页功能-->
  <div style="padding: 10px 0">
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 5,10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
</div>
</template>

<script>

export default {
  name: "File",
  data() {
    return {
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum:1,
      pageSize:10,
      total:0
    }
  },
  created() {
    this.load()
  },
  methods: {
    search(){
      this.pageNum=1
      this.load()
    },
    load() { //封装的加载页面的方法
      this.request.get("/file/page",{
        params: {
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          name:this.name,
        }
      }).then(res => {
        console.log(res)

        this.tableData=res.data.records
        this.total=res.data.total

      })

    },
    reset() {
      this.name=""
      this.load()
    },
    del(id) {
      this.request.delete("/file/" + id).then(res => {
        if(res) {
          this.$message.success("删除成功")
          const totalPage = Math.ceil((this.total - 1) / this.pageSize)
          const pagenum = this.pageNum > totalPage ? totalPage : this.pageNum
          this.pageNum = pagenum < 1 ? 1 : pagenum
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id) //把一个对象数组变为传id的数组,类似于[{}, {}, {}] => [1,2,3]
      this.request.post("/file/del/batch", ids).then(res => {
        if(res) {
          this.$message.success("批量删除成功")
          const totalPage = Math.ceil((this.total - 1) / this.pageSize)
          const pagenum = this.pageNum > totalPage ? totalPage : this.pageNum
          this.pageNum = pagenum < 1 ? 1 : pagenum
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection=val
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize=pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum=pageNum
      this.load()
    },
    handleFileUploadSuccess(res) {
      console.log(res)
      this.load()
    },
    download(url) {
      window.open(url)
    }

  }

}
</script>

<style scoped>

</style>