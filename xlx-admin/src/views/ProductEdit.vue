<template>
  <div class="product-edit">
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-bold">{{ isEdit ? '编辑产品' : '新增产品' }}</h2>
      <el-button @click="$router.back()">返回</el-button>
    </div>

    <el-tabs v-model="activeTab" type="border-card" class="product-tabs">
      <!-- Tab 1: 基础信息 -->
      <el-tab-pane label="基础信息" name="basic">
        <el-form :model="form" label-width="120px" v-loading="loading" class="tab-form product-edit-form">
          <el-form-item label="中文名称" required>
            <el-input v-model="form.nameCn" placeholder="请输入产品中文名称" />
          </el-form-item>
          <el-form-item label="英文名称">
            <el-input v-model="form.nameEn" placeholder="请输入产品英文名称" />
          </el-form-item>
          <el-form-item label="分类" required>
            <el-select v-model="form.categoryId" placeholder="请选择分类" class="w-full">
              <el-option v-for="c in categories" :key="c.id" :label="c.nameCn" :value="c.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="Slug">
            <el-input v-model="form.slug" placeholder="URL 别名，如 galvanized-wire-mesh" />
          </el-form-item>
          <el-form-item label="产品主图">
            <div class="flex items-center gap-4">
              <el-image
                v-if="form.mainImage"
                :src="form.mainImage"
                style="width: 100px; height: 100px"
                fit="cover"
                :preview-src-list="[form.mainImage]"
                :preview-teleported="true"
              />
              <el-upload
                :show-file-list="false"
                :http-request="handleMainImageUpload"
                accept="image/*"
              >
                <el-button size="small">{{ form.mainImage ? '更换图片' : '上传图片' }}</el-button>
              </el-upload>
            </div>
          </el-form-item>
          <el-form-item label="中文简介">
            <el-input v-model="form.summaryCn" type="textarea" :rows="3" placeholder="产品简短描述" />
          </el-form-item>
          <el-form-item label="英文简介">
            <el-input v-model="form.summaryEn" type="textarea" :rows="3" placeholder="Brief product description" />
          </el-form-item>
          <el-form-item label="中文详情">
            <el-input v-model="form.descriptionCn" type="textarea" :rows="6" placeholder="产品详细描述，支持多行文本" />
          </el-form-item>
          <el-form-item label="英文详情">
            <el-input v-model="form.descriptionEn" type="textarea" :rows="6" placeholder="Detailed product description" />
          </el-form-item>
          <el-form-item label="状态">
            <div class="flex items-center gap-6">
              <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="上架" inactive-text="下架" />
              <el-switch v-model="form.isFeatured" :active-value="1" :inactive-value="0" active-text="推荐" inactive-text="" />
            </div>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <!-- Tab 2: 价格与销售 -->
      <el-tab-pane label="价格与销售" name="pricing">
        <el-form :model="form" label-width="120px" class="tab-form">
          <el-form-item label="价格">
            <el-input-number v-model="form.price" :precision="2" :min="0" />
          </el-form-item>
          <el-form-item label="价格单位">
            <el-input v-model="form.priceUnit" placeholder="如 元/件、元/米" class="w-64" />
          </el-form-item>
          <el-form-item label="显示价格">
            <el-switch v-model="form.showPrice" :active-value="1" :inactive-value="0" active-text="显示" inactive-text="隐藏" />
          </el-form-item>
          <el-form-item label="支持定制">
            <el-switch v-model="form.supportCustom" :active-value="1" :inactive-value="0" />
          </el-form-item>
          <el-form-item label="支持询价">
            <el-switch v-model="form.supportInquiry" :active-value="1" :inactive-value="0" />
          </el-form-item>
          <el-form-item label="最小起订量">
            <el-input v-model="form.moq" placeholder="如 100件、1吨" class="w-64" />
          </el-form-item>
          <el-form-item label="库存状态">
            <el-select v-model="form.stockStatus" class="w-48">
              <el-option label="有库存" value="in_stock" />
              <el-option label="缺货" value="out_of_stock" />
              <el-option label="预订" value="pre_order" />
            </el-select>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <!-- Tab 3: 规格参数 -->
      <el-tab-pane label="规格参数" name="specs">
        <div class="flex justify-end mb-3">
          <el-button type="primary" size="small" @click="addSpec">
            <el-icon class="mr-1"><Plus /></el-icon>添加规格
          </el-button>
        </div>
        <div class="overflow-x-auto">
        <el-table :data="specs" border size="small">
          <el-table-column label="参数名（中文）" min-width="140">
            <template #default="{ row }">
              <el-input v-model="row.specNameCn" size="small" placeholder="如 材质" />
            </template>
          </el-table-column>
          <el-table-column label="参数名（英文）" min-width="140">
            <template #default="{ row }">
              <el-input v-model="row.specNameEn" size="small" placeholder="e.g. Material" />
            </template>
          </el-table-column>
          <el-table-column label="参数值（中文）" min-width="140">
            <template #default="{ row }">
              <el-input v-model="row.specValueCn" size="small" placeholder="如 镀锌铁丝" />
            </template>
          </el-table-column>
          <el-table-column label="参数值（英文）" min-width="140">
            <template #default="{ row }">
              <el-input v-model="row.specValueEn" size="small" placeholder="e.g. Galvanized Wire" />
            </template>
          </el-table-column>
          <el-table-column label="排序" width="90">
            <template #default="{ row }">
              <el-input-number v-model="row.sortOrder" size="small" :min="0" controls-position="right" style="width: 70px" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="{ row, $index }">
              <el-button size="small" type="danger" text @click="removeSpec(row, $index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        </div>
        <div v-if="specs.length === 0" class="text-center text-gray-400 py-8">
          暂无规格参数，点击上方按钮添加
        </div>
      </el-tab-pane>

      <!-- Tab 4: 图片管理 -->
      <el-tab-pane label="图片管理" name="images">
        <div class="flex justify-end mb-3">
          <el-upload :show-file-list="false" :http-request="handleImageUpload" accept="image/*">
            <el-button type="primary" size="small" :disabled="!isEdit">
              <el-icon class="mr-1"><Plus /></el-icon>上传图片
            </el-button>
          </el-upload>
        </div>
        <div v-if="!isEdit" class="text-center text-gray-400 py-8">
          请先保存产品后再上传图片
        </div>
        <div v-else-if="images.length === 0" class="text-center text-gray-400 py-8">
          暂无产品图片
        </div>
        <div v-else class="flex flex-wrap gap-4">
          <div v-for="img in images" :key="img.id" class="relative group">
            <el-image
              :src="img.imageUrl"
              style="width: 140px; height: 140px"
              fit="cover"
              :preview-src-list="images.map((i) => i.imageUrl)"
              :preview-teleported="true"
            />
            <el-button
              class="absolute top-1 right-1 opacity-0 group-hover:opacity-100 transition-opacity"
              type="danger"
              size="small"
              circle
              @click="removeImage(img)"
            >
              <el-icon><Delete /></el-icon>
            </el-button>
          </div>
        </div>
      </el-tab-pane>

      <!-- Tab 5: 应用与包装 -->
      <el-tab-pane label="应用与包装" name="application">
        <el-form :model="form" label-width="140px" class="tab-form">
          <el-form-item label="应用领域（中文）">
            <el-input v-model="form.applicationsCn" placeholder="如 高速公路防护、养殖场围栏" />
          </el-form-item>
          <el-form-item label="应用领域（英文）">
            <el-input v-model="form.applicationsEn" placeholder="e.g. Highway protection, farm fencing" />
          </el-form-item>
          <el-form-item label="包装方式（中文）">
            <el-input v-model="form.packagingCn" placeholder="如 纸箱包装、托盘包装" />
          </el-form-item>
          <el-form-item label="包装方式（英文）">
            <el-input v-model="form.packagingEn" placeholder="e.g. Carton packaging, pallet packaging" />
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <!-- Tab 6: 扩展信息 -->
      <el-tab-pane label="扩展信息" name="extra">
        <el-form :model="form" label-width="120px" class="tab-form">
          <el-form-item label="证书说明（中文）">
            <el-input v-model="form.certificateNoteCn" placeholder="如 ISO9001 认证" />
          </el-form-item>
          <el-form-item label="证书说明（英文）">
            <el-input v-model="form.certificateNoteEn" placeholder="e.g. ISO9001 Certified" />
          </el-form-item>
          <el-form-item label="排序权重">
            <el-input-number v-model="form.sortOrder" :min="0" />
            <span class="text-gray-400 text-xs ml-2">数值越小越靠前</span>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>

    <!-- 底部操作栏 -->
    <div class="sticky bottom-0 bg-white border-t border-gray-200 px-6 py-3 mt-4 -mx-1 rounded-b-lg flex justify-end gap-3 z-10">
      <el-button @click="$router.back()">取消</el-button>
      <el-button type="primary" :loading="saving" @click="handleSave">
        {{ isEdit ? '保存修改' : '创建产品' }}
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import { productApi } from '../api/product'
import { categoryApi } from '../api/category'
import { uploadApi } from '../api/upload'

