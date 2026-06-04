<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>产品数量</template>
          <div class="text-3xl font-bold text-blue-600">{{ stats.productCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>总询盘</template>
          <div class="text-3xl font-bold text-green-600">{{ stats.inquiryCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>新询盘</template>
          <div class="text-3xl font-bold text-orange-500">{{ stats.newInquiryCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>分类数量</template>
          <div class="text-3xl font-bold text-purple-600">{{ stats.categoryCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="mt-6">
      <template #header>
        <span class="font-bold">最新询盘</span>
      </template>
      <el-table :data="recentInquiries" stripe>
        <el-table-column prop="created_at" label="时间" width="180">
          <template #default="{ row }">{{ formatDate(row.created_at) }}</template>
        </el-table-column>
        <el-table-column prop="name" label="客户" width="120" />
        <el-table-column prop="company" label="公司" />
        <el-table-column prop="product_name" label="产品" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { productApi } from '../api/product'
import { inquiryApi } from '../api/inquiry'
import { categoryApi } from '../api/category'

const stats = ref({ productCount: 0, inquiryCount: 0, newInquiryCount: 0, categoryCount: 0 })
const recentInquiries = ref<any[]>([])

const statusMap: Record<string, { label: string; type: string }> = {
  new: { label: '新询盘', type: 'danger' },
  contacted: { label: '已联系', type: 'warning' },
  quoted: { label: '已报价', type: 'success' },
  sample_confirmed: { label: '样品沟通中', type: '' },
  order_confirmed: { label: '订单确认', type: 'success' },
  closed: { label: '已关闭', type: 'info' },
  invalid: { label: '无效询盘', type: 'info' },
}

const statusLabel = (s: string) => statusMap[s]?.label || s
const statusType = (s: string) => (statusMap[s]?.type || '') as any

const formatDate = (d: string) => {
  if (!d) return '-'
  return new Date(d).toLocaleString('zh-CN')
}

onMounted(async () => {
  try {
    const [prodRes, inqRes, catRes, newInqRes]: any[] = await Promise.all([
      productApi.list({ page: 1, size: 1 }),
      inquiryApi.list({ page: 1, size: 1 }),
      categoryApi.list(),
      inquiryApi.list({ page: 1, size: 1, status: 'new' }),
    ])
    stats.value.productCount = prodRes.data?.total || 0
    stats.value.inquiryCount = inqRes.data?.total || 0
    stats.value.categoryCount = (catRes.data || []).length
    stats.value.newInquiryCount = newInqRes.data?.total || 0

    const recentRes: any = await inquiryApi.list({ page: 1, size: 5 })
    recentInquiries.value = recentRes.data?.records || []
  } catch {
    // Stats will remain 0 on error
  }
})
</script>
