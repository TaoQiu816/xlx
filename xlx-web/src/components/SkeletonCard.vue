<script setup lang="ts">
defineProps<{
  image?: boolean
  lines?: number
  imageClass?: string
}>()
</script>

<template>
  <div class="bg-white rounded-lg border border-steel-100 overflow-hidden">
    <div v-if="image" class="aspect-square bg-steel-100 animate-pulse" :class="imageClass"></div>
    <div class="p-4 space-y-2">
      <div v-for="n in (lines || 2)" :key="n"
        class="bg-steel-200 rounded animate-pulse"
        :class="n === 1 ? 'h-4 w-3/4' : 'h-3 w-1/2'">
      </div>
    </div>
  </div>
</template>
