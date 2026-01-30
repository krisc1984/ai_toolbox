<template>
  <div class="auth-page">
    <video class="auth-video" autoplay loop muted playsinline preload="metadata">
      <source src="../assets/videos/hero_neural_network.mp4" type="video/mp4">
    </video>
    <div class="auth-overlay"></div>
    
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <router-link to="/" class="logo-link">
            <span class="logo-icon">⚡</span>
            <span class="logo-text">AI工具箱</span>
          </router-link>
          <h1 class="auth-title">欢迎回来</h1>
          <p class="auth-subtitle">登录您的账户，继续探索AI的无限可能</p>
        </div>

        <form @submit.prevent="handleLogin" class="auth-form">
          <div class="form-group">
            <label for="username" class="form-label">用户名</label>
            <div class="input-wrapper">
              <span class="input-icon">👤</span>
              <input 
                id="username"
                v-model="form.username"
                type="text" 
                class="form-input" 
                placeholder="请输入用户名"
                required
              >
            </div>
          </div>

          <div class="form-group">
            <label for="password" class="form-label">密码</label>
            <div class="input-wrapper">
              <span class="input-icon">🔒</span>
              <input 
                id="password"
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'" 
                class="form-input" 
                placeholder="请输入密码"
                required
              >
              <button 
                type="button" 
                class="password-toggle"
                @click="showPassword = !showPassword"
              >
                {{ showPassword ? '👁️' : '👁️‍🗨️' }}
              </button>
            </div>
          </div>

          <div class="form-options">
            <label class="remember-me">
              <input type="checkbox" v-model="rememberMe">
              <span class="checkmark"></span>
              记住我
            </label>
            <a href="#" class="forgot-password">忘记密码?</a>
          </div>

          <div v-if="error" class="error-message">
            {{ error }}
          </div>

          <button type="submit" class="submit-btn" :disabled="loading">
            <span v-if="loading" class="loading-spinner"></span>
            <span v-else>登录</span>
          </button>
        </form>

        <div class="auth-footer">
          <p class="footer-text">
            还没有账户? 
            <router-link to="/register" class="footer-link">立即注册</router-link>
          </p>
        </div>

        <div class="divider">
          <span>或</span>
        </div>

        <div class="social-login">
          <button class="social-btn" type="button">
            <span class="social-icon">🔵</span>
            GitHub登录
          </button>
          <button class="social-btn" type="button">
            <span class="social-icon">🔴</span>
            谷歌登录
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../api'

const router = useRouter()

const form = reactive({
  username: '',
  password: ''
})

const showPassword = ref(false)
const rememberMe = ref(false)
const loading = ref(false)
const error = ref('')

const handleLogin = async () => {
  loading.value = true
  error.value = ''

  try {
    if (!form.username) {
      error.value = '请输入用户名'
      return
    }
    
    if (!form.password) {
      error.value = '请输入密码'
      return
    }
    
    // 调用后端登录接口
    const response = await authApi.login({
      username: form.username,
      password: form.password
    })
    
    console.log('登录响应:', response)
    
    if (response.code === 200 && response.data) {
      // 保存token和用户信息
      localStorage.setItem('token', response.data.token)
      localStorage.setItem('user', JSON.stringify({
        username: response.data.userInfo.username,
        role: response.data.userInfo.role,
        rememberMe: rememberMe.value
      }))
      
      // 跳转到后端管理台
      router.push('/admin')
    } else {
      error.value = response.message || '登录失败'
    }
  } catch (e: any) {
    console.error('登录错误:', e)
    if (e.response && e.response.data) {
      error.value = e.response.data.message || '登录失败'
    } else {
      error.value = '网络错误，请稍后重试'
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.auth-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 0;
}

.auth-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(26,26,26,0.95) 0%, rgba(26,26,26,0.7) 50%, rgba(26,26,26,0.85) 100%);
  z-index: 1;
}

.auth-container {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 480px;
  padding: 24px;
}

