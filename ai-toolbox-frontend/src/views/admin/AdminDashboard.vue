<template>
  <div class="dashboard">
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon users">👥</div>
        <div class="stat-info">
          <span class="stat-value">{{ stats.userCount }}</span>
          <span class="stat-label">用户总数</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon apps">🚀</div>
        <div class="stat-info">
          <span class="stat-value">{{ stats.applicationCount }}</span>
          <span class="stat-label">应用数量</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon knowledge">📚</div>
        <div class="stat-info">
          <span class="stat-value">{{ stats.knowledgeCount }}</span>
          <span class="stat-label">知识库条目</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon cases">📁</div>
        <div class="stat-info">
          <span class="stat-value">{{ stats.caseCount }}</span>
          <span class="stat-label">案例数量</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon news">📰</div>
        <div class="stat-info">
          <span class="stat-value">{{ stats.newsCount }}</span>
          <span class="stat-label">资讯数量</span>
        </div>
      </div>
    </div>

    <div class="recent-section">
      <h2>最近活跃用户</h2>
      <div class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>用户名</th>
              <th>邮箱</th>
              <th>注册时间</th>
              <th>状态</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in recentUsers" :key="user.id">
              <td>{{ user.id }}</td>
              <td>{{ user.username }}</td>
              <td>{{ user.email }}</td>
              <td>{{ formatDate(user.createTime) }}</td>
              <td>
                <span class="status-badge active">正常</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="quick-actions">
      <h2>快捷操作</h2>
      <div class="actions-grid">
        <router-link to="/admin/applications" class="action-card">
          <span class="action-icon">➕</span>
          <span>添加应用</span>
        </router-link>
        <router-link to="/admin/knowledge" class="action-card">
          <span class="action-icon">➕</span>
          <span>添加知识</span>
        </router-link>
        <router-link to="/admin/cases" class="action-card">
          <span class="action-icon">➕</span>
          <span>添加案例</span>
        </router-link>
        <router-link to="/admin/news" class="action-card">
          <span class="action-icon">➕</span>
          <span>发布资讯</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

const stats = ref({
  userCount: 0,
  applicationCount: 0,
  knowledgeCount: 0,
  caseCount: 0,
  newsCount: 0
})

const recentUsers = ref<any[]>([])

const formatDate = (date: string) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

onMounted(() => {
  stats.value = {
    userCount: 1,
    applicationCount: 5,
    knowledgeCount: 3,
    caseCount: 3,
    newsCount: 3
  }

  recentUsers.value = [
    { id: 1, username: 'admin', email: 'admin@example.com', createTime: '2025-12-30T10:00:00', status: 1 }
  ]
})
</script>

<style scoped>
.dashboard {
  max-width: 1400px;
  margin: 0 auto;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.stat-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  transition: transform 0.3s, box-shadow 0.3s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.8rem;
}

.stat-icon.users { background: rgba(33, 150, 243, 0.1); }
.stat-icon.apps { background: rgba(255, 107, 53, 0.1); }
.stat-icon.knowledge { background: rgba(76, 175, 80, 0.1); }
.stat-icon.cases { background: rgba(156, 39, 176, 0.1); }
.stat-icon.news { background: rgba(255, 193, 7, 0.1); }

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 2rem;
  font-weight: 700;
  color: #1a1a2e;
}

.stat-label {
  font-size: 0.9rem;
  color: #999;
  margin-top: 4px;
}

.recent-section {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 32px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.recent-section h2 {
  font-size: 1.2rem;
  font-weight: 600;
  color: #1a1a2e;
  margin-bottom: 20px;
}

.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
}

.data-table th {
  font-weight: 600;
  color: #666;
  font-size: 0.9rem;
  background: #fafafa;
}

.data-table td {
  color: #333;
  font-size: 0.95rem;
}

.data-table tbody tr:hover {
  background: #fafafa;
}

.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
}

.status-badge.active {
  background: rgba(76, 175, 80, 0.1);
  color: #4caf50;
}

.quick-actions {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.quick-actions h2 {
  font-size: 1.2rem;
  font-weight: 600;
  color: #1a1a2e;
  margin-bottom: 20px;
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
}

.action-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  background: linear-gradient(135deg, rgba(255, 107, 53, 0.05) 0%, rgba(255, 107, 53, 0.1) 100%);
  border: 1px solid rgba(255, 107, 53, 0.2);
  border-radius: 12px;
  color: #FF6B35;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s;
}

.action-card:hover {
  background: linear-gradient(135deg, rgba(255, 107, 53, 0.1) 0%, rgba(255, 107, 53, 0.15) 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.2);
}

.action-icon {
  font-size: 1.2rem;
}
</style>
