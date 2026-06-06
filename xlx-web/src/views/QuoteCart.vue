<template>
  <div>
    <section class="bg-primary-900 text-white py-12">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl font-bold">{{ t('quoteCart.title') }}</h1>
        <p class="text-blue-200 mt-2">{{ t('quoteCart.subtitle') }}</p>
      </div>
    </section>

    <section class="py-12">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <!-- Empty state -->
        <div v-if="cartItems.length === 0 && !submitSuccess" class="text-center py-20">
          <div class="w-20 h-20 bg-steel-100 rounded-full flex items-center justify-center mx-auto mb-6">
            <svg class="w-10 h-10 text-steel-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/>
            </svg>
          </div>
          <p class="text-steel-600 text-lg font-medium mb-2">{{ t('quoteCart.empty') }}</p>
          <p class="text-steel-400 text-sm mb-6">{{ t('quoteCart.emptyHint') }}</p>
          <router-link to="/products" class="inline-flex items-center gap-2 px-6 py-3 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors font-medium">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"/></svg>
            {{ t('quoteCart.browseProducts') }}
          </router-link>
        </div>

        <!-- Cart items -->
        <div v-else>
          <div class="flex justify-between items-center mb-6">
            <h2 class="text-xl font-bold text-gray-900">{{ t('quoteCart.totalItems', { count: cartItems.length }) }}</h2>
            <button @click="handleClear" class="text-sm text-red-500 hover:text-red-700">{{ t('quoteCart.clearAll') }}</button>
          </div>

          <div v-for="item in cartItems" :key="item.productId" class="bg-white border border-gray-200 rounded-lg p-4 mb-4 flex gap-4">
            <img :src="item.image || '/placeholder.png'" :alt="item.nameCn" class="w-20 h-20 object-cover rounded flex-shrink-0" />
            <div class="flex-1 min-w-0">
              <div class="flex justify-between items-start">
                <div>
                  <h3 class="font-medium text-gray-900">{{ lang === 'en' ? (item.nameEn || item.nameCn) : item.nameCn }}</h3>
                  <p v-if="lang === 'en' && item.nameEn" class="text-sm text-gray-500">{{ item.nameCn }}</p>
                </div>
                <button @click="removeItem(item.productId)" class="text-gray-400 hover:text-red-500 flex-shrink-0">
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                  </svg>
                </button>
              </div>
              <div class="mt-3 grid grid-cols-1 sm:grid-cols-2 gap-3">
                <div>
                  <label class="block text-xs text-gray-500 mb-1">{{ t('quoteCart.quantity') }}</label>
                  <input :value="item.quantity" @input="updateItem(item.productId, { quantity: ($event.target as HTMLInputElement).value })"
                    class="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:border-primary-500" />
                </div>
                <div>
                  <label class="block text-xs text-gray-500 mb-1">{{ t('quoteCart.specification') }}</label>
                  <input :value="item.specification" @input="updateItem(item.productId, { specification: ($event.target as HTMLInputElement).value })"
                    class="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:border-primary-500" />
                </div>
              </div>
            </div>
          </div>

          <!-- Contact form -->
          <div class="bg-white border border-gray-200 rounded-lg p-6 mt-6">
            <h3 class="font-bold text-gray-900 mb-4">{{ t('quoteCart.contactInfo') }}</h3>
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm text-gray-700 mb-1">{{ t('quoteCart.name') }} *</label>
                <input v-model="contact.name" class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:border-primary-500" />
              </div>
              <div>
                <label class="block text-sm text-gray-700 mb-1">{{ t('quoteCart.company') }}</label>
                <input v-model="contact.company" class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:border-primary-500" />
              </div>
              <div>
                <label class="block text-sm text-gray-700 mb-1">{{ t('quoteCart.phone') }}</label>
                <input v-model="contact.phone" class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:border-primary-500" />
              </div>
              <div>
                <label class="block text-sm text-gray-700 mb-1">{{ t('quoteCart.email') }} *</label>
                <input v-model="contact.email" type="email" class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:border-primary-500" />
              </div>
              <div>
                <label class="block text-sm text-gray-700 mb-1">WeChat</label>
                <input v-model="contact.wechat" class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:border-primary-500" />
              </div>
              <div>
                <label class="block text-sm text-gray-700 mb-1">WhatsApp</label>
                <input v-model="contact.whatsapp" class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:border-primary-500" />
              </div>
              <div>
                <label class="block text-sm text-gray-700 mb-1">{{ t('quoteCart.country') }}</label>
                <input v-model="contact.country" class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:border-primary-500" />
              </div>
            </div>
            <div class="mt-4">
              <label class="block text-sm text-gray-700 mb-1">{{ t('quoteCart.message') }}</label>
              <textarea v-model="message" rows="3" class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:border-primary-500"></textarea>
            </div>
          </div>

          <!-- Submit -->
          <div class="mt-6 flex justify-end">
            <button @click="handleSubmit" :disabled="submitting"
              class="px-8 py-3 bg-primary-600 text-white font-medium rounded-lg hover:bg-primary-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
              {{ submitting ? t('quoteCart.submitting') : t('quoteCart.submit') }}
            </button>
          </div>

          <!-- Success message -->
          <div v-if="submitSuccess" class="mt-6 p-6 bg-green-50 border border-green-200 rounded-lg text-center">
            <div class="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-3">
              <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
              </svg>
            </div>
            <p class="text-green-700 font-medium mb-1">{{ t('quoteCart.submitSuccess') }}</p>
            <p class="text-green-600 text-sm mb-4">{{ t('quoteCart.submitSuccessHint') }}</p>
            <router-link to="/products" class="inline-flex items-center gap-2 px-5 py-2.5 bg-primary-600 text-white text-sm rounded-lg hover:bg-primary-700 transition-colors">
              {{ t('quoteCart.browseProducts') }}
            </router-link>
          </div>
          <div v-if="submitError" class="mt-4 p-4 bg-red-50 border border-red-200 rounded-lg text-red-700">
            {{ submitError }}
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useSEO } from '../composables/useSEO'
import { useQuoteCart } from '../composables/useQuoteCart'
import { getLocale } from '../i18n'