.auth-card {
  background: rgba(45, 45, 45, 0.8);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 48px 40px;
  border: 1px solid rgba(255, 107, 53, 0.2);
  box-shadow: 0 24px 64px rgba(0, 0, 0, 0.4);
  animation: fadeInUp 0.6s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.auth-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo-link {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 32px;
  text-decoration: none;
}

.logo-icon {
  font-size: 2.5rem;
  background: linear-gradient(135deg, var(--ember-orange) 0%, var(--coral-red) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.logo-text {
  font-size: 1.5rem;
  font-weight: 800;
  background: linear-gradient(135deg, var(--ember-orange) 0%, var(--coral-red) 50%, var(--gold) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.auth-title {
  font-size: 2rem;
  font-weight: 700;
  color: var(--cream-white);
  margin-bottom: 12px;
}

.auth-subtitle {
  font-size: 1rem;
  color: rgba(250, 249, 246, 0.6);
}

.auth-form {
  margin-bottom: 32px;
}

.form-group {
  margin-bottom: 24px;
}

.form-label {
  display: block;
  font-size: 0.95rem;
  font-weight: 600;
  color: rgba(250, 249, 246, 0.9);
  margin-bottom: 10px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 16px;
  font-size: 1.2rem;
  opacity: 0.6;
}

.form-input {
  width: 100%;
  padding: 16px 16px 16px 48px;
  border: 2px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  background: rgba(26, 26, 26, 0.6);
  color: var(--cream-white);
  font-size: 1rem;
  transition: all 0.3s ease;
}

.form-input::placeholder {
  color: rgba(250, 249, 246, 0.4);
}

.form-input:focus {
  outline: none;
  border-color: var(--ember-orange);
  box-shadow: 0 0 0 4px rgba(255, 107, 53, 0.1);
}

.password-toggle {
  position: absolute;
  right: 16px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
  opacity: 0.6;
  transition: opacity 0.3s;
}

.password-toggle:hover {
  opacity: 1;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 10px;
  color: rgba(250, 249, 246, 0.7);
  font-size: 0.9rem;
  cursor: pointer;
}

.remember-me input {
  width: 18px;
  height: 18px;
  accent-color: var(--ember-orange);
}

.forgot-password {
  color: var(--ember-orange);
  font-size: 0.9rem;
  text-decoration: none;
  transition: color 0.3s;
}

.forgot-password:hover {
  color: var(--coral-red);
}

.error-message {
  background: rgba(244, 67, 54, 0.1);
  border: 1px solid rgba(244, 67, 54, 0.3);
  color: #ef5350;
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 20px;
  font-size: 0.9rem;
}

.submit-btn {
  width: 100%;
  padding: 16px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(90deg, var(--ember-orange) 0%, var(--coral-red) 100%);
  color: var(--soft-white);
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(255, 107, 53, 0.4);
}

.submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: var(--soft-white);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.auth-footer {
  text-align: center;
  margin-bottom: 24px;
}

.footer-text {
  color: rgba(250, 249, 246, 0.6);
  font-size: 0.95rem;
}

.footer-link {
  color: var(--ember-orange);
  font-weight: 600;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-link:hover {
  color: var(--coral-red);
}

.divider {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: rgba(255, 255, 255, 0.1);
}

.divider span {
  padding: 0 16px;
  color: rgba(250, 249, 246, 0.4);
  font-size: 0.9rem;
}

.social-login {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.social-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 12px;
  border: 2px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  background: transparent;
  color: var(--cream-white);
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.social-btn:hover {
  border-color: rgba(255, 255, 255, 0.3);
  background: rgba(255, 255, 255, 0.05);
}

.social-icon {
  font-size: 1.2rem;
}

@media (max-width: 480px) {
  .auth-card {
    padding: 32px 24px;
  }

  .social-login {
    grid-template-columns: 1fr;
  }

  .auth-title {
    font-size: 1.6rem;
  }
}
</style>
