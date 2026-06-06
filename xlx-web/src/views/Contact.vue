<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { siteApi } from '../api/site'
import { useSEO } from '../composables/useSEO'

const { t } = useI18n()

useSEO({
  title: t('contact.title'),
  description: t('contact.subtitle'),
})

defineProps<{
  config?: Record<string, string>
}>()

const form = ref({
  name: '', company: '', phone: '', email: '',
  wechat: '', whatsapp: '', country: '',
  quantity: '', specification: '', message: '',
})
const file = ref<File | null>(null)
const uploadProgress = ref(0)
const uploading = ref(false)
const submitting = ref(false)
const submitted = ref(false)
const errorMessage = ref('')

const handleFileChange = (e: Event) => {
  const input = e.target as HTMLInputElement
  file.value = input.files?.[0] || null
  uploadProgress.value = 0
}

const resetForm = () => {
  form.value = {
    name: '', company: '', phone: '', email: '',
    wechat: '', whatsapp: '', country: '',
    quantity: '', specification: '', message: '',
  }
  file.value = null
  uploadProgress.value = 0
  errorMessage.value = ''
}

const submitInquiry = async () => {
  submitting.value = true
  errorMessage.value = ''
  try {
    let fileUrl = ''
    if (file.value) {
      uploading.value = true
      const uploadRes: any = await siteApi.uploadFile(file.value, (percent: number) => {
        uploadProgress.value = percent
      })
      fileUrl = uploadRes.data?.fileUrl || ''
      uploading.value = false
    }
    await siteApi.submitInquiry({
      ...form.value,
      fileUrl,
    })
    submitted.value = true
    resetForm()
  } catch (err: any) {
    errorMessage.value = err?.message || '提交失败，请稍后重试'
  } finally {
    submitting.value = false
    uploading.value = false
  }
}
</script>

