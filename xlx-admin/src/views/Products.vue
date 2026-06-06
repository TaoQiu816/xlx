<template>
  <div>
    <div class="flex justify-between items-center mb-4 flex-wrap gap-3">
      <div class="flex gap-2 flex-wrap">
        <el-input
          v-model="keyword"
          placeholder="搜索产品名称"
          clearable
          class="w-full sm:w-56"
          @keyup.enter="handleSearch"
          @clear="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select v-model="filterCategory" placeholder="筛选分类" clearable @change="handleSearch" class="w-full sm:w-44">
          <el-option v-for="c in categories" :key="c.id" :label="c.nameCn" :value="c.id" />
        </el-select>
        <el-select v-model="filterStatus" placeholder="筛选状态" clearable @change="handleSearch" class="w-full sm:w-32">
          <el-option label="上架" :value="1" />
          <el-option label="下架" :value="0" />
        </el-select>
      </div>
      <el-button type="primary" @click="$router.push('/products/create')">
        <el-icon class="mr-1"><Plus /></el-icon>新增产品
      </el-button>
    </div>

    <div class="overflow-x-auto">
    <el-table :data="list" border stripe v-loading="loading">
      <template #empty>
        <el-empty description="暂无产品数据" />
      </template>
      <el-table-column label="图片" width="80">
        <template #default="{ row }">
          <el-image
            v-if="row.mainImage"
            :src="row.mainImage"
            :preview-src-list="[row.mainImage]"
            style="width: 50px; height: 50px"
            fit="cover"
            :preview-teleported="true"
          />
          <div v-else class="w-[50px] h-[50px] bg-gray-100 rounded flex items-center justify-center">
            <el-icon :size="20" class="text-gray-300"><Picture /></el-icon>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="nameCn" label="产品名称" min-width="180" show-overflow-tooltip />
      <el-table-column label="分类" width="120">
        <template #default="{ row }">
          {{ getCategoryName(row.categoryId) }}
        </template>
      </el-table-column>
      <el-table-column label="价格" width="120">
        <template #default="{ row }">
          <span v-if="row.showPrice && row.price" class="text-orange-600 font-medium">
            {{ row.price }} {{ row.priceUnit }}
          </span>
          <span v-else class="text-gray-400 text-xs">联系报价</span>
        </template>
      </el-table-column>
      <el-table-column label="标签" width="180">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small" class="mr-1">
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
          <el-tag v-if="row.isFeatured" type="warning" size="small" class="mr-1">推荐</el-tag>
          <el-tag v-if="row.supportCustom" type="primary" size="small" effect="plain">定制</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button size="small" text type="primary" @click="$router.push(`/products/${row.id}/edit`)">
            编辑
          </el-button>
          <el-popconfirm
            title="确定删除该产品？删除后不可恢复"
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

    <el-pagination
      class="mt-4 justify-end"
      v-model:current-page="page"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="[10, 20, 50]"
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
import { Search, Plus, Picture } from '@element-plus/icons-vue'
import { productApi } from '../api/product'
import { categoryApi } from '../api/category'

interface ProductItem {
  id: number
  nameCn: string
  categoryId: number
  mainImage: string
  price: string
  priceUnit: string
  showPrice: number
  status: number
  isFeatured: number
  supportCustom: number
}

const list = ref<ProductItem[]>([])
const categories = ref<{ id: number; nameCn: string }[]>([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)
const keyword = ref('')
const filterCategory = ref<number | undefined>(undefined)
const filterStatus = ref<number | undefined>(undefined)

const getCategoryName = (id: number) =>
  categories.value.find((c) => c.id === id)?.nameCn || '-'

const loadList = async () => {
  loading.value = true
  try {
    const res: any = await productApi.list({
      page: page.value,
      size: pageSize.value,
      categoryId: filterCategory.value,
      status: filterStatus.value,
      keyword: keyword.value || undefined,
    })
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch {
    ElMessage.error('加载产品列表失败')
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

const handleDelete = async (id: number) => {
  try {
    await productApi.delete(id)
    ElMessage.success('删除成功')
    loadList()
  } catch {
    ElMessage.error('删除失败，请重试')
  }
}

onMounted(async () => {
  try {
    const catRes: any = await categoryApi.list()
    categories.value = catRes.data || []
  } catch {
    ElMessage.error('加载分类失败')
  }
  loadList()
})
</script>
