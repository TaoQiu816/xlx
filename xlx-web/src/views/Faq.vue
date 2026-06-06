<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { siteApi } from '../api/site'
import { useSEO } from '../composables/useSEO'
import { getLocale } from '../i18n'

const { t } = useI18n()
const lang = computed(() => getLocale())

useSEO({
  title: t('faq.title'),
  description: t('faq.subtitle'),
})

interface FaqItem { q: string; a: string; qEn: string; aEn: string; open: boolean }
interface FaqGroup { title: string; titleEn: string; items: FaqItem[] }

const defaultGroups: FaqGroup[] = [
  {
    title: '产品相关', titleEn: 'Products',
    items: [
      { q: '你们主要生产哪些产品？', a: '我们主要生产两大类产品：金属丝类（镀锌铁丝、不锈钢丝、退火丝、PVC涂塑丝等）和丝网类（电焊网片、荷兰网、护栏网、窗纱、钢板网、钢格板等）。', qEn: 'What products do you mainly produce?', aEn: 'We mainly produce two categories: metal wire products (galvanized wire, stainless steel wire, annealed wire, PVC coated wire, etc.) and wire mesh products (welded mesh, Holland mesh, fence mesh, window screen, steel grate, etc.).', open: false },
      { q: '产品可以定制吗？', a: '可以。我们支持来图来样定制加工，可以根据您的需求定制不同材质、规格、尺寸的产品。定制产品一般15-30个工作日交货。', qEn: 'Can products be customized?', aEn: 'Yes. We support custom processing based on your drawings or samples. We can customize products with different materials, specifications, and dimensions. Custom products typically take 15-30 business days.', open: false },
      { q: '产品的质量标准是什么？', a: '所有产品均按照国家标准生产，部分产品可按国际标准（ISO、EN等）生产。每批产品均附带质检报告和材质证书。', qEn: 'What are the quality standards?', aEn: 'All products are manufactured according to national standards. Some products can be produced according to international standards (ISO, EN, etc.). Each batch comes with quality inspection reports and material certificates.', open: false },
      { q: '可以提供样品吗？', a: '可以提供免费样品，运费到付。样品一般3-5个工作日准备好并寄出。', qEn: 'Can you provide samples?', aEn: 'Yes, free samples are available (freight collect). Samples are typically prepared and shipped within 3-5 business days.', open: false },
    ],
  },
  {
    title: '订单与付款', titleEn: 'Orders & Payment',
    items: [
      { q: '最小起订量是多少？', a: '不同产品起订量不同。常规产品一般100公斤起订，定制产品视具体情况而定。具体请联系我们获取详细信息。', qEn: 'What is the minimum order quantity?', aEn: 'MOQ varies by product. Standard products typically have a 100kg minimum. Custom products depend on specific requirements. Please contact us for details.', open: false },
      { q: '支持哪些付款方式？', a: '我们支持T/T（电汇）、L/C（信用证）、Western Union等多种国际付款方式。具体付款方式可根据订单金额和双方协商确定。', qEn: 'What payment methods do you accept?', aEn: 'We accept T/T ( Telegraphic Transfer), L/C (Letter of Credit), Western Union, and other international payment methods. Specific terms can be negotiated based on order amount.', open: false },
      { q: '交货周期多长？', a: '常规产品7-15个工作日，定制产品15-30个工作日。具体交期视产品类型、订单数量和当时产能而定，下单时会与您确认具体交期。', qEn: 'What is the delivery lead time?', aEn: 'Standard products: 7-15 business days. Custom products: 15-30 business days. Exact lead time depends on product type, order quantity, and current capacity. Delivery date will be confirmed when placing the order.', open: false },
      { q: '可以接受FOB/CIF等贸易方式吗？', a: '可以。我们支持FOB、CIF、CFR等多种国际贸易条款，可以根据您的需求灵活安排。', qEn: 'Do you accept FOB/CIF trade terms?', aEn: 'Yes. We support FOB, CIF, CFR and other international trade terms. We can arrange flexibly according to your needs.', open: false },
    ],
  },
  {
    title: '物流与售后', titleEn: 'Logistics & After-sales',
    items: [
      { q: '如何安排物流运输？', a: '国内订单可走物流或快递；出口订单可安排海运、空运或铁路运输。我们与多家货代公司有长期合作，可以为您提供优惠的运输方案。', qEn: 'How do you arrange logistics?', aEn: 'Domestic orders can be shipped via logistics or express. Export orders can be arranged by sea, air, or rail. We have long-term partnerships with multiple freight forwarders to offer competitive shipping solutions.', open: false },
      { q: '包装方式是怎样的？', a: '常规产品采用编织袋或纸箱包装，出口产品可按客户要求进行中性包装或定制包装。所有包装均经过防潮、防锈处理。', qEn: 'How are products packaged?', aEn: 'Standard products are packaged in woven bags or cartons. Export products can be neutral or custom packaged per customer requirements. All packaging is moisture-proof and rust-proof.', open: false },
      { q: '如果收到的产品有质量问题怎么办？', a: '如有质量问题，请在收到货物后7天内联系我们并提供照片或视频证据。经确认后，我们将免费补发或退款。', qEn: 'What if I receive defective products?', aEn: 'If there are quality issues, please contact us within 7 days of receipt with photo or video evidence. After confirmation, we will reship or refund at no cost.', open: false },
      { q: '是否提供售后服务？', a: '是的。我们提供完善的售后服务，包括产品使用指导、质量问题处理、返单优惠等。每位客户都有专属业务经理对接。', qEn: 'Do you provide after-sales service?', aEn: 'Yes. We provide comprehensive after-sales service, including product usage guidance, quality issue handling, and repeat order discounts. Each customer has a dedicated account manager.', open: false },
    ],
  },
]

