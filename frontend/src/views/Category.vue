<template>
  <div class="category">
    <el-card>
      <div slot="header">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增分类</el-button>
      </div>
      <el-table :data="tableData" style="width: 100%" row-key="id" :tree-props="{children: 'children'}">
        <el-table-column prop="name" label="分类名称" min-width="200"></el-table-column>
        <el-table-column prop="level" label="层级" width="80"></el-table-column>
        <el-table-column prop="sort" label="排序" width="80"></el-table-column>
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
        <el-form-item label="父分类">
          <el-select v-model="form.parentId" placeholder="无" style="width: 100%" clearable>
            <el-option :value="0" label="无（顶级分类）"></el-option>
            <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称"></el-input>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0"></el-input-number>
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
  name: 'Category',
  data() {
    return {
      tableData: [],
      categoryList: [],
      dialogVisible: false,
      dialogTitle: '新增分类',
      form: {
        parentId: 0,
        name: '',
        sort: 0,
        status: 1
      },
      rules: {
        name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        const res = await axios.get('/v1/categories')
        this.tableData = this.buildTree(res.data)
        this.categoryList = res.data.filter(item => item.level === 1)
      } catch (error) {
        console.error(error)
      }
    },
    buildTree(list) {
      const map = {}
      const tree = []
      list.forEach(item => {
        map[item.id] = { ...item, children: [] }
      })
      list.forEach(item => {
        if (item.parentId === 0) {
          tree.push(map[item.id])
        } else {
          map[item.parentId].children.push(map[item.id])
        }
      })
      return tree
    },
    handleAdd() {
      this.dialogTitle = '新增分类'
      this.form = {
        parentId: 0,
        name: '',
        sort: 0,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑分类'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该分类吗？', '提示', {
        type: 'warning'
      }).then(async () => {
        await axios.delete(`/v1/categories/${row.id}`)
        this.$message.success('删除成功')
        this.fetchData()
      })
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          if (this.form.id) {
            await axios.put(`/v1/categories/${this.form.id}`, this.form)
          } else {
            await axios.post('/v1/categories', this.form)
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
.category {
  padding: 0;
}
</style>