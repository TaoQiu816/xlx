<template>
  <div class="dashboard">
    <!-- 欢迎区 -->
    <div class="welcome-section">
      <div>
        <h1 class="welcome-title">欢迎回来，管理员</h1>
        <p class="welcome-desc">{{ greeting }}，今天是 {{ today }}</p>
      </div>
      <div class="quick-actions">
        <el-button type="primary" @click="$router.push('/products')">
          <el-icon><Plus /></el-icon>
          新增产品
        </el-button>
        <el-button @click="$router.push('/inquiries')">
          <el-icon><ChatLineSquare /></el-icon>
          查看询盘
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :xs="12" :sm="12" :md="6" class="mb-4">
        <div class="stat-card stat-card--blue">
          <div class="stat-icon">
            <el-icon :size="24"><Goods /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.productTotal }}</div>
            <div class="stat-label">产品总数</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6" class="mb-4">
        <div class="stat-card stat-card--green">
          <div class="stat-icon">
            <el-icon :size="24"><Check /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.productOnline }}</div>
            <div class="stat-label">上架产品</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6" class="mb-4">
        <div class="stat-card stat-card--orange">
          <div class="stat-icon">
            <el-icon :size="24"><ChatLineSquare /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.inquiryTotal }}</div>
            <div class="stat-label">总询盘</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6" class="mb-4">
        <div class="stat-card stat-card--purple">
          <div class="stat-icon">
            <el-icon :size="24"><Bell /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.inquiryPending }}</div>
            <div class="stat-label">待处理询盘</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="16" class="mb-6">
      <el-col :xs="24" :md="16" class="mb-4">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">询盘趋势</span>
              <el-radio-group v-model="trendRange" size="small" @change="loadStats">
                <el-radio-button value="7">近7天</el-radio-button>
                <el-radio-button value="30">近30天</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <v-chart :option="trendOption" autoresize style="height: 300px" />
        </el-card>
      </el-col>
      <el-col :xs="24" :md="8" class="mb-4">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span class="card-title">询盘状态分布</span>
          </template>
          <v-chart :option="pieOption" autoresize style="height: 300px" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近询盘 -->
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">最新询盘</span>
          <el-button text type="primary" @click="$router.push('/inquiries')">查看全部</el-button>
        </div>
      </template>
      <div class="overflow-x-auto">
      <el-table :data="stats.latestInquiries" stripe>
        <el-table-column prop="createdAt" label="时间" width="170">
          <template #default="{ row }">{{ formatDate(row.createdAt) }}</template>
        </el-table-column>
        <el-table-column prop="name" label="客户" width="110" />
        <el-table-column prop="company" label="公司" />
        <el-table-column prop="productName" label="产品" />
        <el-table-column prop="status" label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)" size="small" round>{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, PieChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import { dashboardApi, type DashboardStats } from '../api/dashboard'
import { Goods, ChatLineSquare, Bell, Plus, Check } from '@element-plus/icons-vue'
import { inquiryStatusLabel as statusLabel, inquiryStatusType as statusType } from '../constants'

use([CanvasRenderer, LineChart, PieChart, GridComponent, TooltipComponent, LegendComponent])

const stats = ref<DashboardStats>({
  productTotal: 0,
  productOnline: 0,
  inquiryTotal: 0,
  inquiryPending: 0,
  inquiryTrend: [],
  inquiryStatus: [],
  latestInquiries: [],
})
const trendRange = ref('7')

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 6) return '夜深了'
  if (h < 9) return '早上好'
  if (h < 12) return '上午好'
  if (h < 14) return '中午好'
  if (h < 18) return '下午好'
  return '晚上好'
})

const today = computed(() => {
  return new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' })
})

const formatDate = (d: string) => {
  if (!d) return '-'
  return new Date(d).toLocaleString('zh-CN')
}

const trendOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { top: 10, right: 20, bottom: 30, left: 40 },
  xAxis: {
    type: 'category',
    data: stats.value.inquiryTrend.map(t => t.date),
    axisLine: { lineStyle: { color: '#e2e8f0' } },
    axisLabel: { color: '#64748b', fontSize: 11 },
  },
  yAxis: {
    type: 'value',
    splitLine: { lineStyle: { color: '#f1f5f9' } },
    axisLabel: { color: '#64748b', fontSize: 11 },
  },
  series: [{
    type: 'line',
    data: stats.value.inquiryTrend.map(t => t.count),
    smooth: true,
    symbol: 'circle',
    symbolSize: 6,
    lineStyle: { color: '#3b82f6', width: 2.5 },
    itemStyle: { color: '#3b82f6' },
    areaStyle: {
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: 'rgba(59,130,246,0.15)' },
          { offset: 1, color: 'rgba(59,130,246,0.01)' },
        ],
      },
    },
  }],
}))

const pieOption = computed(() => ({
  tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
  legend: { orient: 'vertical', right: 10, top: 'center', textStyle: { color: '#64748b', fontSize: 11 } },
  series: [{
    type: 'pie',
    radius: ['45%', '70%'],
    center: ['40%', '50%'],
    avoidLabelOverlap: false,
    itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
    label: { show: false },
    emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
    data: stats.value.inquiryStatus.map(s => ({ name: s.label, value: s.count })),
  }],
}))

const loadStats = async () => {
  try {
    const res: any = await dashboardApi.stats(Number(trendRange.value))
    if (res.data) stats.value = res.data
  } catch {
    // Stats will remain 0 on error
  }
}

onMounted(loadStats)
</script>

<style scoped>
.dashboard {
  max-width: 1400px;
}

.welcome-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.welcome-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 0.25rem;
}

.welcome-desc {
  color: #64748b;
  font-size: 0.9rem;
}

.quick-actions {
  display: flex;
  gap: 0.5rem;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.25rem;
  border-radius: 12px;
  background: white;
  border: 1px solid #f1f5f9;
  transition: box-shadow 0.2s, transform 0.2s;
}

.stat-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  transform: translateY(-2px);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-card--blue .stat-icon { background: rgba(59, 130, 246, 0.1); color: #3b82f6; }
.stat-card--green .stat-icon { background: rgba(34, 197, 94, 0.1); color: #22c55e; }
.stat-card--orange .stat-icon { background: rgba(249, 115, 22, 0.1); color: #f97316; }
.stat-card--purple .stat-icon { background: rgba(168, 85, 247, 0.1); color: #a855f7; }

.stat-card--blue { border-left: 3px solid #3b82f6; }
.stat-card--green { border-left: 3px solid #22c55e; }
.stat-card--orange { border-left: 3px solid #f97316; }
.stat-card--purple { border-left: 3px solid #a855f7; }

.stat-value {
  font-size: 1.75rem;
  font-weight: 700;
  color: #1e293b;
  line-height: 1.2;
}

.stat-label {
  font-size: 0.8rem;
  color: #94a3b8;
  margin-top: 0.125rem;
}

.chart-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-weight: 600;
  color: #1e293b;
}
</style>
