<template>
  <div>
    <div class="flex gap-2 mb-4">
      <el-select v-model="filterStatus" placeholder="筛选状态" clearable @change="loadList" class="w-40">
        <el-option label="新询盘" value="new" />
        <el-option label="已联系" value="contacted" />
        <el-option label="已报价" value="quoted" />
        <el-option label="样品沟通中" value="sample_confirmed" />
        <el-option label="订单确认" value="order_confirmed" />
        <el-option label="已关闭" value="closed" />
        <el-option label="无效询盘" value="invalid" />
      </el-select>
    </div>

    <el-table :data="list" border v-loading="loading">
      <el-table-column prop="createdAt" label="提交时间" width="170">
        <template #default="{ row }">{{ formatDate(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column prop="name" label="客户姓名" width="100" />
      <el-table-column prop="company" label="公司" />
      <el-table-column prop="phone" label="电话" width="130" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="productName" label="感兴趣产品" />
      <el-table-column prop="country" label="国家/地区" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/inquiries/${row.id}`)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination class="mt-4 justify-end" v-model:current-page="page" :total="total" layout="total, prev, pager, next" @change="loadList" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { inquiryApi } from '../api/inquiry'

const list = ref<any[]>([])
const loading = ref(false)
const page = ref(1)
const total = ref(0)
const filterStatus = ref('')

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
const formatDate = (d: string) => d ? new Date(d).toLocaleString('zh-CN') : '-'

const loadList = async () => {
  loading.value = true
  try {
    const res: any = await inquiryApi.list({ page: page.value, size: 20, status: filterStatus.value || undefined })
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally { loading.value = false }
}

onMounted(loadList)
</script>
