<template>

  <div>

    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="search">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference" >批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange"  >
      <el-table-column type="selection" width="55" class="table-style"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="listname" label="问卷名称"></el-table-column>
      <el-table-column prop="btime" label="开始时间"></el-table-column>
      <el-table-column prop="etime" label="结束时间"></el-table-column>
      <el-table-column label="操作"  width="280" align="center">
        <template slot-scope="scope">
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定恢复吗？"
              @confirm="recover(scope.row.id)"
          >
            <el-button type="success" slot="reference">恢复<i class="el-icon-edit"></i></el-button>
          </el-popconfirm>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">彻底删除<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
          </template>
        </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "IsDelete",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      name:"",
      form: {},
      dialogFormVisible: false,
      menuDialogVis: false,
      multipleSelection: [],
      courses:[]
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
    load() {
      this.request.get("/List/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name:this.name
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      this.request.post("/student", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    recover(id){
      this.form=JSON.parse(id);
      this.request.post("/List/recover",this.form).then(res=>{
        if (res.code === '200') {
          this.$message.success("恢复成功")
          this.load()
        } else {
          this.$message.error("恢复失败")
        }
      })
    },
    del(id) {
      this.request.delete("/List/DeleteTrue/" + id).then(res => {
        if (res.code === '200') {
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
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/List/del/batch", ids).then(res => {
        if (res.code === '200') {
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
    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
.table-style{
  background-color: #095fcc;
}

</style>
