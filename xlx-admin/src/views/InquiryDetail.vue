<template>
  <div>
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-bold">询盘详情</h2>
      <el-button @click="$router.back()">返回</el-button>
    </div>

    <el-card v-loading="loading">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="客户姓名">{{ inquiry.name }}</el-descriptions-item>
        <el-descriptions-item label="公司名称">{{ inquiry.company || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ inquiry.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ inquiry.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="微信">{{ inquiry.wechat || '-' }}</el-descriptions-item>
        <el-descriptions-item label="WhatsApp">{{ inquiry.whatsapp || '-' }}</el-descriptions-item>
        <el-descriptions-item label="国家/地区">{{ inquiry.country || '-' }}</el-descriptions-item>
        <el-descriptions-item label="感兴趣产品">{{ inquiry.productName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="需求数量">{{ inquiry.quantity || '-' }}</el-descriptions-item>
        <el-descriptions-item label="规格要求">{{ inquiry.specification || '-' }}</el-descriptions-item>
        <el-descriptions-item label="留言内容" :span="2">{{ inquiry.message || '-' }}</el-descriptions-item>
        <el-descriptions-item label="上传文件" :span="2">
          <el-link v-if="inquiry.fileUrl" :href="inquiry.fileUrl" target="_blank" type="primary">下载附件</el-link>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ formatDate(inquiry.createdAt) }}</el-descriptions-item>
        <el-descriptions-item label="处理状态">
          <el-select v-model="inquiry.status" @change="handleStatusChange" style="width:160px">
            <el-option label="新询盘" value="new" />
            <el-option label="已联系" value="contacted" />
            <el-option label="已报价" value="quoted" />
            <el-option label="样品沟通中" value="sample_confirmed" />
            <el-option label="订单确认" value="order_confirmed" />
            <el-option label="已关闭" value="closed" />
            <el-option label="无效询盘" value="invalid" />
          </el-select>
        </el-descriptions-item>
        <el-descriptions-item label="后台备注">
          <div class="flex gap-2">
            <el-input v-model="remark" type="textarea" :rows="2" />
            <el-button type="primary" @click="handleRemarkSave">保存</el-button>
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { inquiryApi } from '../api/inquiry'

const route = useRoute()
const loading = ref(false)
const inquiry = ref<any>({})
const remark = ref('')

const formatDate = (d: string) => d ? new Date(d).toLocaleString('zh-CN') : '-'

const loadDetail = async () => {
  loading.value = true
  try {
    const res: any = await inquiryApi.detail(Number(route.params.id))
    inquiry.value = res.data || {}
    remark.value = inquiry.value.remark || ''
  } finally { loading.value = false }
}

const handleStatusChange = async (status: string) => {
  await inquiryApi.updateStatus(Number(route.params.id), status)
  ElMessage.success('状态已更新')
}

const handleRemarkSave = async () => {
  await inquiryApi.updateRemark(Number(route.params.id), remark.value)
  ElMessage.success('备注已保存')
}

onMounted(loadDetail)
</script>
