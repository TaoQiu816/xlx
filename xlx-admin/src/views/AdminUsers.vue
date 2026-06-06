<template>
  <div>
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">管理员列表</span>
          <el-button type="primary" @click="showAdd">
            <el-icon><Plus /></el-icon>
            新增管理员
          </el-button>
        </div>
      </template>

      <el-table :data="users" v-loading="loading" stripe>
        <el-table-column prop="username" label="用户名" width="140" />
        <el-table-column prop="nickname" label="昵称" width="140" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="row.role === 'admin' ? 'danger' : ''" size="small">
              {{ row.role === 'admin' ? '管理员' : '编辑' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLoginAt" label="最后登录" width="170">
          <template #default="{ row }">{{ formatDate(row.lastLoginAt) }}</template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="170">
          <template #default="{ row }">{{ formatDate(row.createdAt) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button text type="primary" size="small" @click="showEdit(row)">编辑</el-button>
            <el-button text type="warning" size="small" @click="showChangePwd(row)">改密</el-button>
            <el-button text type="danger" size="small" @click="handleDelete(row)"
              :disabled="row.username === 'admin'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="mt-4 flex justify-end">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[20, 50]"
          layout="total, sizes, prev, pager, next"
          @current-change="loadUsers"
          @size-change="loadUsers"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑管理员' : '新增管理员'" width="420px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名" required>
          <el-input v-model="form.username" :disabled="isEdit" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item v-if="!isEdit" label="密码" required>
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码（至少6位）" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="显示昵称" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" style="width: 100%">
            <el-option label="管理员" value="admin" />
            <el-option label="编辑" value="editor" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0"
            active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 改密对话框 -->
    <el-dialog v-model="pwdDialogVisible" title="修改密码" width="400px">
      <el-form :model="pwdForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input :model-value="pwdForm.username" disabled />
        </el-form-item>
        <el-form-item label="新密码" required>
          <el-input v-model="pwdForm.password" type="password" show-password placeholder="请输入新密码（至少6位）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleChangePwd">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { adminUserApi } from '../api/adminUser'

const loading = ref(false)
const saving = ref(false)
const users = ref<any[]>([])
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)

const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref<number | null>(null)
const form = ref({ username: '', password: '', nickname: '', role: 'editor', status: 1 })

const pwdDialogVisible = ref(false)
const pwdForm = ref({ id: 0, username: '', password: '' })

const formatDate = (d: string) => d ? new Date(d).toLocaleString('zh-CN') : '-'

const loadUsers = async () => {
  loading.value = true
  try {
    const res: any = await adminUserApi.list({ page: page.value, size: pageSize.value })
    users.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally { loading.value = false }
}

onMounted(loadUsers)

const showAdd = () => {
  isEdit.value = false
  editId.value = null
  form.value = { username: '', password: '', nickname: '', role: 'editor', status: 1 }
  dialogVisible.value = true
}

const showEdit = (row: any) => {
  isEdit.value = true
  editId.value = row.id
  form.value = { username: row.username, password: '', nickname: row.nickname || '', role: row.role || 'editor', status: row.status }
  dialogVisible.value = true
}

const showChangePwd = (row: any) => {
  pwdForm.value = { id: row.id, username: row.username, password: '' }
  pwdDialogVisible.value = true
}

const handleSubmit = async () => {
  if (!isEdit.value && !form.value.username) {
    ElMessage.warning('请输入用户名')
    return
  }
  if (!isEdit.value && (!form.value.password || form.value.password.length < 6)) {
    ElMessage.warning('密码不能少于6位')
    return
  }
  saving.value = true
  try {
    if (isEdit.value && editId.value) {
      await adminUserApi.update(editId.value, {
        nickname: form.value.nickname,
        role: form.value.role,
        status: form.value.status,
      })
      ElMessage.success('修改成功')
    } else {
      await adminUserApi.create({
        username: form.value.username,
        password: form.value.password,
        nickname: form.value.nickname,
        role: form.value.role,
      })
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadUsers()
  } finally { saving.value = false }
}

const handleChangePwd = async () => {
  if (!pwdForm.value.password || pwdForm.value.password.length < 6) {
    ElMessage.warning('密码不能少于6位')
    return
  }
  saving.value = true
  try {
    await adminUserApi.changePassword(pwdForm.value.id, pwdForm.value.password)
    ElMessage.success('密码修改成功')
    pwdDialogVisible.value = false
  } finally { saving.value = false }
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(`确定删除管理员「${row.username}」？`, '确认删除', { type: 'warning' })
    await adminUserApi.delete(row.id)
    ElMessage.success('删除成功')
    loadUsers()
  } catch {}
}
</script>

<style scoped>
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
