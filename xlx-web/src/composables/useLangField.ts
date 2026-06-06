import { getLocale } from '../i18n'

/**
 * Returns the appropriate field value based on current locale.
 * Usage: langField(item, 'title') → item.titleEn or item.titleCn
 */
export function langField<T extends Record<string, unknown>>(obj: T, base: string): string {
  const locale = getLocale()
  const key = locale === 'en' ? `${base}En` : `${base}Cn`
  const val = obj[key]
  if (typeof val === 'string' && val) return val
  const fallback = obj[`${base}Cn`]
  return typeof fallback === 'string' ? fallback : ''
}