const faqGroups = ref<FaqGroup[]>(defaultGroups)

const displayGroups = computed(() => faqGroups.value.map(g => ({
  title: lang.value === 'en' ? (g.titleEn || g.title) : g.title,
  items: g.items.map(item => ({
    q: lang.value === 'en' ? (item.qEn || item.q) : item.q,
    a: lang.value === 'en' ? (item.aEn || item.a) : item.a,
    open: item.open,
  })),
})))

onMounted(async () => {
  try {
    const res: any = await siteApi.config()
    const configMap: Record<string, string> = {}
    for (const item of (res.data || [])) {
      configMap[item.configKey] = item.configValue || ''
    }
    const raw = configMap.faq_page_groups
    if (raw) {
      const parsed = JSON.parse(raw)
      if (Array.isArray(parsed) && parsed.length > 0) {
        faqGroups.value = parsed.map((g: any) => ({
          title: g.title || g.titleCn || '',
          titleEn: g.titleEn || '',
          items: (g.items || []).map((item: any) => ({
            q: item.q || item.qCn || '',
            a: item.a || item.aCn || '',
            qEn: item.qEn || '',
            aEn: item.aEn || '',
            open: false,
          })),
        }))
      }
    }
  } catch {
    // use defaults
  }
})

const toggle = (groupIdx: number, itemIdx: number) => {
  faqGroups.value[groupIdx].items[itemIdx].open = !faqGroups.value[groupIdx].items[itemIdx].open
}
</script>

<template>
  <div>
    <!-- Page Header -->
    <section class="bg-primary-900 text-white py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl md:text-4xl font-bold mb-4">{{ t('faq.title') }}</h1>
        <p class="text-blue-200 text-lg">{{ t('faq.subtitle') }}</p>
      </div>
    </section>

    <!-- FAQ Groups -->
    <section class="py-16">
      <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
        <div v-for="(group, gi) in displayGroups" :key="gi" class="mb-10">
          <h2 class="text-xl font-bold text-steel-900 mb-4">{{ group.title }}</h2>
          <div class="space-y-3">
            <div v-for="(faq, fi) in group.items" :key="fi" class="bg-white rounded-lg border border-steel-100 overflow-hidden">
              <button @click="toggle(gi, fi)" class="w-full px-6 py-4 text-left flex items-center justify-between hover:bg-steel-50 transition-colors">
                <span class="font-medium text-steel-800 pr-4">{{ faq.q }}</span>
                <svg class="w-5 h-5 text-steel-400 flex-shrink-0 transition-transform duration-200" :class="{ 'rotate-180': faq.open }" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/></svg>
              </button>
              <div v-if="faq.open" class="px-6 pb-4 text-steel-600 text-sm leading-relaxed border-t border-steel-50 pt-4">
                {{ faq.a }}
              </div>
            </div>
          </div>
        </div>

        <!-- Contact CTA -->
        <div class="text-center mt-12 p-8 bg-steel-50 rounded-lg">
          <h3 class="text-lg font-bold text-steel-900 mb-2">{{ t('faq.noQuestion') }}</h3>
          <p class="text-steel-500 mb-4">{{ t('faq.noQuestionHint') }}</p>
          <router-link to="/contact" class="inline-flex items-center px-8 py-3 min-h-[44px] bg-primary-600 text-white font-medium rounded hover:bg-primary-700 transition-colors">
            {{ t('nav.contact') }}
          </router-link>
        </div>
      </div>
    </section>
  </div>
</template>
