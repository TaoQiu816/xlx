<template>
  <div>
    <!-- 筛选栏 -->
    <div class="flex gap-2 mb-4 flex-wrap items-center">
      <el-input
        v-model="keyword"
        placeholder="搜索客户/公司/邮箱/产品"
        clearable
        class="w-full sm:w-60"
        @keyup.enter="handleSearch"
        @clear="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-select v-model="filterStatus" placeholder="筛选状态" clearable @change="handleSearch" class="w-full sm:w-36">
        <el-option v-for="(v, k) in statusMap" :key="k" :label="v.label" :value="k" />
      </el-select>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="YYYY-MM-DD"
        @change="handleSearch"
        class="!w-full sm:!w-64"
      />
    </div>

    <!-- 询盘列表 -->
    <div class="overflow-x-auto">
    <el-table :data="list" border stripe v-loading="loading" :row-class-name="rowClassName">
      <template #empty>
        <el-empty description="暂无询盘数据" />
      </template>
      <el-table-column prop="createdAt" label="提交时间" width="170">
        <template #default="{ row }">{{ formatDate(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column prop="name" label="客户" width="100" show-overflow-tooltip />
      <el-table-column prop="company" label="公司" min-width="140" show-overflow-tooltip />
      <el-table-column prop="email" label="邮箱" min-width="160" show-overflow-tooltip />
      <el-table-column prop="productName" label="产品" min-width="120" show-overflow-tooltip />
      <el-table-column prop="country" label="国家" width="90" />
      <el-table-column label="状态" width="140">
        <template #default="{ row }">
          <el-select
            v-model="row.status"
            size="small"
            :class="statusClass(row.status)"
            @change="(val: string) => handleStatusChange(row.id, val)"
          >
            <el-option v-for="(v, k) in statusMap" :key="k" :label="v.label" :value="k" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="附件" width="70" align="center">
        <template #default="{ row }">
          <a
            v-if="row.fileUrl"
            :href="row.fileUrl"
            target="_blank"
            download
            class="text-blue-500 hover:text-blue-700"
          >
            <el-icon :size="16"><Download /></el-icon>
          </a>
          <span v-else class="text-gray-300">-</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="130" fixed="right">
        <template #default="{ row }">
          <el-button size="small" text type="primary" @click="$router.push(`/inquiries/${row.id}`)">
            详情
          </el-button>
          <el-popconfirm
            title="确定删除该询盘？"
            confirm-button-text="删除"
            cancel-button-text="取消"
            confirm-button-type="danger"
            @confirm="handleDelete(row.id)"
          >
            <template #reference>
              <el-button size="small" text type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    </div>

    <!-- 分页 -->
    <el-pagination
      class="mt-4 justify-end"
      v-model:current-page="page"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next"
      background
      @current-change="loadList"
      @size-change="handleSizeChange"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Download } from '@element-plus/icons-vue'
import { inquiryApi } from '../api/inquiry'
import { inquiryStatusMap } from '../constants'

interface InquiryItem {
  id: number
  name: string
  company: string
  email: string
  phone: string
  productName: string
  country: string
  status: string
  fileUrl: string
  createdAt: string
}

const statusMap = inquiryStatusMap

const list = ref<InquiryItem[]>([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)
const keyword = ref('')
const filterStatus = ref('')
const dateRange = ref<[string, string] | null>(null)

const formatDate = (d: string) => d ? new Date(d).toLocaleString('zh-CN') : '-'

const rowClassName = ({ row }: { row: InquiryItem }) => {
  return row.status === 'new' ? 'inquiry-row--new' : ''
}

const statusClass = (status: string) => {
  if (status === 'new') return 'status-select--danger'
  if (status === 'contacted') return 'status-select--warning'
  if (status === 'quoted' || status === 'order_confirmed') return 'status-select--success'
  return ''
}

const loadList = async () => {
  loading.value = true
  try {
    const res: any = await inquiryApi.list({
      page: page.value,
      size: pageSize.value,
      status: filterStatus.value || undefined,
      keyword: keyword.value || undefined,
      startDate: dateRange.value?.[0] || undefined,
      endDate: dateRange.value?.[1] || undefined,
    })
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch {
    ElMessage.error('加载询盘列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  page.value = 1
  loadList()
}

const handleSizeChange = () => {
  page.value = 1
  loadList()
}

const handleStatusChange = async (id: number, newStatus: string) => {
  try {
    await inquiryApi.updateStatus(id, newStatus)
    ElMessage.success('状态已更新')
  } catch {
    ElMessage.error('状态更新失败')
    loadList()
  }
}

const handleDelete = async (id: number) => {
  try {
    await inquiryApi.delete(id)
    ElMessage.success('删除成功')
    loadList()
  } catch {
    ElMessage.error('删除失败')
  }
}

onMounted(loadList)
</script>

<style scoped>
.inquiry-row--new {
  background-color: #fef9ee !important;
}

.inquiry-row--new:hover > td {
  background-color: #fdf3d7 !important;
}

:deep(.status-select--danger .el-input__wrapper) {
  box-shadow: 0 0 0 1px #f56c6c inset;
}

:deep(.status-select--warning .el-input__wrapper) {
  box-shadow: 0 0 0 1px #e6a23c inset;
}

:deep(.status-select--success .el-input__wrapper) {
  box-shadow: 0 0 0 1px #67c23a inset;
}
</style>
