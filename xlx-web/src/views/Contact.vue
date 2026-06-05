<script setup lang="ts">
import { ref } from 'vue'
import { siteApi } from '../api/site'

defineProps<{
  config?: Record<string, string>
}>()

const form = ref({
  name: '', company: '', phone: '', email: '',
  wechat: '', whatsapp: '', country: '',
  quantity: '', specification: '', message: '',
})
const file = ref<File | null>(null)
const submitting = ref(false)
const submitted = ref(false)
const errorMessage = ref('')

const handleFileChange = (e: Event) => {
  const input = e.target as HTMLInputElement
  file.value = input.files?.[0] || null
}

const submitInquiry = async () => {
  submitting.value = true
  errorMessage.value = ''
  try {
    let fileUrl = ''
    if (file.value) {
      const uploadRes: any = await siteApi.uploadFile(file.value)
      fileUrl = uploadRes.data?.fileUrl || ''
    }
    await siteApi.submitInquiry({
      ...form.value,
      fileUrl,
    })
    submitted.value = true
  } catch (err: any) {
    errorMessage.value = err?.message || '提交失败，请稍后重试'
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div>
    <!-- Page Header -->
    <section class="bg-primary-900 text-white py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl md:text-4xl font-bold mb-4">联系我们</h1>
        <p class="text-blue-200 text-lg">期待与您合作，欢迎随时咨询</p>
      </div>
    </section>

    <section class="py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-12">
          <!-- Contact Info -->
          <div>
            <h2 class="text-xl font-bold text-steel-900 mb-6">联系方式</h2>
            <div class="space-y-6">
              <div v-if="config?.phone" class="flex items-start gap-4">
                <div class="w-10 h-10 bg-primary-100 rounded-lg flex items-center justify-center flex-shrink-0">
                  <svg class="w-5 h-5 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/></svg>
                </div>
                <div>
                  <div class="text-sm text-steel-500">联系电话</div>
                  <div class="text-steel-800 font-medium">{{ config.phone }}</div>
                </div>
              </div>
              <div v-if="config?.email" class="flex items-start gap-4">
                <div class="w-10 h-10 bg-primary-100 rounded-lg flex items-center justify-center flex-shrink-0">
                  <svg class="w-5 h-5 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/></svg>
                </div>
                <div>
                  <div class="text-sm text-steel-500">邮箱</div>
                  <div class="text-steel-800 font-medium">{{ config.email }}</div>
                </div>
              </div>
              <div v-if="config?.wechat" class="flex items-start gap-4">
                <div class="w-10 h-10 bg-primary-100 rounded-lg flex items-center justify-center flex-shrink-0">
                  <svg class="w-5 h-5 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/></svg>
                </div>
                <div>
                  <div class="text-sm text-steel-500">微信</div>
                  <div class="text-steel-800 font-medium">{{ config.wechat }}</div>
                </div>
              </div>
              <div v-if="config?.whatsapp" class="flex items-start gap-4">
                <div class="w-10 h-10 bg-primary-100 rounded-lg flex items-center justify-center flex-shrink-0">
                  <svg class="w-5 h-5 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/></svg>
                </div>
                <div>
                  <div class="text-sm text-steel-500">WhatsApp</div>
                  <div class="text-steel-800 font-medium">{{ config.whatsapp }}</div>
                </div>
              </div>
              <div v-if="config?.address" class="flex items-start gap-4">
                <div class="w-10 h-10 bg-primary-100 rounded-lg flex items-center justify-center flex-shrink-0">
                  <svg class="w-5 h-5 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/></svg>
                </div>
                <div>
                  <div class="text-sm text-steel-500">地址</div>
                  <div class="text-steel-800 font-medium">{{ config.address }}</div>
                </div>
              </div>
            </div>
          </div>

          <!-- Inquiry Form -->
          <div class="lg:col-span-2">
            <h2 class="text-xl font-bold text-steel-900 mb-6">在线询盘</h2>

            <div v-if="submitted" class="bg-green-50 border border-green-200 rounded-lg p-8 text-center">
              <svg class="w-16 h-16 text-green-500 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/></svg>
              <h3 class="text-lg font-bold text-steel-900 mb-2">询盘已提交成功！</h3>
              <p class="text-steel-600">感谢您的咨询，我们将在24小时内与您联系。</p>
            </div>

            <form v-else @submit.prevent="submitInquiry" class="space-y-6">
              <div v-if="errorMessage" class="bg-red-50 border border-red-200 rounded-lg p-4 text-red-700 text-sm">
                {{ errorMessage }}
              </div>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">姓名 *</label>
                  <input v-model="form.name" required placeholder="请输入您的姓名"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">公司名称</label>
                  <input v-model="form.company" placeholder="请输入公司名称"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
              </div>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">联系电话</label>
                  <input v-model="form.phone" placeholder="请输入联系电话"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">邮箱</label>
                  <input v-model="form.email" type="email" placeholder="请输入邮箱地址"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
              </div>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">微信</label>
                  <input v-model="form.wechat" placeholder="请输入微信号"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">WhatsApp</label>
                  <input v-model="form.whatsapp" placeholder="请输入WhatsApp号码"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
              </div>
              <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">国家/地区</label>
                  <input v-model="form.country" placeholder="请输入国家或地区"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">需求数量</label>
                  <input v-model="form.quantity" placeholder="如：1000公斤"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">规格要求</label>
                  <input v-model="form.specification" placeholder="如：丝径、网孔等"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
              </div>
              <div>
                <label class="block text-sm font-medium text-steel-700 mb-2">留言内容</label>
                <textarea v-model="form.message" rows="4" placeholder="请描述您的需求..."
                  class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500"></textarea>
              </div>
              <div>
                <label class="block text-sm font-medium text-steel-700 mb-2">上传附件</label>
                <input type="file" @change="handleFileChange" accept=".pdf,.doc,.docx,.xls,.xlsx,.jpg,.jpeg,.png"
                  class="w-full text-sm text-steel-500 file:mr-4 file:py-2 file:px-4 file:rounded file:border-0 file:text-sm file:font-medium file:bg-primary-50 file:text-primary-700 hover:file:bg-primary-100" />
                <p class="text-xs text-steel-400 mt-1">支持 PDF、Word、Excel、图片格式，最大 10MB</p>
              </div>
              <button type="submit" :disabled="submitting"
                class="w-full py-3 bg-primary-600 text-white font-semibold rounded-lg hover:bg-primary-700 transition-colors disabled:opacity-60">
                {{ submitting ? '提交中...' : '提交询盘' }}
              </button>
            </form>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
