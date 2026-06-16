<template>
  <div class="inbound">
    <el-card>
      <div slot="header">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新建入库单</el-button>
        <el-select v-model="searchForm.type" placeholder="入库类型" style="width: 120px; margin-left: 10px" clearable>
          <el-option label="采购入库" :value="1"></el-option>
          <el-option label="退货入库" :value="2"></el-option>
          <el-option label="调拨入库" :value="3"></el-option>
        </el-select>
        <el-select v-model="searchForm.status" placeholder="状态" style="width: 120px; margin-left: 10px" clearable>
          <el-option label="待审核" :value="0"></el-option>
          <el-option label="已审核" :value="1"></el-option>
          <el-option label="已完成" :value="2"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" style="margin-left: 10px" @click="handleSearch">搜索</el-button>
      </div>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="orderNo" label="入库单号" width="150"></el-table-column>
        <el-table-column prop="type" label="入库类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.type === 1 ? 'success' : scope.row.type === 2 ? 'warning' : 'info'">
              {{ scope.row.type === 1 ? '采购入库' : scope.row.type === 2 ? '退货入库' : '调拨入库' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warehouseName" label="仓库" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 2 ? 'success' : scope.row.status === 1 ? 'primary' : 'info'">
              {{ scope.row.status === 2 ? '已完成' : scope.row.status === 1 ? '已审核' : '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" v-if="scope.row.status === 0" @click="handleAudit(scope.row)">审核</el-button>
            <el-button type="text" v-if="scope.row.status === 1" @click="handleExecute(scope.row)">执行</el-button>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="入库类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择入库类型" style="width: 100%">
                <el-option label="采购入库" :value="1"></el-option>
                <el-option label="退货入库" :value="2"></el-option>
                <el-option label="调拨入库" :value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="仓库" prop="warehouseId">
              <el-select v-model="form.warehouseId" placeholder="请选择仓库" style="width: 100%">
                <el-option v-for="item in warehouseList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注"></el-input>
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
  name: 'Inbound',
  data() {
    return {
      tableData: [],
      total: 0,
      currentPage: 1,
      pageSize: 10,
      searchForm: {
        type: '',
        status: ''
      },
      warehouseList: [],
      dialogVisible: false,
      dialogTitle: '新建入库单',
      form: {
        type: '',
        warehouseId: '',
        remark: ''
      },
      rules: {
        type: [{ required: true, message: '请选择入库类型', trigger: 'change' }],
        warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }]
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
        const res = await axios.get('/v1/inbound', {
          params: {
            page: this.currentPage,
            size: this.pageSize,
            type: this.searchForm.type,
            status: this.searchForm.status
          }
        })
        this.tableData = res.data.records
        this.total = res.data.total
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
      this.currentPage = 1
      this.fetchData()
    },
    handleCurrentChange() {
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新建入库单'
      this.form = {
        type: '',
        warehouseId: '',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleView(row) {
      this.$alert(`入库单号：${row.orderNo}<br>状态：${row.status === 2 ? '已完成' : row.status === 1 ? '已审核' : '待审核'}`, '入库单详情', {
        dangerouslyUseHTMLString: true
      })
    },
    handleAudit(row) {
      this.$confirm('确认审核该入库单吗？', '提示', {
        type: 'warning'
      }).then(async () => {
        await axios.put(`/v1/inbound/${row.id}/audit`, { status: 1 })
        this.$message.success('审核成功')
        this.fetchData()
      })
    },
    handleExecute(row) {
      this.$confirm('确认执行该入库单吗？执行后将更新库存', '提示', {
        type: 'warning'
      }).then(async () => {
        await axios.put(`/v1/inbound/${row.id}/execute`)
        this.$message.success('执行成功')
        this.fetchData()
      })
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          await axios.post('/v1/inbound', this.form)
          this.$message.success('创建成功')
          this.dialogVisible = false
          this.fetchData()
        }
      })
    }
  }
}
</script>

<style scoped>
.inbound {
  padding: 0;
}
</style>