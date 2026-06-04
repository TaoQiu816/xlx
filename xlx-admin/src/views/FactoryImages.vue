<template>
  <div>
    <el-button type="primary" class="mb-4" @click="openDialog()">上传图片</el-button>
    <el-table :data="list" border v-loading="loading">
      <el-table-column prop="imageUrl" label="图片" width="120">
        <template #default="{ row }">
          <el-image :src="row.imageUrl" style="width:80px;height:60px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="titleCn" label="标题" />
      <el-table-column prop="descriptionCn" label="描述" />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
            {{ row.status === 1 ? '显示' : '隐藏' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination class="mt-4 justify-end" v-model:current-page="page" :total="total" layout="total, prev, pager, next" @change="loadList" />

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑图片' : '上传图片'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="图片">
          <div class="flex items-center gap-4">
            <el-image v-if="form.imageUrl" :src="form.imageUrl" style="width:120px;height:90px" fit="cover" />
            <el-upload :show-file-list="false" :http-request="handleUpload" accept="image/*">
              <el-button size="small">选择图片</el-button>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="标题"><el-input v-model="form.titleCn" /></el-form-item>
        <el-form-item label="英文标题"><el-input v-model="form.titleEn" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.descriptionCn" type="textarea" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="显示"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { factoryApi } from '../api/factory'
import { uploadApi } from '../api/upload'

const list = ref<any[]>([])
const loading = ref(false)
const page = ref(1)
const total = ref(0)
const dialogVisible = ref(false)
const saving = ref(false)
const isEdit = ref(false)
const editId = ref<number | null>(null)

const defaultForm = { titleCn: '', titleEn: '', imageUrl: '', descriptionCn: '', descriptionEn: '', sortOrder: 0, status: 1 }
const form = reactive({ ...defaultForm })

const loadList = async () => {
  loading.value = true
  try {
    const res: any = await factoryApi.list({ page: page.value, size: 20 })
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally { loading.value = false }
}

const openDialog = (row?: any) => {
  isEdit.value = !!row
  editId.value = row?.id || null
  Object.assign(form, row || defaultForm)
  dialogVisible.value = true
}

const handleUpload = async (options: any) => {
  const res: any = await uploadApi.upload(options.file, 'factory_image')
  form.imageUrl = res.data.fileUrl
  ElMessage.success('上传成功')
}

const handleSave = async () => {
  if (!form.imageUrl) { ElMessage.warning('请上传图片'); return }
  saving.value = true
  try {
    if (isEdit.value && editId.value) { await factoryApi.update(editId.value, form) }
    else { await factoryApi.create(form) }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadList()
  } finally { saving.value = false }
}

const handleDelete = async (id: number) => {
  await factoryApi.delete(id)
  ElMessage.success('删除成功')
  loadList()
}

onMounted(loadList)
</script>
