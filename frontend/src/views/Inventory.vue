<template>
  <div class="inventory">
    <el-card>
      <div slot="header">
        <el-input v-model="searchForm.productName" placeholder="商品名称" style="width: 200px" clearable></el-input>
        <el-select v-model="searchForm.warehouseId" placeholder="选择仓库" style="width: 150px; margin-left: 10px" clearable>
          <el-option v-for="item in warehouseList" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" style="margin-left: 10px" @click="handleSearch">搜索</el-button>
      </div>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="productName" label="商品名称" min-width="150"></el-table-column>
        <el-table-column prop="warehouseName" label="仓库" width="120"></el-table-column>
        <el-table-column prop="locationCode" label="库位" width="120"></el-table-column>
        <el-table-column prop="quantity" label="库存数量" width="100"></el-table-column>
        <el-table-column prop="frozenQuantity" label="冻结数量" width="100"></el-table-column>
        <el-table-column label="可用数量" width="100">
          <template slot-scope="scope">
            {{ scope.row.quantity - scope.row.frozenQuantity }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="text" @click="handleAdjust(scope.row)">调整</el-button>
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

    <el-dialog title="库存调整" :visible.sync="dialogVisible" width="400px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="商品名称">
          <el-input v-model="form.productName" disabled></el-input>
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input v-model="form.quantity" disabled></el-input>
        </el-form-item>
        <el-form-item label="调整数量">
          <el-input-number v-model="form.adjustQuantity" :min="-form.quantity"></el-input-number>
        </el-form-item>
        <el-form-item label="调整原因">
          <el-input v-model="form.reason" type="textarea" placeholder="请输入调整原因"></el-input>
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
  name: 'Inventory',
  data() {
    return {
      tableData: [],
      total: 0,
      currentPage: 1,
      pageSize: 10,
      searchForm: {
        productName: '',
        warehouseId: ''
      },
      warehouseList: [],
      dialogVisible: false,
      form: {
        id: '',
        productName: '',
        quantity: 0,
        adjustQuantity: 0,
        reason: ''
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
        const res = await axios.get('/v1/inventory', {
          params: {
            page: this.currentPage,
            size: this.pageSize
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
    handleAdjust(row) {
      this.form = {
        id: row.id,
        productName: row.productName,
        quantity: row.quantity,
        adjustQuantity: 0,
        reason: ''
      }
      this.dialogVisible = true
    },
    async handleSubmit() {
      if (this.form.adjustQuantity === 0) {
        this.$message.warning('调整数量不能为0')
        return
      }
      try {
        await axios.post('/v1/inventory/adjust', {
          id: this.form.id,
          quantity: this.form.quantity + this.form.adjustQuantity
        })
        this.$message.success('调整成功')
        this.dialogVisible = false
        this.fetchData()
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>

<style scoped>
.inventory {
  padding: 0;
}
</style>