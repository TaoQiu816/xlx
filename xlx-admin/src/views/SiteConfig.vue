<template>
  <div>
    <el-card v-loading="loading">
      <el-form label-width="120px">
        <el-divider content-position="left">基本信息</el-divider>
        <el-form-item label="网站名称">
          <el-input v-model="configs.site_name" />
        </el-form-item>
        <el-form-item label="公司名称">
          <el-input v-model="configs.company_name" />
        </el-form-item>
        <el-form-item label="公司英文名">
          <el-input v-model="configs.company_name_en" />
        </el-form-item>

        <el-divider content-position="left">联系方式</el-divider>
        <el-form-item label="联系电话">
          <el-input v-model="configs.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="configs.email" />
        </el-form-item>
        <el-form-item label="微信">
          <el-input v-model="configs.wechat" />
        </el-form-item>
        <el-form-item label="WhatsApp">
          <el-input v-model="configs.whatsapp" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="configs.address" />
        </el-form-item>

        <el-divider content-position="left">首页 Banner</el-divider>
        <el-form-item label="Banner 标题">
          <el-input v-model="configs.banner_title" />
        </el-form-item>
        <el-form-item label="Banner 副标题">
          <el-input v-model="configs.banner_subtitle" type="textarea" :rows="2" />
        </el-form-item>

        <el-divider content-position="left">SEO</el-divider>
        <el-form-item label="SEO 标题">
          <el-input v-model="configs.seo_title" />
        </el-form-item>
        <el-form-item label="SEO 描述">
          <el-input v-model="configs.seo_description" type="textarea" :rows="3" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="large" :loading="saving" @click="handleSave">保存配置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { siteConfigApi } from '../api/siteConfig'

const loading = ref(false)
const saving = ref(false)
const configs = ref<Record<string, string>>({})

onMounted(async () => {
  loading.value = true
  try {
    const res: any = await siteConfigApi.list()
    const map: Record<string, string> = {}
    for (const item of res.data || []) {
      map[item.configKey] = item.configValue || ''
    }
    configs.value = map
  } finally { loading.value = false }
})

const handleSave = async () => {
  saving.value = true
  try {
    await siteConfigApi.update(configs.value)
    ElMessage.success('保存成功')
  } finally { saving.value = false }
}
</script>
