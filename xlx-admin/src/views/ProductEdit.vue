<template>
  <div>
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-bold">{{ isEdit ? '编辑产品' : '新增产品' }}</h2>
      <el-button @click="$router.back()">返回</el-button>
    </div>

    <el-form :model="form" label-width="120px" v-loading="loading">
      <!-- 基础信息 -->
      <el-card class="mb-4">
        <template #header><span class="font-bold">基础信息</span></template>
        <el-form-item label="中文名称" required>
          <el-input v-model="form.nameCn" />
        </el-form-item>
        <el-form-item label="英文名称">
          <el-input v-model="form.nameEn" />
        </el-form-item>
        <el-form-item label="分类" required>
          <el-select v-model="form.categoryId" class="w-full">
            <el-option v-for="c in categories" :key="c.id" :label="c.nameCn" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="Slug">
          <el-input v-model="form.slug" />
        </el-form-item>
        <el-form-item label="产品主图">
          <div class="flex items-center gap-4">
            <el-image v-if="form.mainImage" :src="form.mainImage" style="width:100px;height:100px" fit="cover" />
            <el-upload :show-file-list="false" :http-request="handleMainImageUpload" accept="image/*">
              <el-button size="small">上传图片</el-button>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="是否上架">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="是否推荐">
          <el-switch v-model="form.isFeatured" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-card>

      <!-- 价格与销售 -->
      <el-card class="mb-4">
        <template #header><span class="font-bold">价格与销售</span></template>
        <el-form-item label="价格">
          <el-input-number v-model="form.price" :precision="2" :min="0" />
        </el-form-item>
        <el-form-item label="价格单位">
          <el-input v-model="form.priceUnit" placeholder="如 元/件、元/米" />
        </el-form-item>
        <el-form-item label="显示价格">
          <el-switch v-model="form.showPrice" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="支持定制">
          <el-switch v-model="form.supportCustom" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="支持询价">
          <el-switch v-model="form.supportInquiry" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="最小起订量">
          <el-input v-model="form.moq" />
        </el-form-item>
        <el-form-item label="库存状态">
          <el-select v-model="form.stockStatus">
            <el-option label="有库存" value="in_stock" />
            <el-option label="缺货" value="out_of_stock" />
            <el-option label="预订" value="pre_order" />
          </el-select>
        </el-form-item>
      </el-card>

      <!-- 内容介绍 -->
      <el-card class="mb-4">
        <template #header><span class="font-bold">内容介绍</span></template>
        <el-form-item label="中文简介">
          <el-input v-model="form.summaryCn" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="英文简介">
          <el-input v-model="form.summaryEn" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="中文详情">
          <el-input v-model="form.descriptionCn" type="textarea" :rows="6" />
        </el-form-item>
        <el-form-item label="英文详情">
          <el-input v-model="form.descriptionEn" type="textarea" :rows="6" />
        </el-form-item>
      </el-card>

      <!-- 规格参数 -->
      <el-card class="mb-4">
        <template #header>
          <div class="flex justify-between items-center">
            <span class="font-bold">规格参数</span>
            <el-button size="small" type="primary" @click="addSpec">添加规格</el-button>
          </div>
        </template>
        <el-table :data="specs" border size="small">
          <el-table-column label="参数名（中文）">
            <template #default="{ row }">
              <el-input v-model="row.specNameCn" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="参数名（英文）">
            <template #default="{ row }">
              <el-input v-model="row.specNameEn" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="参数值（中文）">
            <template #default="{ row }">
              <el-input v-model="row.specValueCn" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="参数值（英文）">
            <template #default="{ row }">
              <el-input v-model="row.specValueEn" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="排序" width="80">
            <template #default="{ row }">
              <el-input-number v-model="row.sortOrder" size="small" :min="0" controls-position="right" style="width:60px" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="{ row, $index }">
              <el-button size="small" type="danger" text @click="removeSpec(row, $index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 产品图片 -->
      <el-card class="mb-4">
        <template #header>
          <div class="flex justify-between items-center">
            <span class="font-bold">产品图片</span>
            <el-upload :show-file-list="false" :http-request="handleImageUpload" accept="image/*">
              <el-button size="small" type="primary">上传图片</el-button>
            </el-upload>
          </div>
        </template>
        <div class="flex flex-wrap gap-4">
          <div v-for="img in images" :key="img.id" class="relative">
            <el-image :src="img.imageUrl" style="width:120px;height:120px" fit="cover" />
            <el-button
              class="absolute top-0 right-0"
              type="danger"
              size="small"
              circle
              @click="removeImage(img)"
            >X</el-button>
          </div>
          <div v-if="images.length === 0" class="text-gray-400 text-sm">暂无图片</div>
        </div>
      </el-card>

      <!-- 其他信息 -->
      <el-card class="mb-4">
        <template #header><span class="font-bold">其他信息</span></template>
        <el-form-item label="包装方式（中文）">
          <el-input v-model="form.packagingCn" />
        </el-form-item>
        <el-form-item label="包装方式（英文）">
          <el-input v-model="form.packagingEn" />
        </el-form-item>
        <el-form-item label="应用领域（中文）">
          <el-input v-model="form.applicationsCn" />
        </el-form-item>
        <el-form-item label="应用领域（英文）">
          <el-input v-model="form.applicationsEn" />
        </el-form-item>
        <el-form-item label="证书说明（中文）">
          <el-input v-model="form.certificateNoteCn" />
        </el-form-item>
        <el-form-item label="证书说明（英文）">
          <el-input v-model="form.certificateNoteEn" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
      </el-card>

      <div class="text-center">
        <el-button type="primary" size="large" :loading="saving" @click="handleSave">保存产品</el-button>
        <el-button size="large" @click="$router.back()">取消</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { productApi } from '../api/product'
