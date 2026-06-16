<template>
  <div class="product">
    <el-card>
      <div slot="header">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增商品</el-button>
        <el-input v-model="searchForm.name" placeholder="商品名称" style="width: 200px; margin-left: 10px" clearable></el-input>
        <el-button type="primary" icon="el-icon-search" style="margin-left: 10px" @click="handleSearch">搜索</el-button>
      </div>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="code" label="商品编码" width="120"></el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="150"></el-table-column>
        <el-table-column prop="unit" label="单位" width="80"></el-table-column>
        <el-table-column prop="price" label="单价" width="100"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
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
      <el-pagination
        style="margin-top: 20px; text-align: right"
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        :page-size.sync="pageSize"
        :current-page.sync="currentPage"
        @current-change="handleCurrentChange"
      ></el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="商品编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入商品编码"></el-input>
        </el-form-item>
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商品名称"></el-input>
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入单位"></el-input>
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-input-number v-model="form.price" :precision="2" :min="0"></el-input-number>
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
  name: 'Product',
  data() {
    return {
      tableData: [],
      total: 0,
      currentPage: 1,
      pageSize: 10,
      searchForm: {
        name: ''
      },
      dialogVisible: false,
      dialogTitle: '新增商品',
      form: {
        code: '',
        name: '',
        unit: '',
        price: 0,
        status: 1
      },
      rules: {
        code: [{ required: true, message: '请输入商品编码', trigger: 'blur' }],
        name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        const res = await axios.get('/v1/products', {
          params: {
            page: this.currentPage,
            size: this.pageSize,
            name: this.searchForm.name
          }
        })
        this.tableData = res.data.records
        this.total = res.data.total
      } catch (error) {
        console.error(error)
      }
    },
    handleSearch() {
      this.currentPage = 1
      this.fetchData()
    },
    handleCurrentChange() {
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增商品'
      this.form = {
        code: '',
        name: '',
        unit: '',
        price: 0,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑商品'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该商品吗？', '提示', {
        type: 'warning'
      }).then(async () => {
        await axios.delete(`/v1/products/${row.id}`)
        this.$message.success('删除成功')
        this.fetchData()
      })
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          if (this.form.id) {
            await axios.put(`/v1/products/${this.form.id}`, this.form)
          } else {
            await axios.post('/v1/products', this.form)
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
.product {
  padding: 0;
}
</style>