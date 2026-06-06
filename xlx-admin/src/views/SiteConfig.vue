<template>
  <div>
    <el-card v-loading="loading">
      <el-form label-width="120px" class="site-config-form">

        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        <el-form-item label="网站名称">
          <el-input v-model="configs.site_name" />
        </el-form-item>
        <el-form-item label="网站名称(EN)">
          <el-input v-model="configs.site_name_en" placeholder="English site name" />
        </el-form-item>
        <el-form-item label="公司名称">
          <el-input v-model="configs.company_name" />
        </el-form-item>
        <el-form-item label="公司名称(EN)">
          <el-input v-model="configs.company_name_en" />
        </el-form-item>

        <!-- 联系方式 -->
        <el-divider content-position="left">联系方式</el-divider>
        <el-row :gutter="16">
          <el-col :xs="24" :sm="12">
            <el-form-item label="联系电话">
              <el-input v-model="configs.phone" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="联系电话(EN)">
              <el-input v-model="configs.phone_en" placeholder="English phone" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :xs="24" :sm="12">
            <el-form-item label="邮箱">
              <el-input v-model="configs.email" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="邮箱(EN)">
              <el-input v-model="configs.email_en" placeholder="English email" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="微信">
          <el-input v-model="configs.wechat" />
        </el-form-item>
        <el-form-item label="WhatsApp">
          <el-input v-model="configs.whatsapp" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :xs="24" :sm="12">
            <el-form-item label="地址">
              <el-input v-model="configs.address" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="地址(EN)">
              <el-input v-model="configs.address_en" placeholder="English address" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 首页 Banner -->
        <el-divider content-position="left">首页 Banner</el-divider>
        <el-form-item label="Banner 标题">
          <el-input v-model="configs.banner_title" />
        </el-form-item>
        <el-form-item label="Banner 标题(EN)">
          <el-input v-model="configs.banner_title_en" placeholder="English banner title" />
        </el-form-item>
        <el-form-item label="Banner 副标题">
          <el-input v-model="configs.banner_subtitle" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="Banner 副标题(EN)">
          <el-input v-model="configs.banner_subtitle_en" type="textarea" :rows="2" placeholder="English banner subtitle" />
        </el-form-item>

        <!-- Hero 统计数字 -->
        <el-divider content-position="left">Hero 统计数字</el-divider>
        <template v-for="i in 4" :key="'stat-'+i">
          <el-row :gutter="16">
            <el-col :xs="24" :sm="8">
              <el-form-item :label="`统计${i} 数字`">
                <el-input v-model="configs[`hero_stat${i}_value`]" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="8">
              <el-form-item :label="`统计${i} 数字(EN)`">
                <el-input v-model="configs[`hero_stat${i}_value_en`]" :placeholder="`English stat ${i} value`" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="16">
            <el-col :xs="24" :sm="8">
              <el-form-item :label="`统计${i} 标签`">
                <el-input v-model="configs[`hero_stat${i}_label`]" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="8">
              <el-form-item :label="`统计${i} 标签(EN)`">
                <el-input v-model="configs[`hero_stat${i}_label_en`]" :placeholder="`English stat ${i} label`" />
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <!-- 首页卖点区块 -->
        <el-divider content-position="left">首页卖点区块</el-divider>
        <template v-for="i in 4" :key="'sp-'+i">
          <el-row :gutter="16">
            <el-col :xs="24" :sm="8">
              <el-form-item :label="`卖点${i} 数字`">
                <el-input v-model="configs[`sp${i}_num`]" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="8">
              <el-form-item :label="`卖点${i} 数字(EN)`">
                <el-input v-model="configs[`sp${i}_num_en`]" :placeholder="`EN number`" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="16">
            <el-col :xs="24" :sm="8">
              <el-form-item :label="`卖点${i} 标题`">
                <el-input v-model="configs[`sp${i}_title`]" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="8">
              <el-form-item :label="`卖点${i} 标题(EN)`">
                <el-input v-model="configs[`sp${i}_title_en`]" placeholder="EN title" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="16">
            <el-col :xs="24" :sm="8">
              <el-form-item :label="`卖点${i} 描述`">
                <el-input v-model="configs[`sp${i}_desc`]" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="8">
              <el-form-item :label="`卖点${i} 描述(EN)`">
                <el-input v-model="configs[`sp${i}_desc_en`]" placeholder="EN description" />
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <!-- 首页 FAQ -->
        <el-divider content-position="left">首页 FAQ</el-divider>
        <p class="config-hint">FAQ 内容同时包含中英文。每条 FAQ 包含 q（中文问题）、a（中文回答）、qEn（英文问题）、aEn（英文回答）。</p>
        <div v-for="(item, idx) in homeFaqList" :key="idx" class="faq-edit-item">
          <div class="faq-edit-fields">
            <el-input v-model="item.q" placeholder="中文问题" />
            <el-input v-model="item.qEn" placeholder="English question" />
            <el-input v-model="item.a" type="textarea" :rows="2" placeholder="中文回答" />
            <el-input v-model="item.aEn" type="textarea" :rows="2" placeholder="English answer" />
          </div>
          <el-button type="danger" text @click="removeHomeFaq(idx)">
            <el-icon><Delete /></el-icon>
          </el-button>
        </div>
        <el-button type="primary" text @click="addHomeFaq">
          <el-icon><Plus /></el-icon> 添加一条 FAQ
        </el-button>

        <!-- FAQ 页面分组 -->
        <el-divider content-position="left">FAQ 页面（分组）</el-divider>
        <p class="config-hint">每个分组包含 title（中文标题）、titleEn（英文标题），每条 FAQ 包含 q/a/qEn/aEn。</p>
        <div v-for="(group, gi) in faqGroupList" :key="gi" class="faq-group-edit">
          <div class="faq-group-header">
            <el-input v-model="group.title" placeholder="中文分组标题" class="faq-group-title-input" />
            <el-input v-model="group.titleEn" placeholder="English group title" class="faq-group-title-input" />
            <el-button type="danger" text @click="removeFaqGroup(gi)">
              <el-icon><Delete /></el-icon> 删除分组
            </el-button>
          </div>
          <div v-for="(item, ii) in group.items" :key="ii" class="faq-edit-item faq-edit-item--nested">
            <div class="faq-edit-fields">
              <el-input v-model="item.q" placeholder="中文问题" />
              <el-input v-model="item.qEn" placeholder="English question" />
              <el-input v-model="item.a" type="textarea" :rows="2" placeholder="中文回答" />
              <el-input v-model="item.aEn" type="textarea" :rows="2" placeholder="English answer" />
            </div>
            <el-button type="danger" text @click="removeFaqGroupItem(gi, ii)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </div>
          <el-button type="primary" text @click="addFaqGroupItem(gi)">
            <el-icon><Plus /></el-icon> 添加一条
          </el-button>
        </div>
        <el-button type="primary" plain @click="addFaqGroup">
          <el-icon><Plus /></el-icon> 添加分组
        </el-button>

        <!-- SEO 配置 -->
        <el-divider content-position="left">SEO 配置</el-divider>
        <el-form-item label="SEO 标题">
          <el-input v-model="configs.seo_title" placeholder="建议60字符以内" />
        </el-form-item>
        <el-form-item label="SEO 标题(EN)">
          <el-input v-model="configs.seo_title_en" placeholder="Recommended within 60 chars" />
        </el-form-item>
        <el-form-item label="SEO 描述">
          <el-input v-model="configs.seo_description" type="textarea" :rows="3" placeholder="建议150-160字符" />
        </el-form-item>
        <el-form-item label="SEO 描述(EN)">
          <el-input v-model="configs.seo_description_en" type="textarea" :rows="3" placeholder="Recommended 150-160 chars" />
        </el-form-item>
        <el-form-item label="SEO 关键词">
          <el-input v-model="configs.seo_keywords" placeholder="多个关键词用逗号分隔" />
        </el-form-item>
        <el-form-item label="SEO 关键词(EN)">
          <el-input v-model="configs.seo_keywords_en" placeholder="Multiple keywords separated by commas" />
        </el-form-item>
        <el-form-item label="OG 分享图片">
          <el-input v-model="configs.seo_og_image" placeholder="如 /logo.png 或完整URL" />
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
import { Plus, Delete } from '@element-plus/icons-vue'
import { siteConfigApi } from '../api/siteConfig'

