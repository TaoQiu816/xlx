import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '../api/request'

interface UserInfo {
  username: string
  nickname: string
  role: string
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('admin_token') || '')
  const userInfo = ref<UserInfo | null>(null)

  async function login(username: string, password: string) {
    const res: any = await request.post('/auth/login', { username, password })
    token.value = res.data.token
    userInfo.value = {
      username: res.data.username,
      nickname: res.data.nickname,
      role: res.data.role,
    }
    localStorage.setItem('admin_token', res.data.token)
  }

  async function fetchProfile() {
    const res: any = await request.get('/auth/profile')
    userInfo.value = res.data
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('admin_token')
  }

  return { token, userInfo, login, fetchProfile, logout }
})
