<template>
  <section class="applications-section">
    <div class="container">
      <!-- 页面头部 -->
      <h1 class="section-title">AI应用列表</h1>
      <p class="section-subtitle">探索最新、最强大的AI工具和应用</p>

      <!-- 分类筛选 -->
      <div class="category-filter">
        <button 
          v-for="category in categories" 
          :key="category"
          :class="['category-btn', { active: activeCategory === category }]"
          @click="activeCategory = category"
        >
          {{ category }}
        </button>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p class="loading-text">正在加载应用数据...</p>
      </div>

      <!-- 应用列表 -->
      <div v-else class="apps-grid">
        <div v-if="applications.length > 0">
          <div 
            v-for="app in applications" 
            :key="app.id"
            class="app-card"
            @click="openAppUrl(app.url)"
          >
            <img :src="app.icon" :alt="app.name" class="app-icon">
            <h3 class="app-title">{{ app.name }}</h3>
            <p class="app-description">{{ app.description }}</p>
            <div class="app-meta">
              <span class="app-category">{{ app.category }}</span>
              <span class="app-views">{{ app.viewCount }} 次浏览</span>
            </div>
          </div>
        </div>
        <div v-else class="empty-container">
          <p class="empty-text">暂无应用数据</p>
          <p class="empty-text">数据长度: {{ applications.length }}</p>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination">
        <button class="page-btn" :disabled="currentPage === 1" @click="handlePageChange(currentPage - 1)">
          上一页
        </button>
        <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
        <button class="page-btn" :disabled="currentPage === totalPages" @click="handlePageChange(currentPage + 1)">
          下一页
        </button>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { applicationApi } from '../api'

const router = useRouter()

// 应用数据
const applications = ref<any[]>([])
const loading = ref(false)

// 分类列表
const categories = ref(['全部', '图像生成', '文本处理', '数据分析', '语音合成', '视频编辑', '代码助手'])
const activeCategory = ref('全部')

// 分页
const currentPage = ref(1)
const pageSize = ref(6)
const totalPages = ref(1)

// 加载应用数据
const loadApplications = async () => {
  console.log('开始加载应用数据');
  loading.value = true
  try {
    console.log('调用applicationApi.getApplications');
    console.log('参数:', {
      current: currentPage.value,
      size: pageSize.value,
      category: activeCategory.value !== '全部' ? activeCategory.value : undefined
    });
    const response = await applicationApi.getApplications({
      current: currentPage.value,
      size: pageSize.value,
      category: activeCategory.value !== '全部' ? activeCategory.value : undefined
    })
    console.log('API响应:', response);
    console.log('响应类型:', typeof response);
    console.log('响应结构:', {
      hasData: 'data' in response,
      hasRecords: response.data && 'records' in response.data,
      hasTotal: response.data && 'total' in response.data
    });
    applications.value = response.data?.records || []
    totalPages.value = Math.ceil((response.data?.total || 0) / pageSize.value)
    console.log('应用数据:', applications.value);
    console.log('应用数据长度:', applications.value.length);
  } catch (error) {
    console.error('Failed to load applications:', error)
  } finally {
    loading.value = false
    console.log('加载完成');
  }
}

// 处理页面变化
const handlePageChange = (page: number) => {
  currentPage.value = page
  loadApplications()
}

// 监听分类变化
watch(activeCategory, () => {
  currentPage.value = 1
  loadApplications()
})

// 打开应用URL
const openAppUrl = (url: string) => {
  if (url) {
    window.open(url, '_blank', 'noopener,noreferrer')
  }
}

// 初始化加载数据
onMounted(() => {
  loadApplications()
})
</script>

<style scoped>
.applications-section {
  padding: 96px 48px;
}

.container {
  max-width: 1440px;
  margin: 0 auto;
}

.section-title {
  font-size: clamp(2rem, 5vw, 3rem);
  font-weight: 700;
  text-align: center;
  margin-bottom: 16px;
  color: var(--cream-white);
}

.section-subtitle {
  text-align: center;
  font-size: 1.1rem;
  color: rgba(250, 249, 246, 0.7);
  margin-bottom: 64px;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.category-filter {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 48px;
  flex-wrap: wrap;
}

.category-btn {
  padding: 8px 16px;
  border: 1px solid rgba(255, 107, 53, 0.3);
  border-radius: 20px;
  background: rgba(255, 107, 53, 0.1);
  color: var(--ember-orange);
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.category-btn:hover {
  border-color: var(--ember-orange);
  background: rgba(255, 107, 53, 0.2);
}

.category-btn.active {
  background: var(--ember-orange);
  color: var(--cream-white);
  border-color: var(--ember-orange);
}

.apps-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 32px;
  margin-bottom: 48px;
}

.app-card {
  background: linear-gradient(145deg, rgba(255,107,53,0.05) 0%, rgba(196,69,54,0.05) 100%);
  padding: 32px;
  border-radius: 16px;
  border: 1px solid rgba(255, 107, 53, 0.1);
  transition: all 0.3s cubic-bezier(0.4, 0.0, 0.2, 1);
  cursor: pointer;
}

.app-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 48px rgba(255, 107, 53, 0.2);
  border-color: var(--ember-orange);
}

.app-icon {
  width: 64px;
  height: 64px;
  margin-bottom: 20px;
  border-radius: 12px;
  object-fit: cover;
}

.app-title {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 12px;
  color: var(--cream-white);
}

.app-description {
  color: rgba(250, 249, 246, 0.7);
  margin-bottom: 20px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.app-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.app-category {
  padding: 6px 16px;
  border-radius: 20px;
  background: rgba(255, 107, 53, 0.2);
  color: var(--ember-orange);
  font-size: 0.85rem;
  font-weight: 600;
}

.app-views {
  color: rgba(250, 249, 246, 0.5);
  font-size: 0.9rem;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin-top: 30px;
}

.page-btn {
  padding: 10px 20px;
  border: 1px solid rgba(255, 107, 53, 0.3);
  border-radius: 25px;
  background: transparent;
  color: var(--ember-orange);
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.page-btn:hover:not(:disabled) {
  border-color: var(--ember-orange);
  background: rgba(255, 107, 53, 0.2);
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: rgba(250, 249, 246, 0.7);
}

@media (max-width: 768px) {
  .applications-section {
    padding: 64px 24px;
  }

  .category-filter {
    gap: 8px;
  }

  .category-btn {
    padding: 6px 12px;
    font-size: 12px;
  }

  .apps-grid {
    grid-template-columns: 1fr;
    gap: 24px;
  }

  .app-card {
    padding: 24px;
  }

  .app-title {
    font-size: 1.3rem;
  }
}
</style>