interface FaqItem { q: string; a: string; qEn: string; aEn: string }
interface FaqGroup { title: string; titleEn: string; items: FaqItem[] }

const loading = ref(false)
const saving = ref(false)
const configs = ref<Record<string, string>>({})

const homeFaqList = ref<FaqItem[]>([])
const faqGroupList = ref<FaqGroup[]>([])

const defaultHomeFaqs: FaqItem[] = [
  { q: '支持哪些付款方式？', a: '我们支持T/T、L/C、Western Union等多种国际付款方式，具体可根据订单金额协商。', qEn: 'What payment methods do you accept?', aEn: 'We accept T/T, L/C, Western Union and other international payment methods. Specific terms can be negotiated based on order amount.' },
  { q: '最小起订量是多少？', a: '不同产品起订量不同，常规产品一般100公斤起订，具体请联系我们获取报价。', qEn: 'What is the minimum order quantity?', aEn: 'MOQ varies by product. Standard products typically have a 100kg minimum. Please contact us for a quote.' },
  { q: '交货周期多长？', a: '常规产品7-15个工作日，定制产品15-30个工作日，具体视订单数量和规格而定。', qEn: 'What is the delivery lead time?', aEn: 'Standard products: 7-15 business days. Custom products: 15-30 business days, depending on order quantity and specifications.' },
]