const route = useRoute()
const router = useRouter()
const isEdit = !!route.params.id
const activeTab = ref('basic')
const loading = ref(false)
const saving = ref(false)
const categories = ref<{ id: number; nameCn: string }[]>([])
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
  try {
    const catRes: any = await categoryApi.list()
    categories.value = catRes.data || []
  } catch {
    ElMessage.error('加载分类失败')
  }

  if (isEdit) {
    loading.value = true
    try {
      const res: any = await productApi.detail(Number(route.params.id))
      Object.assign(form, res.data.product)
      specs.value = res.data.specs || []
      images.value = res.data.images || []
    } catch {
      ElMessage.error('加载产品详情失败')
    } finally {
      loading.value = false
    }
  }
})

const handleMainImageUpload = async (options: any) => {
  try {
    const res: any = await uploadApi.upload(options.file, 'product')
    form.mainImage = res.data.fileUrl
    ElMessage.success('上传成功')
  } catch {
    ElMessage.error('上传失败')
  }
}

const handleImageUpload = async (options: any) => {
  if (!isEdit) {
    ElMessage.warning('请先保存产品后再上传图片')
    return
  }
  try {
    const res: any = await uploadApi.upload(options.file, 'product_image', Number(route.params.id))
    await productApi.addImage(Number(route.params.id), { imageUrl: res.data.fileUrl })
    const imgRes: any = await productApi.listImages(Number(route.params.id))
    images.value = imgRes.data || []
    ElMessage.success('上传成功')
  } catch {
    ElMessage.error('上传失败')
  }
}

