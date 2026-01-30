<template>
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <div class="sidebar-header">
        <router-link to="/" class="admin-logo">⚡ AI工具箱</router-link>
        <span class="admin-badge">管理后台</span>
      </div>
      <nav class="admin-nav">
        <ul>
          <li>
            <router-link to="/admin" class="nav-item" :class="{ active: currentRoute === 'dashboard' }">
              <span class="nav-icon">📊</span>
              <span>数据概览</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/users" class="nav-item" :class="{ active: currentRoute === 'users' }">
              <span class="nav-icon">👥</span>
              <span>用户管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/applications" class="nav-item" :class="{ active: currentRoute === 'applications' }">
              <span class="nav-icon">🚀</span>
              <span>应用管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/knowledge" class="nav-item" :class="{ active: currentRoute === 'knowledge' }">
              <span class="nav-icon">📚</span>
              <span>知识库管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/cases" class="nav-item" :class="{ active: currentRoute === 'cases' }">
              <span class="nav-icon">📁</span>
              <span>案例管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/news" class="nav-item" :class="{ active: currentRoute === 'news' }">
              <span class="nav-icon">📰</span>
              <span>资讯管理</span>
            </router-link>
          </li>
        </ul>
      </nav>
      <div class="sidebar-footer">
        <router-link to="/" class="back-home">
          <span>🏠</span>
          <span>返回前台</span>
        </router-link>
      </div>
    </aside>
    <main class="admin-main">
      <header class="admin-header">
        <div class="header-left">
          <h1>{{ pageTitle }}</h1>
        </div>
        <div class="header-right">
          <span class="admin-user">👤 管理员</span>
          <button @click="handleLogout" class="logout-btn">退出登录</button>
        </div>
      </header>
      <div class="admin-content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const currentRoute = computed(() => route.name as string || '')

const pageTitle = computed(() => {
  const titles: Record<string, string> = {
    dashboard: '数据概览',
    users: '用户管理',
    applications: '应用管理',
    knowledge: '知识库管理',
    cases: '案例管理',
    news: '资讯管理'
  }
  return titles[currentRoute.value] || '管理后台'
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

onMounted(() => {
  const token = localStorage.getItem('token')
  if (!token) {
    router.push('/login')
  }
})
</script>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  background: #f5f7fa;
}

.admin-sidebar {
  width: 260px;
  background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
  color: #fff;
  display: flex;
  flex-direction: column;
  position: fixed;
  height: 100vh;
  left: 0;
  top: 0;
}

.sidebar-header {
  padding: 24px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.admin-logo {
  display: block;
  font-size: 1.4rem;
  font-weight: 700;
  color: #fff;
  text-decoration: none;
  margin-bottom: 8px;
}

.admin-badge {
  display: inline-block;
  padding: 4px 12px;
  background: rgba(255, 107, 53, 0.2);
  color: #FF6B35;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
}

.admin-nav {
  flex: 1;
  padding: 16px 0;
  overflow-y: auto;
}

.admin-nav ul {
  list-style: none;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 24px;
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  transition: all 0.3s;
  border-left: 3px solid transparent;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.05);
  color: #fff;
}

.nav-item.active {
  background: rgba(255, 107, 53, 0.1);
  color: #FF6B35;
  border-left-color: #FF6B35;
}

.nav-icon {
  font-size: 1.2rem;
}

.sidebar-footer {
  padding: 16px 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.back-home {
  display: flex;
  align-items: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.6);
  text-decoration: none;
  font-size: 0.9rem;
  transition: color 0.3s;
}

.back-home:hover {
  color: #FF6B35;
}

.admin-main {
  flex: 1;
  margin-left: 260px;
  display: flex;
  flex-direction: column;
}

.admin-header {
  background: #fff;
  padding: 20px 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 10;
}

.header-left h1 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1a1a2e;
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.admin-user {
  color: #666;
  font-size: 0.95rem;
}

.logout-btn {
  padding: 8px 20px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background: #fff;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 0.9rem;
}

.logout-btn:hover {
  border-color: #FF6B35;
  color: #FF6B35;
}

.admin-content {
  flex: 1;
  padding: 32px;
  background: #f5f7fa;
}

@media (max-width: 1024px) {
  .admin-sidebar {
    width: 220px;
  }

  .admin-main {
    margin-left: 220px;
  }
}

@media (max-width: 768px) {
  .admin-sidebar {
    width: 60px;
  }

  .sidebar-header .admin-badge,
  .nav-item span:not(.nav-icon),
  .sidebar-footer span:not(:first-child) {
    display: none;
  }

  .nav-item {
    justify-content: center;
    padding: 14px;
  }

  .admin-main {
    margin-left: 60px;
  }
}
</style>
