<template>
  <div class="location">
    <el-card>
      <div slot="header">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增库位</el-button>
        <el-select v-model="searchForm.warehouseId" placeholder="选择仓库" style="width: 150px; margin-left: 10px" clearable>
          <el-option v-for="item in warehouseList" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" style="margin-left: 10px" @click="handleSearch">搜索</el-button>
      </div>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="code" label="库位编码" width="120"></el-table-column>
        <el-table-column prop="name" label="库位名称" min-width="150"></el-table-column>
        <el-table-column prop="warehouseName" label="所属仓库" width="120"></el-table-column>
        <el-table-column prop="area" label="区域" width="80"></el-table-column>
        <el-table-column prop="capacity" label="容量" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" style="color: #F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="所属仓库" prop="warehouseId">
          <el-select v-model="form.warehouseId" placeholder="请选择仓库" style="width: 100%">
            <el-option v-for="item in warehouseList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="库位编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入库位编码"></el-input>
        </el-form-item>
        <el-form-item label="库位名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入库位名称"></el-input>
        </el-form-item>
        <el-form-item label="区域">
          <el-input v-model="form.area" placeholder="请输入区域"></el-input>
        </el-form-item>
        <el-form-item label="容量">
          <el-input-number v-model="form.capacity" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Location',
  data() {
    return {
      tableData: [],
      warehouseList: [],
      searchForm: {
        warehouseId: ''
      },
      dialogVisible: false,
      dialogTitle: '新增库位',
      form: {
        warehouseId: '',
        code: '',
        name: '',
        area: '',
        capacity: 0,
        status: 1
      },
      rules: {
        warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }],
        code: [{ required: true, message: '请输入库位编码', trigger: 'blur' }],
        name: [{ required: true, message: '请输入库位名称', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchData()
    this.fetchWarehouseList()
  },
  methods: {
    async fetchData() {
      try {
        const res = await axios.get('/v1/locations', {
          params: {
            warehouseId: this.searchForm.warehouseId
          }
        })
        this.tableData = res.data
      } catch (error) {
        console.error(error)
      }
    },
    async fetchWarehouseList() {
      try {
        const res = await axios.get('/v1/warehouses')
        this.warehouseList = res.data
      } catch (error) {
        console.error(error)
      }
    },
    handleSearch() {
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增库位'
      this.form = {
        warehouseId: '',
        code: '',
        name: '',
        area: '',
        capacity: 0,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑库位'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该库位吗？', '提示', {
        type: 'warning'
      }).then(async () => {
        await axios.delete(`/v1/locations/${row.id}`)
        this.$message.success('删除成功')
        this.fetchData()
      })
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          if (this.form.id) {
            await axios.put(`/v1/locations/${this.form.id}`, this.form)
          } else {
            await axios.post('/v1/locations', this.form)
          }
          this.$message.success('操作成功')
          this.dialogVisible = false
          this.fetchData()
        }
      })
    }
  }
}
</script>

<style scoped>
.location {
  padding: 0;
}
</style>