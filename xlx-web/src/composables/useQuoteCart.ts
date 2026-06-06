import { ref, computed } from 'vue'

const STORAGE_KEY = 'xlx_quote_cart'

export interface QuoteCartItem {
  productId: number
  slug: string
  nameCn: string
  nameEn: string
  image: string
  quantity: string
  specification: string
}

const items = ref<QuoteCartItem[]>([])

function load() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    if (raw) items.value = JSON.parse(raw)
  } catch { /* ignore */ }
}

function save() {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(items.value))
  } catch { /* ignore */ }
}

export function useQuoteCart() {
  const count = computed(() => items.value.length)

  function addItem(item: QuoteCartItem) {
    const exists = items.value.find(i => i.productId === item.productId)
    if (!exists) {
      items.value.push(item)
      save()
    }
  }

  function removeItem(productId: number) {
    items.value = items.value.filter(i => i.productId !== productId)
    save()
  }

  function updateItem(productId: number, data: Partial<QuoteCartItem>) {
    const item = items.value.find(i => i.productId === productId)
    if (item) {
      Object.assign(item, data)
      save()
    }
  }

  function clear() {
    items.value = []
    save()
  }

  function hasItem(productId: number) {
    return items.value.some(i => i.productId === productId)
  }

  load()

  return { items, count, addItem, removeItem, updateItem, clear, hasItem }
}
