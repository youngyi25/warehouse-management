<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6" v-for="(item, idx) in statCards" :key="idx">
        <div class="stat-card" :class="'stat-card-' + item.color">
          <div class="stat-card-bg"></div>
          <div class="stat-icon">
            <i :class="item.icon"></i>
          </div>
          <div class="stat-info">
            <div class="stat-title">{{ item.title }}</div>
            <div class="stat-value">{{ item.value }}</div>
          </div>
          <div class="stat-line"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-card-header">
            <div class="chart-title">
              <div class="title-dot"></div>
              <span>近7天出入库趋势</span>
            </div>
          </div>
          <div ref="trendChart" class="chart-body"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-card-header">
            <div class="chart-title">
              <div class="title-dot dot-green"></div>
              <span>各仓库库存分布</span>
            </div>
          </div>
          <div ref="warehouseChart" class="chart-body"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-card-header">
            <div class="chart-title">
              <div class="title-dot dot-orange"></div>
              <span>分类库存占比</span>
            </div>
          </div>
          <div ref="categoryChart" class="chart-body"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-card-header">
            <div class="chart-title">
              <div class="title-dot dot-red"></div>
              <span>最近入库单</span>
            </div>
          </div>
          <div class="table-body">
            <el-table :data="recentInbound" style="width: 100%" max-height="280">
              <el-table-column prop="orderNo" label="单号" width="160"></el-table-column>
              <el-table-column prop="type" label="类型" width="100">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.type === 1 ? 'success' : 'warning'" size="small" effect="dark">
                    {{ scope.row.type === 1 ? '采购入库' : '退货入库' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.status === 2 ? 'success' : 'info'" size="small" effect="dark">
                    {{ scope.row.status === 2 ? '已完成' : '进行中' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" min-width="160"></el-table-column>
            </el-table>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-card-header">
            <div class="chart-title">
              <div class="title-dot dot-purple"></div>
              <span>最近出库单</span>
            </div>
          </div>
          <div class="table-body">
            <el-table :data="recentOutbound" style="width: 100%" max-height="280">
              <el-table-column prop="orderNo" label="单号" width="160"></el-table-column>
              <el-table-column prop="type" label="类型" width="100">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.type === 1 ? '' : 'warning'" size="small" effect="dark">
                    {{ scope.row.type === 1 ? '销售出库' : '调拨出库' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.status === 2 ? 'success' : 'info'" size="small" effect="dark">
                    {{ scope.row.status === 2 ? '已完成' : '进行中' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" min-width="160"></el-table-column>
            </el-table>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card overview-card">
          <div class="chart-card-header">
            <div class="chart-title">
              <div class="title-dot dot-cyan"></div>
              <span>库存概览</span>
            </div>
          </div>
          <div class="overview-body">
            <div class="overview-item" v-for="(item, idx) in overviewItems" :key="idx">
              <div class="overview-icon" :style="{ background: item.gradient }">
                <i :class="item.icon"></i>
              </div>
              <div class="overview-info">
                <div class="overview-label">{{ item.label }}</div>
                <div class="overview-value">{{ item.value }}</div>
              </div>
              <div class="overview-trend" :class="item.trend > 0 ? 'trend-up' : 'trend-down'" v-if="item.trend !== undefined">
                <i :class="item.trend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import axios from 'axios'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {},
      recentInbound: [],
      recentOutbound: [],
      trendChart: null,
      warehouseChart: null,
      categoryChart: null
    }
  },
  computed: {
    statCards() {
      const s = this.stats
      return [
        { title: '商品总数', value: s.productCount || 0, icon: 'el-icon-goods', color: 'blue', gradient: 'linear-gradient(135deg, #00d4ff 0%, #0099ff 100%)' },
        { title: '库存总量', value: (s.totalQuantity || 0).toLocaleString(), icon: 'el-icon-box', color: 'green', gradient: 'linear-gradient(135deg, #00e676 0%, #00c853 100%)' },
        { title: '今日入库', value: s.todayInbound || 0, icon: 'el-icon-download', color: 'orange', gradient: 'linear-gradient(135deg, #ffab00 0%, #ff6d00 100%)' },
        { title: '今日出库', value: s.todayOutbound || 0, icon: 'el-icon-upload2', color: 'red', gradient: 'linear-gradient(135deg, #ff5252 0%, #d50000 100%)' }
      ]
    },
    overviewItems() {
      const s = this.stats
      return [
        { label: '仓库数量', value: (s.warehouseCount || 0) + ' 个', icon: 'el-icon-office-building', gradient: 'linear-gradient(135deg, #00d4ff, #0099ff)' },
        { label: '冻结库存', value: (s.totalFrozen || 0).toLocaleString(), icon: 'el-icon-lock', gradient: 'linear-gradient(135deg, #ffab00, #ff6d00)' },
        { label: '可用库存', value: ((s.totalQuantity || 0) - (s.totalFrozen || 0)).toLocaleString(), icon: 'el-icon-unlock', gradient: 'linear-gradient(135deg, #00e676, #00c853)' }
      ]
    }
  },
  mounted() {
    this.loadStats()
    this.loadTrend()
    this.loadWarehouseInventory()
    this.loadCategoryDistribution()
    this.loadRecentOrders()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.trendChart) this.trendChart.dispose()
    if (this.warehouseChart) this.warehouseChart.dispose()
    if (this.categoryChart) this.categoryChart.dispose()
  },
  methods: {
    getChartTheme() {
      const isDark = this.$store.state.theme === 'dark'
      return {
        textColor: isDark ? '#a3b1bf' : '#4a5568',
        lineColor: isDark ? 'rgba(64, 158, 255, 0.1)' : 'rgba(0, 0, 0, 0.06)',
        splitColor: isDark ? 'rgba(64, 158, 255, 0.08)' : 'rgba(0, 0, 0, 0.04)'
      }
    },
    handleResize() {
      if (this.trendChart) this.trendChart.resize()
      if (this.warehouseChart) this.warehouseChart.resize()
      if (this.categoryChart) this.categoryChart.resize()
    },
    async loadStats() {
      try {
        const res = await axios.get('/v1/dashboard/stats')
        if (res.data.code === 200) {
          this.stats = res.data.data
        }
      } catch (e) {
        console.error('加载统计数据失败', e)
      }
    },
    async loadTrend() {
      try {
        const [inboundRes, outboundRes] = await Promise.all([
          axios.get('/v1/dashboard/inbound-trend'),
          axios.get('/v1/dashboard/outbound-trend')
        ])
        const inboundData = inboundRes.data.code === 200 ? inboundRes.data.data : []
        const outboundData = outboundRes.data.code === 200 ? outboundRes.data.data : []
        const theme = this.getChartTheme()

        this.$nextTick(() => {
          this.trendChart = echarts.init(this.$refs.trendChart)
          this.trendChart.setOption({
            tooltip: {
              trigger: 'axis',
              backgroundColor: 'rgba(15, 25, 35, 0.9)',
              borderColor: 'rgba(0, 212, 255, 0.3)',
              textStyle: { color: '#e4e7ed' }
            },
            legend: {
              data: ['入库', '出库'],
              textStyle: { color: theme.textColor },
              top: 0
            },
            grid: { left: '3%', right: '4%', bottom: '3%', top: '40', containLabel: true },
            xAxis: {
              type: 'category',
              boundaryGap: false,
              data: inboundData.map(i => i.date),
              axisLine: { lineStyle: { color: theme.lineColor } },
              axisLabel: { color: theme.textColor }
            },
            yAxis: {
              type: 'value',
              splitLine: { lineStyle: { color: theme.splitColor } },
              axisLabel: { color: theme.textColor }
            },
            series: [
              {
                name: '入库',
                type: 'line',
                smooth: true,
                symbol: 'circle',
                symbolSize: 8,
                lineStyle: { width: 3, color: '#00e676' },
                itemStyle: { color: '#00e676', borderWidth: 2 },
                areaStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: 'rgba(0, 230, 118, 0.3)' },
                    { offset: 1, color: 'rgba(0, 230, 118, 0.02)' }
                  ])
                },
                data: inboundData.map(i => i.count)
              },
              {
                name: '出库',
                type: 'line',
                smooth: true,
                symbol: 'circle',
                symbolSize: 8,
                lineStyle: { width: 3, color: '#ff5252' },
                itemStyle: { color: '#ff5252', borderWidth: 2 },
                areaStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: 'rgba(255, 82, 82, 0.3)' },
                    { offset: 1, color: 'rgba(255, 82, 82, 0.02)' }
                  ])
                },
                data: outboundData.map(i => i.count)
              }
            ]
          })
        })
      } catch (e) {
        console.error('加载趋势数据失败', e)
      }
    },
    async loadWarehouseInventory() {
      try {
        const res = await axios.get('/v1/dashboard/warehouse-inventory')
        const data = res.data.code === 200 ? res.data.data : []
        const theme = this.getChartTheme()

        this.$nextTick(() => {
          this.warehouseChart = echarts.init(this.$refs.warehouseChart)
          this.warehouseChart.setOption({
            tooltip: {
              trigger: 'axis',
              axisPointer: { type: 'shadow' },
              backgroundColor: 'rgba(15, 25, 35, 0.9)',
              borderColor: 'rgba(0, 212, 255, 0.3)',
              textStyle: { color: '#e4e7ed' }
            },
            grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
            xAxis: {
              type: 'category',
              data: data.map(i => i.name),
              axisLine: { lineStyle: { color: theme.lineColor } },
              axisLabel: { color: theme.textColor, rotate: 0 }
            },
            yAxis: {
              type: 'value',
              splitLine: { lineStyle: { color: theme.splitColor } },
              axisLabel: { color: theme.textColor }
            },
            series: [{
              type: 'bar',
              data: data.map(i => i.value),
              barWidth: '40%',
              itemStyle: {
                borderRadius: [6, 6, 0, 0],
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#00d4ff' },
                  { offset: 1, color: '#0066cc' }
                ])
              },
              emphasis: {
                itemStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#00e6ff' },
                    { offset: 1, color: '#0088ff' }
                  ])
                }
              }
            }]
          })
        })
      } catch (e) {
        console.error('加载仓库库存数据失败', e)
      }
    },
    async loadCategoryDistribution() {
      try {
        const res = await axios.get('/v1/dashboard/category-distribution')
        const data = res.data.code === 200 ? res.data.data : []
        const colors = ['#00d4ff', '#00e676', '#ffab00', '#ff5252', '#7c4dff', '#18ffff', '#ff6e40', '#69f0ae']
        const theme = this.getChartTheme()

        this.$nextTick(() => {
          this.categoryChart = echarts.init(this.$refs.categoryChart)
          this.categoryChart.setOption({
            tooltip: {
              trigger: 'item',
              formatter: '{b}: {c} ({d}%)',
              backgroundColor: 'rgba(15, 25, 35, 0.9)',
              borderColor: 'rgba(0, 212, 255, 0.3)',
              textStyle: { color: '#e4e7ed' }
            },
            legend: {
              orient: 'vertical',
              left: 'left',
              top: 'center',
              textStyle: { color: theme.textColor }
            },
            series: [{
              type: 'pie',
              radius: ['42%', '72%'],
              center: ['58%', '50%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 8,
                borderColor: 'transparent',
                borderWidth: 3
              },
              label: {
                show: true,
                formatter: '{b}\n{d}%',
                color: theme.textColor
              },
              emphasis: {
                label: { show: true, fontSize: 14, fontWeight: 'bold' },
                itemStyle: { shadowBlur: 20, shadowColor: 'rgba(0, 212, 255, 0.3)' }
              },
              data: data.map((i, idx) => ({
                name: i.name,
                value: i.value,
                itemStyle: { color: colors[idx % colors.length] }
              }))
            }]
          })
        })
      } catch (e) {
        console.error('加载分类数据失败', e)
      }
    },
    async loadRecentOrders() {
      try {
        const res = await axios.get('/v1/dashboard/recent-orders')
        if (res.data.code === 200) {
          this.recentInbound = res.data.data.inbound || []
          this.recentOutbound = res.data.data.outbound || []
        }
      } catch (e) {
        console.error('加载最近订单失败', e)
      }
    }
  }
}
</script>