<template>
  <div>
    <!-- Page Header -->
    <section class="bg-primary-900 text-white py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl md:text-4xl font-bold mb-4">{{ t('contact.title') }}</h1>
        <p class="text-blue-200 text-lg">{{ t('contact.subtitle') }}</p>
      </div>
    </section>

    <section class="py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-12">
          <!-- Contact Info -->
          <div>
            <h2 class="text-xl font-bold text-steel-900 mb-6">{{ t('contact.contactInfo') }}</h2>
            <div class="space-y-6">
              <div v-if="config?.phone" class="flex items-start gap-4">
                <div class="w-10 h-10 bg-primary-100 rounded-lg flex items-center justify-center flex-shrink-0">
                  <svg class="w-5 h-5 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/></svg>
                </div>
                <div>
                  <div class="text-sm text-steel-500">{{ t('contact.phone') }}</div>
                  <div class="text-steel-800 font-medium">{{ config.phone }}</div>
                </div>
              </div>
              <div v-if="config?.email" class="flex items-start gap-4">
                <div class="w-10 h-10 bg-primary-100 rounded-lg flex items-center justify-center flex-shrink-0">
                  <svg class="w-5 h-5 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/></svg>
                </div>
                <div>
                  <div class="text-sm text-steel-500">{{ t('contact.email') }}</div>
                  <div class="text-steel-800 font-medium">{{ config.email }}</div>
                </div>
              </div>
              <div v-if="config?.wechat" class="flex items-start gap-4">
                <div class="w-10 h-10 bg-primary-100 rounded-lg flex items-center justify-center flex-shrink-0">
                  <svg class="w-5 h-5 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/></svg>
                </div>
                <div>
                  <div class="text-sm text-steel-500">{{ t('contact.wechat') }}</div>
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
                  <div class="text-sm text-steel-500">{{ t('contact.address') }}</div>
                  <div class="text-steel-800 font-medium">{{ config.address }}</div>
                </div>
              </div>
            </div>
          </div>

          <!-- Inquiry Form -->
          <div class="lg:col-span-2">
            <h2 class="text-xl font-bold text-steel-900 mb-6">{{ t('contact.onlineInquiry') }}</h2>

            <div v-if="submitted" class="bg-green-50 border border-green-200 rounded-lg p-8 text-center">
              <svg class="w-16 h-16 text-green-500 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/></svg>
              <h3 class="text-lg font-bold text-steel-900 mb-2">{{ t('contact.submitSuccess') }}</h3>
              <p class="text-steel-600">{{ t('contact.submitSuccessHint') }}</p>
            </div>

            <form v-else @submit.prevent="submitInquiry" class="space-y-6">
              <div v-if="errorMessage" class="bg-red-50 border border-red-200 rounded-lg p-4 text-red-700 text-sm">
                {{ errorMessage }}
              </div>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">{{ t('contact.name') }} *</label>
                  <input v-model="form.name" required :placeholder="t('contact.namePlaceholder')"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">{{ t('contact.company') }}</label>
                  <input v-model="form.company" :placeholder="t('contact.companyPlaceholder')"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
              </div>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">{{ t('contact.phoneLabel') }}</label>
                  <input v-model="form.phone" :placeholder="t('contact.phonePlaceholder')"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">{{ t('contact.emailLabel') }}</label>
                  <input v-model="form.email" type="email" :placeholder="t('contact.emailPlaceholder')"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
              </div>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">{{ t('contact.wechatLabel') }}</label>
                  <input v-model="form.wechat" :placeholder="t('contact.wechatPlaceholder')"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">{{ t('contact.whatsappLabel') }}</label>
                  <input v-model="form.whatsapp" :placeholder="t('contact.whatsappPlaceholder')"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
              </div>
              <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">{{ t('contact.country') }}</label>
                  <input v-model="form.country" :placeholder="t('contact.countryPlaceholder')"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">{{ t('contact.quantity') }}</label>
                  <input v-model="form.quantity" :placeholder="t('contact.quantityPlaceholder')"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-2">{{ t('contact.spec') }}</label>
                  <input v-model="form.specification" :placeholder="t('contact.specPlaceholder')"
                    class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
                </div>
              </div>
              <div>
                <label class="block text-sm font-medium text-steel-700 mb-2">{{ t('contact.message') }}</label>
                <textarea v-model="form.message" rows="4" :placeholder="t('contact.messagePlaceholder')"
                  class="w-full px-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500"></textarea>
              </div>
              <div>
                <label class="block text-sm font-medium text-steel-700 mb-2">{{ t('contact.uploadFile') }}</label>
                <input type="file" @change="handleFileChange" accept=".pdf,.doc,.docx,.xls,.xlsx,.jpg,.jpeg,.png"
                  class="w-full text-sm text-steel-500 file:mr-4 file:py-2 file:px-4 file:rounded file:border-0 file:text-sm file:font-medium file:bg-primary-50 file:text-primary-700 hover:file:bg-primary-100" />
                <p class="text-xs text-steel-400 mt-1">{{ t('contact.uploadHint') }}</p>
                <!-- Upload Progress -->
                <div v-if="uploading" class="mt-2">
                  <div class="w-full bg-steel-100 rounded-full h-2">
                    <div class="bg-primary-500 h-2 rounded-full transition-all duration-300" :style="{ width: uploadProgress + '%' }"></div>
                  </div>
                  <p class="text-xs text-steel-500 mt-1">{{ t('contact.uploading') }} {{ uploadProgress }}%</p>
                </div>
                <p v-else-if="file" class="text-xs text-green-600 mt-1">{{ file.name }}</p>
              </div>
              <button type="submit" :disabled="submitting"
                class="w-full py-3 bg-primary-600 text-white font-semibold rounded-lg hover:bg-primary-700 transition-colors disabled:opacity-60">
                {{ submitting ? t('contact.submitting') : t('contact.submit') }}
              </button>
            </form>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
