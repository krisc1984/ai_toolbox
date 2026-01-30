<template>
  <div class="app">
    <nav class="navbar" :class="{ scrolled }">
      <div class="container">
        <router-link to="/" class="logo">AI工具箱</router-link>
        <ul class="nav-links">
          <li><router-link to="/">首页</router-link></li>
          <li><router-link to="/applications">应用</router-link></li>
          <li><router-link to="/knowledge">知识库</router-link></li>
          <li><router-link to="/cases">案例</router-link></li>
          <li><router-link to="/news">资讯</router-link></li>
          <li><router-link to="/chat">AI问答</router-link></li>
        </ul>
        <div class="nav-right">
          <router-link v-if="!isLoggedIn" to="/login" class="login-btn">登录</router-link>
          <div v-else class="user-menu">
            <span class="user-name">{{ username }}</span>
            <button @click="handleLogout" class="logout-btn">退出</button>
          </div>
        </div>
      </div>
    </nav>

    <main class="main-content">
      <router-view />
    </main>

    <footer>
      <div class="container">
        <p>&copy; 2026 AI工具箱. All rights reserved. | Powered by Matrix Agent</p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const scrolled = ref(false)

const isLoggedIn = computed(() => {
  return !!localStorage.getItem('token')
})

const username = computed(() => {
  const user = localStorage.getItem('user')
  if (user) {
    try {
      return JSON.parse(user).username || '用户'
    } catch {
      return '用户'
    }
  }
  return '用户'
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

const handleScroll = () => {
  scrolled.value = window.scrollY > 100
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--deep-charcoal);
  color: var(--cream-white);
}

.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  padding: 24px 48px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 100;
  backdrop-filter: blur(10px);
  background: rgba(26, 26, 26, 0.8);
  transition: all 0.3s;
}

.navbar.scrolled {
  background: rgba(26, 26, 26, 0.95);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.container {
  max-width: 1440px;
  margin: 0 auto;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 1.5rem;
  font-weight: 800;
  color: var(--ember-orange);
  text-decoration: none;
}

.nav-links {
  display: flex;
  gap: 32px;
  list-style: none;
}

.nav-links a {
  color: var(--cream-white);
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
  position: relative;
}

.nav-links a:hover {
  color: var(--ember-orange);
}

.nav-links a::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 0;
  height: 2px;
  background: var(--ember-orange);
  transition: width 0.3s;
}

.nav-links a:hover::after {
  width: 100%;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.login-btn {
  padding: 10px 24px;
  border-radius: 25px;
  background: linear-gradient(90deg, var(--ember-orange) 0%, var(--coral-red) 100%);
  color: var(--soft-white);
  font-weight: 600;
  text-decoration: none;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(255, 107, 53, 0.4);
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-name {
  color: var(--cream-white);
  font-weight: 500;
}

.logout-btn {
  padding: 8px 20px;
  border: 1px solid rgba(255, 107, 53, 0.3);
  border-radius: 20px;
  background: transparent;
  color: var(--ember-orange);
  cursor: pointer;
  transition: all 0.3s ease;
}

.logout-btn:hover {
  background: rgba(255, 107, 53, 0.1);
  border-color: var(--ember-orange);
}

.main-content {
  flex: 1;
  margin-top: 80px;
}

footer {
  background: var(--deep-charcoal);
  padding: 64px 48px 32px;
  text-align: center;
  border-top: 1px solid rgba(250, 249, 246, 0.1);
}

footer p {
  color: rgba(250, 249, 246, 0.6);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 768px) {
  .navbar {
    padding: 16px 24px;
  }

  .nav-links {
    display: none;
  }

  footer {
    padding: 48px 24px 24px;
  }
}
</style>

<style>
:root {
  --ember-orange: #FF6B35;
  --coral-red: #F7931E;
  --burnt-sienna: #C44536;
  --deep-charcoal: #1A1A1A;
  --warm-grey: #2D2D2D;
  --cream-white: #FAF9F6;
  --soft-white: #FFFFFF;
  --gold: #D4AF37;
  --soft-green: #88B04B;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  background: var(--deep-charcoal);
  color: var(--cream-white);
  overflow-x: hidden;
  line-height: 1.6;
}

a {
  color: inherit;
  text-decoration: none;
}

ul {
  list-style: none;
}
</style>