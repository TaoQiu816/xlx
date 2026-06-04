<template>
  <div>
    <el-button type="primary" class="mb-4" @click="openDialog()">新增分类</el-button>
    <el-table :data="list" border v-loading="loading">
      <el-table-column prop="nameCn" label="分类名称" />
      <el-table-column prop="nameEn" label="英文名称" />
      <el-table-column prop="slug" label="Slug" width="200" />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column prop="status" label="状态" width="80">
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
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '新增分类'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="中文名称" required>
          <el-input v-model="form.nameCn" />
        </el-form-item>
        <el-form-item label="英文名称">
          <el-input v-model="form.nameEn" />
        </el-form-item>
        <el-form-item label="Slug">
          <el-input v-model="form.slug" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="显示">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
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
import { categoryApi } from '../api/category'

const list = ref<any[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const saving = ref(false)
const isEdit = ref(false)
const editId = ref<number | null>(null)

const defaultForm = { nameCn: '', nameEn: '', slug: '', sortOrder: 0, status: 1 }
const form = reactive({ ...defaultForm })

const loadList = async () => {
  loading.value = true
  try {
    const res: any = await categoryApi.list()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

const openDialog = (row?: any) => {
  isEdit.value = !!row
  editId.value = row?.id || null
  Object.assign(form, row || defaultForm)
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.nameCn) {
    ElMessage.warning('请输入中文名称')
    return
  }
  saving.value = true
  try {
    if (isEdit.value && editId.value) {
      await categoryApi.update(editId.value, form)
    } else {
      await categoryApi.create(form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadList()
  } finally {
    saving.value = false
  }
}

const handleDelete = async (id: number) => {
  await categoryApi.delete(id)
  ElMessage.success('删除成功')
  loadList()
}

onMounted(loadList)
</script>