<style scoped>
.stat-card {
  position: relative;
  padding: 24px;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid var(--border-color);
  background: var(--bg-card);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--hover-glow);
}

.stat-card-bg {
  position: absolute;
  top: -30px;
  right: -30px;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  opacity: 0.1;
}

.stat-card-blue .stat-card-bg { background: #00d4ff; }
.stat-card-green .stat-card-bg { background: #00e676; }
.stat-card-orange .stat-card-bg { background: #ffab00; }
.stat-card-red .stat-card-bg { background: #ff5252; }

.stat-card:hover .stat-card-bg {
  transform: scale(1.5);
  opacity: 0.15;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
}

.stat-card-blue .stat-icon { background: linear-gradient(135deg, #00d4ff, #0099ff); box-shadow: 0 4px 15px rgba(0, 212, 255, 0.3); }
.stat-card-green .stat-icon { background: linear-gradient(135deg, #00e676, #00c853); box-shadow: 0 4px 15px rgba(0, 230, 118, 0.3); }
.stat-card-orange .stat-icon { background: linear-gradient(135deg, #ffab00, #ff6d00); box-shadow: 0 4px 15px rgba(255, 171, 0, 0.3); }
.stat-card-red .stat-icon { background: linear-gradient(135deg, #ff5252, #d50000); box-shadow: 0 4px 15px rgba(255, 82, 82, 0.3); }

.stat-icon i {
  font-size: 24px;
  color: #fff;
}

.stat-info {
  position: relative;
  z-index: 1;
}

.stat-title {
  color: var(--text-muted);
  font-size: 13px;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
  font-variant-numeric: tabular-nums;
}

.stat-line {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 3px;
}

.stat-card-blue .stat-line { background: linear-gradient(90deg, #00d4ff, transparent); }
.stat-card-green .stat-line { background: linear-gradient(90deg, #00e676, transparent); }
.stat-card-orange .stat-line { background: linear-gradient(90deg, #ffab00, transparent); }
.stat-card-red .stat-line { background: linear-gradient(90deg, #ff5252, transparent); }

.chart-card {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.chart-card:hover {
  box-shadow: var(--hover-glow);
}

.chart-card-header {
  padding: 18px 24px;
  border-bottom: 1px solid var(--border-color);
}

.chart-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
}

.title-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #00d4ff;
  box-shadow: 0 0 8px rgba(0, 212, 255, 0.5);
}

.light .title-dot {
  background: #3b82f6;
  box-shadow: 0 0 8px rgba(59, 130, 246, 0.5);
}

.dot-green { background: #00e676 !important; box-shadow: 0 0 8px rgba(0, 230, 118, 0.5) !important; }
.dot-orange { background: #ffab00 !important; box-shadow: 0 0 8px rgba(255, 171, 0, 0.5) !important; }
.dot-red { background: #ff5252 !important; box-shadow: 0 0 8px rgba(255, 82, 82, 0.5) !important; }
.dot-purple { background: #7c4dff !important; box-shadow: 0 0 8px rgba(124, 77, 255, 0.5) !important; }
.dot-cyan { background: #18ffff !important; box-shadow: 0 0 8px rgba(24, 255, 255, 0.5) !important; }

.light .dot-green { background: #10b981 !important; box-shadow: 0 0 8px rgba(16, 185, 129, 0.5) !important; }
.light .dot-orange { background: #f59e0b !important; box-shadow: 0 0 8px rgba(245, 158, 11, 0.5) !important; }
.light .dot-red { background: #ef4444 !important; box-shadow: 0 0 8px rgba(239, 68, 68, 0.5) !important; }
.light .dot-purple { background: #8b5cf6 !important; box-shadow: 0 0 8px rgba(139, 92, 246, 0.5) !important; }
.light .dot-cyan { background: #06b6d4 !important; box-shadow: 0 0 8px rgba(6, 182, 212, 0.5) !important; }

.chart-body {
  height: 320px;
  padding: 10px;
}

.table-body {
  padding: 16px;
}

.overview-body {
  padding: 20px;
}

.overview-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-radius: 12px;
  margin-bottom: 12px;
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid var(--border-color);
  transition: all 0.2s;
}

.light .overview-item {
  background: rgba(0, 0, 0, 0.01);
}

.overview-item:last-child {
  margin-bottom: 0;
}

.overview-item:hover {
  border-color: var(--accent-color);
  background: rgba(0, 212, 255, 0.03);
}

.light .overview-item:hover {
  background: rgba(59, 130, 246, 0.03);
}

.overview-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-right: 16px;
}

.overview-icon i {
  font-size: 20px;
  color: #fff;
}

.overview-info {
  flex: 1;
}

.overview-label {
  color: var(--text-muted);
  font-size: 13px;
  margin-bottom: 4px;
}

.overview-value {
  color: var(--text-primary);
  font-size: 20px;
  font-weight: 700;
  font-variant-numeric: tabular-nums;
}

.overview-trend {
  font-size: 14px;
  font-weight: 600;
}

.trend-up { color: #00e676; }
.trend-down { color: #ff5252; }
</style>
