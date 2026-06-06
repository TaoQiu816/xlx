import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

const defaultHosts = ['localhost', '127.0.0.1']
const envHosts = process.env.VITE_ALLOWED_HOSTS
  ? process.env.VITE_ALLOWED_HOSTS.split(',').map(h => h.trim()).filter(Boolean)
  : []
const allowedHosts = [...new Set([...defaultHosts, ...envHosts])]

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000,
    allowedHosts,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/uploads': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