const addSpec = () => {
  specs.value.push({
    id: null, specNameCn: '', specNameEn: '', specValueCn: '', specValueEn: '', sortOrder: 0,
  })
}

const removeSpec = async (row: any, index: number) => {
  if (row.id) {
    try {
      await productApi.deleteSpec(row.id)
    } catch {
      ElMessage.error('删除规格失败')
      return
    }
  }
  specs.value.splice(index, 1)
}

const removeImage = async (img: any) => {
  try {
    await productApi.deleteImage(img.id)
    images.value = images.value.filter((i) => i.id !== img.id)
    ElMessage.success('删除成功')
  } catch {
    ElMessage.error('删除图片失败')
  }
}

const handleSave = async () => {
  if (!form.nameCn || !form.categoryId) {
    ElMessage.warning('请填写中文名称和分类')
    activeTab.value = 'basic'
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

    if (productId) {
      for (const spec of specs.value) {
        if (spec.id) {
          await productApi.updateSpec(spec.id, spec)
        } else {
          await productApi.addSpec(productId, spec)
        }
      }
    }

    ElMessage.success(isEdit ? '修改已保存' : '产品已创建')
    router.push('/products')
  } catch {
    ElMessage.error('保存失败，请重试')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.product-tabs :deep(.el-tabs__content) {
  padding: 20px 16px;
  min-height: 400px;
}

.tab-form {
  max-width: 700px;
}

@media (max-width: 640px) {
  .product-edit-form :deep(.el-form-item__label) {
    float: none !important;
    display: block !important;
    text-align: left !important;
    padding-bottom: 4px !important;
    width: auto !important;
  }

  .product-edit-form :deep(.el-form-item__content) {
    margin-left: 0 !important;
  }
}
</style>