const defaultFaqGroups: FaqGroup[] = [
  {
    title: '产品相关', titleEn: 'Products',
    items: [
      { q: '你们主要生产哪些产品？', a: '我们主要生产两大类产品：金属丝类和丝网类。', qEn: 'What products do you mainly produce?', aEn: 'We mainly produce two categories: metal wire and wire mesh products.' },
      { q: '产品可以定制吗？', a: '可以。我们支持来图来样定制加工。', qEn: 'Can products be customized?', aEn: 'Yes. We support custom processing based on your drawings or samples.' },
    ],
  },
]

function parseJsonArray(val: string | undefined): any[] {
  if (!val) return []
  try {
    const parsed = JSON.parse(val)
    return Array.isArray(parsed) ? parsed : []
  } catch { return [] }
}

onMounted(async () => {
  loading.value = true
  try {
    const res: any = await siteConfigApi.list()
    const map: Record<string, string> = {}
    for (const item of res.data || []) {
      map[item.configKey] = item.configValue || ''
    }
    configs.value = map

    // 解析首页 FAQ，兼容旧结构（无 qEn/aEn）
    const parsedHome = parseJsonArray(map.home_faq_items)
    homeFaqList.value = parsedHome.length > 0
      ? parsedHome.map((item: any) => ({
          q: item.q || item.qCn || '',
          a: item.a || item.aCn || '',
          qEn: item.qEn || '',
          aEn: item.aEn || '',
        }))
      : [...defaultHomeFaqs]

    // 解析 FAQ 页面分组，兼容旧结构
    const parsedGroups = parseJsonArray(map.faq_page_groups)
    faqGroupList.value = parsedGroups.length > 0
      ? parsedGroups.map((g: any) => ({
          title: g.title || g.titleCn || '',
          titleEn: g.titleEn || '',
          items: (g.items || []).map((item: any) => ({
            q: item.q || item.qCn || '',
            a: item.a || item.aCn || '',
            qEn: item.qEn || '',
            aEn: item.aEn || '',
          })),
        }))
      : defaultFaqGroups.map(g => ({
          title: g.title,
          titleEn: g.titleEn,
          items: g.items.map(item => ({ ...item })),
        }))
  } finally { loading.value = false }
})

function addHomeFaq() { homeFaqList.value.push({ q: '', a: '', qEn: '', aEn: '' }) }
function removeHomeFaq(idx: number) { homeFaqList.value.splice(idx, 1) }

function addFaqGroup() { faqGroupList.value.push({ title: '', titleEn: '', items: [{ q: '', a: '', qEn: '', aEn: '' }] }) }
function removeFaqGroup(idx: number) { faqGroupList.value.splice(idx, 1) }
function addFaqGroupItem(groupIdx: number) { faqGroupList.value[groupIdx].items.push({ q: '', a: '', qEn: '', aEn: '' }) }
function removeFaqGroupItem(groupIdx: number, itemIdx: number) { faqGroupList.value[groupIdx].items.splice(itemIdx, 1) }

const handleSave = async () => {
  saving.value = true
  try {
    const toSave = { ...configs.value }
    toSave.home_faq_items = JSON.stringify(homeFaqList.value)
    toSave.faq_page_groups = JSON.stringify(faqGroupList.value)
    await siteConfigApi.update(toSave)
    ElMessage.success('保存成功')
  } finally { saving.value = false }
}
</script>

<style scoped>
.config-hint {
  font-size: 12px;
  color: #909399;
  margin: 0 0 12px 0;
  line-height: 1.6;
}

.faq-edit-item {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  margin-bottom: 12px;
}

.faq-edit-item--nested {
  margin-left: 24px;
}

.faq-edit-fields {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.faq-group-edit {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
}

.faq-group-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.faq-group-title-input {
  flex: 1;
  min-width: 200px;
}

@media (max-width: 640px) {
  .site-config-form :deep(.el-form-item__label) {
    float: none !important;
    display: block !important;
    text-align: left !important;
    padding-bottom: 4px !important;
    width: auto !important;
  }

  .site-config-form :deep(.el-form-item__content) {
    margin-left: 0 !important;
  }
}
</style>