import { categoryApi } from '../api/category'
import { uploadApi } from '../api/upload'

const route = useRoute()
const router = useRouter()
const isEdit = !!route.params.id
const loading = ref(false)
const saving = ref(false)
const categories = ref<any[]>([])
const specs = ref<any[]>([])
const images = ref<any[]>([])

const form = reactive({
  nameCn: '', nameEn: '', categoryId: null as number | null, slug: '',
  summaryCn: '', summaryEn: '', descriptionCn: '', descriptionEn: '',
  mainImage: '', price: null as number | null, priceUnit: '',
  showPrice: 0, supportCustom: 0, supportInquiry: 1, supportOrder: 0,
  moq: '', stockStatus: 'in_stock', packagingCn: '', packagingEn: '',
  applicationsCn: '', applicationsEn: '', certificateNoteCn: '', certificateNoteEn: '',
  sortOrder: 0, isFeatured: 0, status: 1,
})

onMounted(async () => {
  const catRes: any = await categoryApi.list()
  categories.value = catRes.data || []

  if (isEdit) {
    loading.value = true
    try {
      const res: any = await productApi.detail(Number(route.params.id))
      Object.assign(form, res.data.product)
      specs.value = res.data.specs || []
      images.value = res.data.images || []
    } finally {
      loading.value = false
    }
  }
})

const handleMainImageUpload = async (options: any) => {
  const res: any = await uploadApi.upload(options.file, 'product')
  form.mainImage = res.data.fileUrl
  ElMessage.success('上传成功')
}

const handleImageUpload = async (options: any) => {
  if (!isEdit) {
    ElMessage.warning('请先保存产品后再上传图片')
    return
  }
  const res: any = await uploadApi.upload(options.file, 'product_image', Number(route.params.id))
  await productApi.addImage(Number(route.params.id), { imageUrl: res.data.fileUrl })
  const imgRes: any = await productApi.listImages(Number(route.params.id))
  images.value = imgRes.data || []
  ElMessage.success('上传成功')
}

const addSpec = () => {
  specs.value.push({
    id: null, specNameCn: '', specNameEn: '', specValueCn: '', specValueEn: '', sortOrder: 0,
  })
}

const removeSpec = async (row: any, index: number) => {
  if (row.id) {
    await productApi.deleteSpec(row.id)
  }
  specs.value.splice(index, 1)
}

const removeImage = async (img: any) => {
  await productApi.deleteImage(img.id)
  images.value = images.value.filter((i) => i.id !== img.id)
  ElMessage.success('删除成功')
}

const handleSave = async () => {
  if (!form.nameCn || !form.categoryId) {
    ElMessage.warning('请填写必填字段')
    return
  }
  saving.value = true
  try {
    let productId = Number(route.params.id)
    if (isEdit) {
      await productApi.update(productId, form)
    } else {
      const res: any = await productApi.create(form)
      productId = res.data?.id || 0
    }

    // Save specs
    if (productId) {
      // Delete existing specs first, then re-create
      for (const spec of specs.value) {
        if (spec.id) {
          await productApi.updateSpec(spec.id, spec)
        } else {
          await productApi.addSpec(productId, spec)
        }
      }
    }

    ElMessage.success('保存成功')
    router.push('/products')
  } finally {
    saving.value = false
  }
}
</script>
