import { createI18n } from 'vue-i18n'
import zh from './locales/zh'
import en from './locales/en'

const STORAGE_KEY = 'xlx_lang'

function getSavedLocale(): string {
  try {
    const saved = localStorage.getItem(STORAGE_KEY)
    if (saved === 'zh' || saved === 'en') return saved
  } catch { /* ignore */ }
  return 'zh'
}

const i18n = createI18n({
  legacy: false,
  locale: getSavedLocale(),
  fallbackLocale: 'zh',
  messages: { zh, en },
})

export function setLocale(lang: 'zh' | 'en') {
  const i = i18n.global
  i.locale.value = lang
  try { localStorage.setItem(STORAGE_KEY, lang) } catch { /* ignore */ }
}

export function getLocale(): 'zh' | 'en' {
  return (i18n.global.locale.value as 'zh' | 'en') || 'zh'
}

export default i18n
