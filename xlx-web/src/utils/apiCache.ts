import { getLocale } from '../i18n'

interface CacheEntry<T = any> {
  data: T
  timestamp: number
  ttl: number
}

const CACHE_PREFIX = 'xlx_cache_'
const inflight = new Map<string, Promise<any>>()

function buildKey(url: string, params?: Record<string, any>, lang?: string): string {
  const locale = lang || getLocale()
  const paramStr = params ? JSON.stringify(params) : ''
  return `${CACHE_PREFIX}${url}|${paramStr}|${locale}`
}

function readStorage(key: string): CacheEntry | null {
  try {
    const raw = sessionStorage.getItem(key)
    if (!raw) return null
    return JSON.parse(raw) as CacheEntry
  } catch {
    sessionStorage.removeItem(key)
    return null
  }
}

function writeStorage(key: string, entry: CacheEntry): void {
  try {
    sessionStorage.setItem(key, JSON.stringify(entry))
  } catch {
    // storage full or unavailable — silently ignore
  }
}

export function getCached<T>(url: string, params?: Record<string, any>, lang?: string): T | null {
  const key = buildKey(url, params, lang)
  const entry = readStorage(key)
  if (!entry) return null
  if (Date.now() - entry.timestamp > entry.ttl) {
    sessionStorage.removeItem(key)
    return null
  }
  return entry.data as T
}

export function setCache<T>(url: string, data: T, ttl: number, params?: Record<string, any>, lang?: string): void {
  const key = buildKey(url, params, lang)
  writeStorage(key, { data, timestamp: Date.now(), ttl })
}

export async function cachedRequest<T>(
  requestFn: () => Promise<T>,
  url: string,
  params?: Record<string, any>,
  ttl: number = 3 * 60 * 1000,
): Promise<T> {
  const locale = getLocale()
  const cacheKey = buildKey(url, params, locale)

  const cached = getCached<T>(url, params, locale)
  if (cached !== null) return cached

  if (inflight.has(cacheKey)) {
    return inflight.get(cacheKey)!
  }

  const promise = requestFn()
    .then((data) => {
      setCache(url, data, ttl, params, locale)
      inflight.delete(cacheKey)
      return data
    })
    .catch((err) => {
      inflight.delete(cacheKey)
      throw err
    })

  inflight.set(cacheKey, promise)
  return promise
}

export function clearCache(pattern?: string): void {
  const keys: string[] = []
  for (let i = 0; i < sessionStorage.length; i++) {
    const k = sessionStorage.key(i)
    if (k && k.startsWith(CACHE_PREFIX)) {
      if (!pattern || k.includes(pattern)) {
        keys.push(k)
      }
    }
  }
  keys.forEach((k) => sessionStorage.removeItem(k))
}

export function clearAllCache(): void {
  clearCache()
}
