<template>
  <div>
    <div class="flex justify-between items-center mb-4">
      <div class="flex gap-2">
        <el-select v-model="filterCategory" placeholder="筛选分类" clearable @change="loadList" class="w-48">
          <el-option v-for="c in categories" :key="c.id" :label="c.nameCn" :value="c.id" />
        </el-select>
      </div>
      <el-button type="primary" @click="$router.push('/products/create')">新增产品</el-button>
    </div>

    <el-table :data="list" border v-loading="loading">
      <el-table-column prop="mainImage" label="图片" width="80">
        <template #default="{ row }">
          <el-image v-if="row.mainImage" :src="row.mainImage" style="width:50px;height:50px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="nameCn" label="产品名称" />
      <el-table-column label="分类" width="120">
        <template #default="{ row }">
          {{ getCategoryName(row.categoryId) }}
        </template>
      </el-table-column>
      <el-table-column label="价格" width="120">
        <template #default="{ row }">
          <span v-if="row.showPrice && row.price">{{ row.price }} {{ row.priceUnit }}</span>
          <span v-else class="text-gray-400">联系报价</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="isFeatured" label="推荐" width="70">
        <template #default="{ row }">
          <el-tag v-if="row.isFeatured" type="warning" size="small">推荐</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/products/${row.id}/edit`)">编辑</el-button>
          <el-popconfirm title="确定删除该产品？" @confirm="handleDelete(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="mt-4 justify-end"
      v-model:current-page="page"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next"
      @change="loadList"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { productApi } from '../api/product'
import { categoryApi } from '../api/category'

const list = ref<any[]>([])
const categories = ref<any[]>([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)
const filterCategory = ref<number | undefined>(undefined)

const getCategoryName = (id: number) =>
  categories.value.find((c) => c.id === id)?.nameCn || '-'

const loadList = async () => {
  loading.value = true
  try {
    const res: any = await productApi.list({
      page: page.value,
      size: pageSize.value,
      categoryId: filterCategory.value,
    })
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

const handleDelete = async (id: number) => {
  await productApi.delete(id)
  ElMessage.success('删除成功')
  loadList()
}

onMounted(async () => {
  const catRes: any = await categoryApi.list()
  categories.value = catRes.data || []
  loadList()
})
</script>
