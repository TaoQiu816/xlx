<template>
  <footer class="bg-primary-900 text-gray-300">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <!-- Company info -->
        <div>
          <h3 class="text-white font-bold text-lg mb-4">鑫连鑫丝网厂</h3>
          <p class="text-sm leading-relaxed">
            专业丝网与金属丝产品制造商，多年生产经验，支持标准规格供应与定制加工。
          </p>
        </div>

        <!-- Quick links -->
        <div>
          <h3 class="text-white font-bold text-lg mb-4">快速链接</h3>
          <div class="space-y-2">
            <router-link to="/products" class="block text-sm hover:text-white transition-colors">产品中心</router-link>
            <router-link to="/factory" class="block text-sm hover:text-white transition-colors">工厂展示</router-link>
            <router-link to="/certificates" class="block text-sm hover:text-white transition-colors">证书展示</router-link>
            <router-link to="/faq" class="block text-sm hover:text-white transition-colors">常见问题</router-link>
            <router-link to="/contact" class="block text-sm hover:text-white transition-colors">联系我们</router-link>
          </div>
        </div>

        <!-- Contact -->
        <div>
          <h3 class="text-white font-bold text-lg mb-4">联系方式</h3>
          <div class="space-y-2 text-sm">
            <p v-if="config.phone">电话：{{ config.phone }}</p>
            <p v-if="config.email">邮箱：{{ config.email }}</p>
            <p v-if="config.wechat">微信：{{ config.wechat }}</p>
            <p v-if="config.whatsapp">WhatsApp：{{ config.whatsapp }}</p>
            <p v-if="config.address">地址：{{ config.address }}</p>
          </div>
        </div>
      </div>

      <div class="border-t border-gray-700 mt-8 pt-8 text-center text-sm text-gray-500">
        <p>&copy; {{ new Date().getFullYear() }} 鑫连鑫丝网厂 All Rights Reserved.</p>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
defineProps<{
  config: Record<string, string>
}>()
</script>