const { t } = useI18n()
const lang = computed(() => getLocale())

useSEO({
  title: t('quoteCart.title'),
  description: t('quoteCart.subtitle'),
})

const { items: cartItems, removeItem, updateItem, clear } = useQuoteCart()

const contact = ref({
  name: '',
  company: '',
  phone: '',
  email: '',
  wechat: '',
  whatsapp: '',
  country: '',
})

const message = ref('')
const submitting = ref(false)
const submitSuccess = ref(false)
const submitError = ref('')

function handleClear() {
  if (confirm(t('quoteCart.confirmClear'))) {
    clear()
  }
}

async function handleSubmit() {
  submitError.value = ''
  submitSuccess.value = false

  if (!contact.value.name.trim()) {
    submitError.value = t('quoteCart.nameRequired')
    return
  }
  if (!contact.value.email.trim()) {
    submitError.value = t('quoteCart.emailRequired')
    return
  }

  submitting.value = true
  try {
    const res = await fetch('/api/public/inquiries', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: contact.value.name,
        company: contact.value.company,
        phone: contact.value.phone,
        email: contact.value.email,
        wechat: contact.value.wechat,
        whatsapp: contact.value.whatsapp,
        country: contact.value.country,
        message: message.value,
        items: cartItems.value.map(i => ({
          productId: i.productId,
          productNameCn: i.nameCn,
          productNameEn: i.nameEn,
          quantity: i.quantity,
          specification: i.specification,
        })),
      }),
    })
    const data = await res.json()
    if (data.code === 200) {
      submitSuccess.value = true
      clear()
      contact.value = { name: '', company: '', phone: '', email: '', wechat: '', whatsapp: '', country: '' }
      message.value = ''
    } else {
      submitError.value = data.message || t('quoteCart.submitError')
    }
  } catch {
    submitError.value = t('quoteCart.submitError')
  } finally {
    submitting.value = false
  }
}
</script>
