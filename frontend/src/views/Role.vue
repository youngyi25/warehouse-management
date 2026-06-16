<template>
  <div class="role">
    <el-card>
      <div slot="header">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增角色</el-button>
      </div>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="code" label="角色编码" width="120"></el-table-column>
        <el-table-column prop="name" label="角色名称" min-width="150"></el-table-column>
        <el-table-column prop="description" label="描述" min-width="200"></el-table-column>
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
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入角色编码"></el-input>
        </el-form-item>
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" placeholder="请输入描述"></el-input>
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
  name: 'Role',
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      dialogTitle: '新增角色',
      form: {
        code: '',
        name: '',
        description: '',
        status: 1
      },
      rules: {
        code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
        name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        const res = await axios.get('/v1/roles')
        this.tableData = res.data
      } catch (error) {
        console.error(error)
      }
    },
    handleAdd() {
      this.dialogTitle = '新增角色'
      this.form = {
        code: '',
        name: '',
        description: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑角色'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该角色吗？', '提示', {
        type: 'warning'
      }).then(async () => {
        await axios.delete(`/v1/roles/${row.id}`)
        this.$message.success('删除成功')
        this.fetchData()
      })
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          if (this.form.id) {
            await axios.put(`/v1/roles/${this.form.id}`, this.form)
          } else {
            await axios.post('/v1/roles', this.form)
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
.role {
  padding: 0;
}
</style>